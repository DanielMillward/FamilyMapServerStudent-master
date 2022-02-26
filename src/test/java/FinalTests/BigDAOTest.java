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
                "Musk", "f", "dad_12", "mom_34","spouse_78");

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
    public void getEventsPass() {

    }

    @Test
    public void getEventsFail() {

    }

    @Test
    public void personInsertPass() {

    }

    @Test
    public void personInsertFail() {

    }

    @Test
    public void getPersonsPass() {

    }

    @Test
    public void getPersonsFail() {

    }

    @Test
    public void clearPersonsPass() {

    }

    @Test
    public void clearPersonsFail() {

    }

    @Test
    public void setSpouseIDPass() {

    }

    @Test
    public void setSpouseIDFail() {

    }

    @Test
    public void getUserPass() {

    }

    @Test
    public void getUserFail() {

    }

    @Test
    public void getUserByUsernamePass() {

    }

    @Test
    public void getUserByUsernameFail() {

    }

    @Test
    public void insertUserPass() {

    }

    @Test
    public void insertUserFail() {

    }

    @Test
    public void clearUsersPass() {

    }

    @Test
    public void clearUsersFail() {

    }

}