package DAOTests;

import DAOs.*;
import Models.AuthToken;
import Models.Person;
import Models.User;
import MyExceptions.DataAccessException;
import Models.Event;
import MyExceptions.UserAlreadyRegisteredException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class UserDaoTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;


    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestUser = new User("admin", "password123", "admin@byu.edu", "Randall",
                "Munroe", "m", "xkcd_123");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests

        db.clearPersonTable();
        //Then we pass that connection to the UserDao so it can access the database
        uDao = new UserDao(conn);
        //Does having multiple DAOs with the same connection work????
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertUserPass() throws DataAccessException, UserAlreadyRegisteredException {
        //Insert a user
        uDao.insertUser(bestUser);
        //Get the user we just inserted
        User toCompareTo = uDao.getUser(bestUser.getUsername(), bestUser.getPassword());
        //Make sure we got something back
        assertNotNull(toCompareTo);
        //Make sure the users are equal
        assertEquals(bestUser, toCompareTo);
    }

    @Test
    public void insertUserFail() throws DataAccessException, UserAlreadyRegisteredException {
        // test the insert to the user db
        uDao.insertUser(bestUser);
        //Make sure that trying to insert an identical user throws a dataAccessException
        assertThrows(DataAccessException.class, ()-> uDao.insertUser(bestUser));
    }

    @Test
    public void retrievalPass() throws DataAccessException, UserAlreadyRegisteredException {
        //add something to database
        uDao.insertUser(bestUser);

        //check if retrieval matches for one case with different authtokens maybe
        User compareTest = uDao.getUser(bestUser.getUsername(), bestUser.getPassword());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);


        //adding another user with the same password
        User secondBestUser = new User("customer", "password123", "student@uvu.edu", "Mandall",
                "Runroe", "m", "smbc_567");
        uDao.insertUser(secondBestUser);

        //make sure you only get the first user
        User shouldBeFirstUser = uDao.getUser(bestUser.getUsername(), bestUser.getPassword());
        //Make sure the users are equal
        assertEquals(bestUser, shouldBeFirstUser);
    }

    @Test
    public void retrievalFail() throws DataAccessException, UserAlreadyRegisteredException {
        //try to get something after inserting nothing beforehand
        assertNull(uDao.getUser(bestUser.getUsername(), bestUser.getPassword()));
        //try to get something after giving a wrong username
        uDao.insertUser(bestUser);
        assertNull(uDao.getUser("incorrect_username", bestUser.getPassword()));
        //try to get something after giving wrong password
        assertNull(uDao.getUser(bestUser.getUsername(), "incorrect_password"));
        //try to get something after giving both an incorrect username and incorrect password
        assertNull(uDao.getUser("incorrect_username", "incorrect_password"));
    }

    @Test
    public void clearPass() throws DataAccessException, UserAlreadyRegisteredException {
        //clear the User table
        uDao.insertUser(bestUser);
        uDao.clearUsers();
        //try to access something (should be empty)
        assertNull(uDao.getUser(bestUser.getUsername(), bestUser.getPassword()));
    }
}
