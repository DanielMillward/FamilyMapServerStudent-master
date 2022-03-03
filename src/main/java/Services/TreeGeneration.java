package Services;

import DAOs.*;
import LocalJSONObjects.Location;
import LocalJSONObjects.LocationList;
import LocalJSONObjects.NamesList;
import Models.*;
import MyExceptions.DataAccessException;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Random;

import static Services.TreeGeneration.Genders.FEMALE;
import static Services.TreeGeneration.Genders.MALE;

public class TreeGeneration {

    Person generateTree(String gender, int generations, String username, String firstName, String lastName, int birthYear, String userPersonID, Connection conn) throws FileNotFoundException {
        /*
        Generate mother and father (if necessary)
         */
        FullPerson userMother = null;
        FullPerson userFather = null;
        if (generations >= 1) {
            userMother = generatePerson(FEMALE, generations, username, birthYear, conn);
            userFather = generatePerson(MALE, generations, username, birthYear, conn);
            // Set mother's and father's spouse IDs
            setSpouseIDInDatabase(userMother, userFather, conn);
            //calls saveEventInDB, also generates the year
            addMarriageEventInDatabase(birthYear, userMother, userFather, username, conn);
        }

        /*
        make details of current person
         */
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
        // Save birth in database
        saveEventInDB(userBirth, conn);
        // save person in database
        savePersonInDB(person, conn);
        return person;
    }

    FullPerson generatePerson(Genders gender, int generations, String username, int childBirthYear, Connection conn) {
        FullPerson mother = null;
        FullPerson father = null;
        int personBirthYear = generateBirthYear(gender, childBirthYear);
        /*
        Recursively add parents
         */
        if (generations > 1) {
            mother = generatePerson(FEMALE, generations -1, username, personBirthYear, conn);
            father = generatePerson(MALE, generations -1, username, personBirthYear, conn);

            // Set mother's and father's spouse IDs
            setSpouseIDInDatabase(mother, father, conn);
            //calls saveEventInDB, also generates the year
            addMarriageEventInDatabase(personBirthYear, mother, father, username, conn);
        }

        /*
        Make details of the person being made
         */
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
        //Save birth in database
        Event birthEvent = getNewEventObject("birth", username, newPersonID, personBirthYear);
        saveEventInDB(birthEvent, conn);
        //save death in database
        Event deathEvent = getNewEventObject("death", username, newPersonID, personDeathYear);
        saveEventInDB(deathEvent, conn);
        // Save person in database
        savePersonInDB(person, conn);

        return new FullPerson(person, personBirthYear, personDeathYear);
    }

    private void savePersonInDB(Person person, Connection conn) {
        boolean commit = false;
        try {
            // Open database connection & make DAOs
            PersonDao pDao = new PersonDao(conn);
            // Insert event into database
            pDao.insert(person);
            // Close database connection, COMMIT transaction
            commit = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction

        }
    }

    private void addMarriageEventInDatabase(int birthYear, FullPerson userMother, FullPerson userFather, String username, Connection conn) {
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

        Event mMarrEv = getNewEventObject("marriage", username,
                userMother.getPerson().getPersonID(), marriageYear);
        Event fMarrEv = new Event(getNewEventID("marriage",userFather.getPerson().getPersonID()), username, userFather.getPerson().getPersonID(),
                mMarrEv.getLatitude(), mMarrEv.getLongitude(),
                mMarrEv.getCountry(), mMarrEv.getCity(), "marriage", mMarrEv.getYear());

                //getNewEventObject("marriage", username, userFather.getPerson().getPersonID(), marriageYear);
        saveEventInDB(mMarrEv, conn);
        saveEventInDB(fMarrEv, conn);
    }

    private void setSpouseIDInDatabase(FullPerson userMother, FullPerson userFather, Connection conn) {
        boolean commit = false;
        try {
            // Open database connection & make DAOs
            PersonDao pDao = new PersonDao(conn);
            // Insert event into database
            pDao.setSpouseID(userMother.getPerson().getPersonID(), userFather.getPerson().getPersonID());
            pDao.setSpouseID(userFather.getPerson().getPersonID(), userMother.getPerson().getPersonID());
            // Close database connection, COMMIT transaction
            commit = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction

        }
    }

    private void saveEventInDB(Event event, Connection conn) {
        boolean commit = false;
        try {
            // Open database connection & make DAOs
            EventDao eDao = new EventDao(conn);
            // Insert event into database
            eDao.insert(event);
            commit = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction
        }
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
        //Turns data into an event object
        String newEventID = getNewEventID(eventType, personID);
        Location newLocation = getNewLocation();

        return new Event(newEventID, username, personID,
                newLocation.getLatitude(), newLocation.getLongitude(),
                newLocation.getCountry(), newLocation.getCity(), eventType, year);
    }

    private String getRandomFirstName(Genders gender) {
        try {
            // create Gson instance
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader fnamesreader = Files.newBufferedReader(Paths.get("json/fnames.json"));
            Reader mnamesreader = Files.newBufferedReader(Paths.get("json/mnames.json"));
            //System.out.println(fnamesreader);
            // convert JSON array to list of users - TEST THIS!!!!!
            NamesList fdataObj = gson.fromJson(fnamesreader, NamesList.class);
            NamesList mdataObj = gson.fromJson(mnamesreader, NamesList.class);

            String[] fdata = fdataObj.getList().toArray(new String[0]);
            String[] mdata = mdataObj.getList().toArray(new String[0]);
            //String[] mdata = gson.fromJson(mnamesreader, String[].class);
            // close reader
            fnamesreader.close();
            mnamesreader.close();
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
            Reader snamesreader = Files.newBufferedReader(Paths.get("json/snames.json"));
            // convert JSON array to list of users - TEST THIS!!!!!
            NamesList sdataObj = gson.fromJson(snamesreader, NamesList.class);

            String[] sdata = sdataObj.getList().toArray(new String[0]);
            // close reader
            snamesreader.close();
            Random r = new Random();

            return sdata[r.nextInt(sdata.length)];
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
            LocationList ldataObj = gson.fromJson(reader, LocationList.class);
            Location[] ldata = ldataObj.getList().toArray(new Location[0]);
            // close reader
            reader.close();
            Random r = new Random();

            return ldata[r.nextInt(ldata.length)];
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

    public String getNewPersonID(String firstName, String lastName) {
        long randNum = System.currentTimeMillis();
        int lastDigits = (int) (randNum % 1000000);
        return firstName + lastName + Integer.toString(lastDigits);
    }

    public enum Genders {
        MALE,
        FEMALE
    }
}
