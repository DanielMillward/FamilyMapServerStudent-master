package ScratchTests;

import LocalJSONObjects.NamesList;
import MyExceptions.DataAccessException;
import DAOs.Database;
import DAOs.EventDao;
import Models.Event;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class ScratchTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests

        db.clearEventTable();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void gsonPass() throws DataAccessException, IOException {
        // create Gson instance
        Gson gson = new Gson();
        // create a reader
        Reader fnamesreader = Files.newBufferedReader(Paths.get("json/fnames.json"));
        Reader mnamesreader = Files.newBufferedReader(Paths.get("json/mnames.json"));
        System.out.println(fnamesreader);
        // convert JSON array to list of users - TEST THIS!!!!!!


        NamesList fdataObj = gson.fromJson(fnamesreader, NamesList.class);
        String[] fdata = fdataObj.getList().toArray(new String[0]);
        //String[] mdata = gson.fromJson(mnamesreader, String[].class);
        // close reader
        fnamesreader.close();
        mnamesreader.close();

        assertNotNull(fdata);
        System.out.println(fdata[0]);
        //assertNotNull(mdata);
    }

}