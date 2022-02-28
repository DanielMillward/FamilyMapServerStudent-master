package FinalTests;

import DAOs.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.UserAlreadyRegisteredException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BigDAOTest {
    private Database db;

    private User testUser;

    private Event firstEvent;
    private Event secondEvent;
    private Event thirdEvent;

    private Person firstPerson;
    private Person secondPerson;

    private AuthToken userToken;
    private AuthToken altToken;

    private EventDao eDao;
    private PersonDao pDao;
    private UserDao uDao;
    private AuthTokenDao aDao;


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        //test users
        testUser = new User("ElonMusk", "admin", "elon@spacex.com",
                "Elon", "Musk", "m", "elon1234");

        //test persons
        firstPerson = new Person("Person_num_1", "ElonMusk", "Elon",
                "Musk", "m", "dad_12", "mom_34","spouse_78");
        secondPerson = new Person("Person_num_2", "ElonMusk", "Amelia",
                "Airheart", "f", "air_dad1", "air_mom2","spouse_66");

        //test events
        firstEvent = new Event("Biking_123A", "ElonMusk", "Person_num_1",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Birth_Somewhere_23", "ElonMusk", "Person_num_2",
                35.9f, 140.1f, "Japan", "Ushiku",
                "birth", 1999);

        //test token
        userToken = new AuthToken("MyAuthToken", "ElonMusk");
        altToken = new AuthToken("Another_Token", "ElonMusk");

        //Connect to fresh database
        Connection conn = db.getConnection();
        db.clearUserTable();
        db.clearAuthTokenTable();
        db.clearEventTable();
        db.clearPersonTable();
        //Make DAOs
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void getAuthTokenPass() throws DataAccessException {
        //add something to database
        aDao.addAuthToken(userToken);

        //check if retrieval matches for one case with different authTokens maybe
        AuthToken compareTest = aDao.getAuthToken(userToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(userToken, compareTest);
    }

    @Test
    public void getAuthTokenFail() throws DataAccessException {
        //try retrieving on an empty db
        assertThrows(DataAccessException.class, () -> aDao.getAuthToken(userToken.getAuthtoken()));

        //not present in db
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> aDao.getAuthToken("Some_random_token"));
        assertThrows(DataAccessException.class, () -> aDao.getAuthToken(""));
    }

    @Test
    public void addAuthTokenPass() throws DataAccessException {
        // User already there (should be fine)
        aDao.addAuthToken(userToken);
        aDao.addAuthToken(altToken);
        AuthToken compareTest = aDao.getAuthToken(userToken.getAuthtoken());
        AuthToken compareTest2 = aDao.getAuthToken(altToken.getAuthtoken());
        assertNotNull(compareTest);
        assertNotNull(compareTest2);
        assertEquals(userToken, compareTest);
        assertEquals(altToken, compareTest2);
    }

    @Test
    public void addAuthTokenFail() throws DataAccessException {
        //Null auth token given
        AuthToken nullToken = new AuthToken(null, null);
        assertThrows(DataAccessException.class, () -> aDao.addAuthToken(nullToken));
        //exact same authtoken given
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> aDao.addAuthToken(userToken));
    }

    @Test
    public void eventInsertPass() throws DataAccessException {
        // Getting events
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        aDao.addAuthToken(userToken);
        ArrayList<Event> compareTest = eDao.getEvents(userToken.getAuthtoken(), null);
        assertNotNull(compareTest);
        ArrayList<Event> realEvents = new ArrayList<>();
        realEvents.add(firstEvent);
        realEvents.add(secondEvent);
        assertEquals(realEvents, compareTest);
    }

    @Test
    public void eventInsertFail() throws DataAccessException {
        //Null and incomplete ones given
        Event nullEvent = new Event(null, null, null, 0, 0, null, null, null,0);
        Event incompleteEvent = new Event(null, "ElonMusk", null, 69, 42, null, "Shangrila", null,0);
        assertThrows(DataAccessException.class, () -> eDao.insert(null));
        assertThrows(DataAccessException.class, () -> eDao.insert(nullEvent));
        assertThrows(DataAccessException.class, () -> eDao.insert(incompleteEvent));
        //exact same event given
        eDao.insert(firstEvent);
        assertThrows(DataAccessException.class, () -> eDao.insert(firstEvent));
    }

    @Test
    public void getEventsPass() throws DataAccessException {
        //add something to database & a logged in user
        eDao.insert(firstEvent);
        aDao.addAuthToken(userToken);

        //check if retrieval matches for one case with different authTokens maybe
        Event compareTest = eDao.getEvents(userToken.getAuthtoken(), firstEvent.getEventID()).get(0);
        assertNotNull(compareTest);
        assertEquals(firstEvent, compareTest);
    }

    @Test
    public void getEventsFail() throws DataAccessException {
        //try retrieving on an empty db and no login
        assertThrows(DataAccessException.class, () -> eDao.getEvents("blahblah", firstEvent.getEventID()));

        //not present in db, logged in
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> eDao.getEvents(userToken.getAuthtoken(), firstEvent.getEventID()));

        //in DB, not logged in
        db.clearAuthTokenTable();
        assertThrows(DataAccessException.class, () -> eDao.getEvents(userToken.getAuthtoken(), firstEvent.getEventID()));
    }

    @Test
    public void personInsertPass() throws DataAccessException {
        // Getting events
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        aDao.addAuthToken(userToken);
        ArrayList<Person> compareTest = pDao.getPersons(userToken.getAuthtoken(), null);
        assertNotNull(compareTest);
        ArrayList<Person> realPersons = new ArrayList<>();
        realPersons.add(firstPerson);
        realPersons.add(secondPerson);
        assertEquals(realPersons, compareTest);
    }

    @Test
    public void personInsertFail() throws DataAccessException {
        //Null and incomplete ones given
        Person nullPerson = new Person(null,null, null,
                null, null, null, null,null);
        Person incompletePerson = new Person(firstPerson.getPersonID(),null, "Bastion",
                null, "f", null, "OrisaOnline",null);
        assertThrows(DataAccessException.class, () -> pDao.insert(null));
        assertThrows(DataAccessException.class, () -> pDao.insert(nullPerson));
        assertThrows(DataAccessException.class, () -> pDao.insert(incompletePerson));

        //exact same event given
        pDao.insert(firstPerson);
        assertThrows(DataAccessException.class, () -> pDao.insert(firstPerson));
    }

    @Test
    public void getPersonsPass() throws DataAccessException {
        //add something to database & a logged in user
        pDao.insert(firstPerson);
        aDao.addAuthToken(userToken);

        //check if retrieval matches for one case with different authTokens maybe
        Person compareTest = pDao.getPersons(userToken.getAuthtoken(), firstPerson.getPersonID()).get(0);
        assertNotNull(compareTest);
        assertEquals(firstPerson, compareTest);
    }

    @Test
    public void getPersonsFail() throws DataAccessException {
        //try retrieving on an empty db and no login
        assertThrows(DataAccessException.class, () -> pDao.getPersons(userToken.getAuthtoken(), firstPerson.getPersonID()));

        //not present in db, logged in
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> pDao.getPersons(userToken.getAuthtoken(), firstPerson.getPersonID()));

        //in DB, not logged in
        db.clearAuthTokenTable();
        assertThrows(DataAccessException.class, () -> pDao.getPersons(userToken.getAuthtoken(), firstPerson.getPersonID()));
    }

    @Test
    public void clearPersonsPass() throws DataAccessException {
        //clear the person table
        pDao.insert(firstPerson);
        pDao.clearPersons();

        //try to access something (should throw DataAccessException)
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> pDao.getPersons(userToken.getAuthtoken(), null));
    }

    @Test
    public void clearPersonsPassTwo() throws DataAccessException {
        //clear without data in it
        pDao.clearPersons();
        //try to access something (should throw DataAccessException)
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> pDao.getPersons(userToken.getAuthtoken(), null));
    }

    @Test
    public void setSpouseIDPass() throws DataAccessException {
        //add person, set its spouse, get it and check spouseID
        pDao.insert(firstPerson);
        pDao.setSpouseID(firstPerson.getPersonID(), "testSpouseID");

        aDao.addAuthToken(userToken);
        Person compareTest = pDao.getPersons(userToken.getAuthtoken(), firstPerson.getPersonID()).get(0);

        assertEquals("testSpouseID", compareTest.getSpouseID());
    }

    @Test
    public void setSpouseIDFail() throws DataAccessException {
        //no personID given
        assertThrows(DataAccessException.class, () -> pDao.setSpouseID(null, "TestSpouseID"));

        //no spouseID given
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> pDao.setSpouseID(firstPerson.getPersonID(), null));
    }

    @Test
    public void getUserPass() throws UserAlreadyRegisteredException, DataAccessException {
        //add something to database
        uDao.insertUser(testUser);

        //check if retrieval matches for one case with different authTokens maybe
        User compareTest = uDao.getUser(testUser.getUsername(), testUser.getPassword());
        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void getUserFail() throws DataAccessException {
        //try retrieving on an empty db and no login
        assertThrows(DataAccessException.class, () -> uDao.getUser(testUser.getUsername(), testUser.getPassword()));

        //not present in db, logged in
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> uDao.getUser(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    public void getUserByUsernamePass() throws UserAlreadyRegisteredException, DataAccessException {
        //add something to database
        uDao.insertUser(testUser);

        //check if retrieval matches for one case with different authTokens maybe
        User compareTest = uDao.getUserByUsername(testUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void getUserByUsernameFail() throws DataAccessException {
        //try retrieving on an empty db and no login
        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(testUser.getUsername()));

        //not present in db, logged in
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void insertUserPass() throws UserAlreadyRegisteredException, DataAccessException {
        uDao.insertUser(testUser);
        User compareTest = uDao.getUser(testUser.getUsername(), testUser.getPassword());

        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void insertUserFail() throws UserAlreadyRegisteredException, DataAccessException {
        //test users
        User nullUser = new User(null, null, null,
                null, null, null, null);
        User incompleteUser = new User("someUsername", null, "spam@gmail.com",
                null, "HogRider", null, null);
        assertThrows(DataAccessException.class, () -> uDao.insertUser(null));
        assertThrows(DataAccessException.class, () -> uDao.insertUser(nullUser));
        assertThrows(DataAccessException.class, () -> uDao.insertUser(incompleteUser));

        //exact same event given
        uDao.insertUser(testUser);
        assertThrows(UserAlreadyRegisteredException.class, () -> uDao.insertUser(testUser));
    }

    @Test
    public void clearUsersPass() throws UserAlreadyRegisteredException, DataAccessException {
        //clear the person table
        uDao.insertUser(testUser);
        uDao.clearUsers();

        //try to access something (should throw DataAccessException)
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void clearUsersPassTwo() throws DataAccessException {
        //clear without data in it
        uDao.clearUsers();
        //try to access something (should throw DataAccessException)
        aDao.addAuthToken(userToken);
        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(testUser.getUsername()));
    }

}