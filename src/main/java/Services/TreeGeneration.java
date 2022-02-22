package Services;

import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.EventDao;
import DAOs.UserDao;
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
        FullPerson userMother = null;
        FullPerson userFather = null;
        if (generations >= 1) {
            userMother = generatePerson(FEMALE, generations, username, birthYear);
            userFather = generatePerson(MALE, generations, username, birthYear);

            // Set mother's and father's spouse IDs
            setSpouseIDInDatabase(userMother, userFather);

            //marriage year must be 13 years before birthYear
            //marriage must be before either death year
            addMarriageEventInDatabase(birthYear, userMother, userFather);
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

    private void saveEventInDB(Event userBirth) {
        
    }

    private int getMarriageYear(int birthYear, String personID, String personID1) {

    }

    FullPerson generatePerson(Genders gender, int generations, String username, int childBirthYear) {
        FullPerson mother = null;
        FullPerson father = null;
        int personBirthYear = childBirthYear - 69;
        if (generations > 1) {
            mother = generatePerson(FEMALE, generations -1, username, personBirthYear);
            father = generatePerson(MALE, generations -1, username, personBirthYear);

            // Set mother's and father's spouse IDs

            // Add marriage events to mother and father
            // (their marriage events must be in synch with each other
        }

        //make details of current person

        Person person= new Person();
        // Generate events for person (except marriage)
        int birthyear = 0;
        int deathyear = 0;
        // Save person in database
        return new FullPerson(person, birthyear, deathyear);
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
