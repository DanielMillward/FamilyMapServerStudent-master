package DAOTests;

import DAOs.AuthTokenDao;
import DAOs.PersonDao;
import Models.AuthToken;
import Models.Person;
import MyExceptions.DataAccessException;
import DAOs.Database;
import DAOs.EventDao;
import Models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;
    private AuthTokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestPerson = new Person("Person_456B", "Storm", "Elon",
                "Musk", "f", "dad_12", "mom_34","spouse_78");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests

        db.clearPersonTable();
        //Then we pass that connection to the EventDAO so it can access the database
        pDao = new PersonDao(conn);
        //Does having multiple DAOs with the same connection work????
        aDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        pDao.insert(bestPerson);
        //So lets use a find method to get the event that we just put in back out
        Person compareTest = pDao.getPersons(null, bestPerson.getPersonID()).get(0);
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        pDao.insert(bestPerson);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> pDao.insert(bestPerson));
    }

    @Test
    public void retrievalPass() throws DataAccessException {
        //add something to database
        pDao.insert(bestPerson);

        //check if retrieval matches for one case with different authtokens maybe
        Person compareTest = pDao.getPersons(null, bestPerson.getPersonID()).get(0);
        Person compareTest2 = pDao.getPersons("this param shouldnt matter", bestPerson.getPersonID()).get(0);
        assertNotNull(compareTest);
        assertNotNull(compareTest2);
        assertEquals(bestPerson, compareTest);
        assertEquals(bestPerson, compareTest2);

        //add an authtoken to the authtoken database
        aDao.addAuthToken(new AuthToken("myToken", "Storm"));

        //array of both of Storm's people
        ArrayList<Person> userStormList = new ArrayList<>();
        Person secondBestPerson = new Person("Person_number_2", "Storm", "Elon",
                "Musk", "f", "dad_12", "mom_34","spouse_78");
        userStormList.add(bestPerson);
        userStormList.add(secondBestPerson);

        //check if retrieving everything works too
        pDao.insert(secondBestPerson);
        ArrayList<Person> getAllPersons = pDao.getPersons("myToken", null);
        assertNotNull(getAllPersons);
        assertEquals(userStormList, getAllPersons);
    }

    @Test
    public void retrievalFail() throws DataAccessException {
        //try to get something after inserting nothing beforehand
        assertNull(pDao.getPersons(null, bestPerson.getPersonID()));
        //try to get something after giving a wrong personID
        pDao.insert(bestPerson);
        assertNull(pDao.getPersons(null, "Not_a_valid_id"));
        //try to get something after giving wrong authtoken
        aDao.addAuthToken(new AuthToken("myToken", "Storm"));
        assertNull(pDao.getPersons("invalid_authToken", null));
    }

    @Test
    public void clearPass() throws DataAccessException {
        //clear the person table
        pDao.insert(bestPerson);
        pDao.clearPersons();
        //try to access something (should be empty)
        ArrayList<Person> compareTest = pDao.getPersons("MyToken", null);
        assertNull(compareTest);
    }
}