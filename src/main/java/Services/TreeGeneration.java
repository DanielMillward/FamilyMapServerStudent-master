package Services;

import DAOs.*;
import Models.*;
import MyExceptions.DataAccessException;
import RequestResult.LoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Services.TreeGeneration.Genders.FEMALE;
import static Services.TreeGeneration.Genders.MALE;

public class TreeGeneration {

    Person generateTree(String gender, int generations, String username, String firstName, String lastName, int birthYear) throws FileNotFoundException {
        /*
        Generate mother and father (if necessary)
         */
        FullPerson userMother = null;
        FullPerson userFather = null;
        if (generations >= 1) {
            userMother = generatePerson(FEMALE, generations, username, birthYear);
            userFather = generatePerson(MALE, generations, username, birthYear);
            // Set mother's and father's spouse IDs
            setSpouseIDInDatabase(userMother, userFather);
            //calls saveEventInDB, also generates the year
            addMarriageEventInDatabase(birthYear, userMother, userFather, username);
        }

        /*
        make details of current person
         */
        String userPersonID = getNewPersonID(firstName, lastName);
        String fatherID;
        String motherID;
        if (userFather == null || userMother == null) {
            fatherID = null;
            motherID = null;
        } else {
            fatherID = userFather.getPerson().getPersonID();
            motherID = userMother.getPerson().getPersonID();
        }
        Person person= new Person(userPersonID, username, firstName, lastName,
                gender, fatherID, motherID, null);
        // Generate birth event for user
        Event userBirth = getNewEventObject("birth", username, userPersonID, birthYear);
        // Save person in database
        saveEventInDB(userBirth);
        return person;
    }

    private void addMarriageEventInDatabase(int birthYear, FullPerson userMother, FullPerson userFather, String username) {
        //must be 13 years after mother or father birth
        int secondOldYear = userMother.getBirthYear() + 13;
        int firstOldYear = userFather.getBirthYear() + 13;
        int oldYear = 0;
        if (secondOldYear > firstOldYear) {
            oldYear = secondOldYear;
        } else {
            oldYear = firstOldYear;
        }
        // must be before mother or father death
        int firstRecentYear = userMother.getDeathYear();
        int secondRecentYear = userFather.getDeathYear();
        int recentYear = 9999;
        if (firstRecentYear < secondRecentYear) {
            recentYear = firstRecentYear;
        } else {
            recentYear = secondRecentYear;
        }

        Random r = new Random();
        int marriageYear = r.nextInt(recentYear - oldYear) + oldYear;

        Event motherMarriageEvent = getNewEventObject("marriage", username,
                userMother.getPerson().getPersonID(), marriageYear);
        Event fatherMarriageEvent = getNewEventObject("marriage", username,
                userFather.getPerson().getPersonID(), marriageYear);
        saveEventInDB(motherMarriageEvent);
        saveEventInDB(fatherMarriageEvent);
    }

    private void setSpouseIDInDatabase(FullPerson userMother, FullPerson userFather) {
        Database db= new Database();
        try {
            // Open database connection & make DAOs
            db.openConnection();
            PersonDao pDao = new PersonDao(db.getConnection());
            // Insert event into database
            pDao.setSpouseID(userMother.getPerson().getPersonID(), userFather.getPerson().getPersonID());
            pDao.setSpouseID(userFather.getPerson().getPersonID(), userMother.getPerson().getPersonID());
            // Close database connection, COMMIT transaction
            db.closeConnection(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEventInDB(Event event) {
        Database db= new Database();
        try {
            // Open database connection & make DAOs
            db.openConnection();
            EventDao eDao = new EventDao(db.getConnection());
            // Insert event into database
            eDao.insert(event);
            // Close database connection, COMMIT transaction
            db.closeConnection(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }


    FullPerson generatePerson(Genders gender, int generations, String username, int childBirthYear) {
        FullPerson mother = null;
        FullPerson father = null;
        int personBirthYear = generateBirthYear(gender, childBirthYear);
        if (generations > 1) {
            mother = generatePerson(FEMALE, generations -1, username, personBirthYear);
            father = generatePerson(MALE, generations -1, username, personBirthYear);

            mother = generatePerson(FEMALE, generations, username, personBirthYear);
            father = generatePerson(MALE, generations, username, personBirthYear);
            // Set mother's and father's spouse IDs
            setSpouseIDInDatabase(mother, father);
            //calls saveEventInDB, also generates the year
            addMarriageEventInDatabase(personBirthYear, mother, father, username);
        }

        //make details of current person
        int personDeathYear = generateDeathYear(childBirthYear, personBirthYear);
        String personFirstName = getRandomFirstName(gender);
        String personLastName = getRandomLastName();
        String newPersonID = getNewPersonID(personFirstName, personLastName);
        String newFatherID;
        String newMotherID;
        if (father == null || mother == null) {
            newFatherID = null;
            newMotherID = null;
        } else {
            newFatherID = father.getPerson().getPersonID();
            newMotherID = mother.getPerson().getPersonID();
        }
        String personGender;
        if (gender == MALE) {
            personGender = "m";
        }  else {
            personGender = "f";
        }
        Person person= new Person(newPersonID, username, personFirstName, personLastName,
                personGender, newFatherID, newMotherID, null);
        // Save person in database
        return new FullPerson(person, personBirthYear, personDeathYear);
    }

    private int generateDeathYear(int childBirthYear, int personBirthYear) {
        Random r = new Random();
        int maxYear = personBirthYear + 120;
        return r.nextInt(maxYear - childBirthYear) + childBirthYear;
    }

    private int generateBirthYear(Genders gender, int childBirthYear) {
        Random r = new Random();
        int maxYear = childBirthYear - 13;
        int minYearF = childBirthYear - 50;
        int minYearM = childBirthYear - 119;
        if (gender == MALE) {
            return r.nextInt(maxYear - minYearM) + minYearM;
        } else {
            return r.nextInt(maxYear - minYearF) + minYearF;
        }
    }

    private Event getNewEventObject(String eventType, String username, String personID, int year) {
        String newEventID = getNewEventID(eventType, personID);
        Location newLocation = getNewLocation();

        Event newEvent = new Event(newEventID, username, personID,
                newLocation.getLatitude(), newLocation.getLongitude(),
                newLocation.getCountry(), newLocation.getCity(), eventType, year);

        return newEvent;
    }

    private String getRandomFirstName(Genders gender) {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader fnamesreader = Files.newBufferedReader(Paths.get("json/fnames.json"));
            Reader mnamesreader = Files.newBufferedReader(Paths.get("json/mnames.json"));
            // convert JSON array to list of users - TEST THIS!!!!!!
            String[] fdata = gson.fromJson(fnamesreader, String[].class);
            String[] mdata = gson.fromJson(mnamesreader, String[].class);
            // close reader
            fnamesreader.close();
            mnamesreader.close();;
            Random r = new Random();

            if (gender == FEMALE) {
                return fdata[r.nextInt(fdata.length)];
            } else {
                return mdata[r.nextInt(mdata.length)];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getRandomLastName() {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("json/snames.json"));
            // convert JSON array to list of users - TEST THIS!!!!!!
            String[] data = gson.fromJson(reader, String[].class);
            // close reader
            reader.close();
            Random r = new Random();

            return data[r.nextInt(data.length)];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Location getNewLocation() {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("json/locations.json"));
            // convert JSON array to list of users - TEST THIS!!!!!!
            Location[] data = gson.fromJson(reader, Location[].class);
            // close reader
            reader.close();
            Random r = new Random();

            return data[r.nextInt(data.length)];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getNewEventID(String eventType, String userPersonID) {
        long randNum = System.currentTimeMillis();
        int lastDigits = (int) (randNum % 100);
        return eventType + userPersonID + Integer.toString(lastDigits);
    }

    private String getNewPersonID(String firstName, String lastName) {
        long randNum = System.currentTimeMillis();
        int lastDigits = (int) (randNum % 100000);
        return firstName + lastName + Integer.toString(lastDigits);
    }

    public enum Genders {
        MALE,
        FEMALE
    }
}
