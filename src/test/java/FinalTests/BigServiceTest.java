package FinalTests;

import DAOs.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidInputException;
import MyExceptions.UserAlreadyRegisteredException;
import RequestResult.*;
import Services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BigServiceTest {
    private Database db;

    ClearService cService;
    EventService eService;
    FillService fService;
    LoadService loadService;
    LoginService loginService;
    PersonService pService;
    RegisterService rService;

    LoginRequest firstLoginRequest;
    LoginRequest secondLoginRequest;

    RegisterRequest firstRegisterRequest;
    RegisterRequest secondRegisterRequest;

    PersonRequest personRequest;
    AllPersonRequest allPersonRequest;
    EventRequest eventRequest;
    AllEventRequest allEventRequest;
    ClearRequest clearRequest;
    FillRequest fillRequest;
    LoadRequest loadRequest;
    LoadRequest loadEventsRequest;
    LoadRequest loadPersonsRequest;
    LoadRequest loadUsersRequest;

    User firstUser;
    User secondUser;

    Person firstPerson;
    Person secondPerson;

    Event firstEvent;
    Event secondEvent;


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        //test Users
        firstUser = new User("ElonMusk", "admin", "elon@spacex.com",
                "Elon", "Musk", "m", "elon1234");
        secondUser = new User("XAE12", "securePassword123", "itsAme@mario.com",
                "XAE12", "Musk", "m", "someKid66");

        //test persons
        firstPerson = new Person("Person_num_1", "ElonMusk", "Elon",
                "Musk", "m", "dad_12", "mom_34", "spouse_78");
        secondPerson = new Person("Person_num_2", "ElonMusk", "Amelia",
                "Airheart", "f", "air_dad1", "air_mom2", "spouse_66");

        //test events
        firstEvent = new Event("Biking_123A", "ElonMusk", "Person_num_1",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Birth_Somewhere_23", "ElonMusk", "Person_num_2",
                35.9f, 140.1f, "Japan", "Ushiku",
                "birth", 1999);

        //test token
        //firstToken = new AuthToken("MyAuthToken", "ElonMusk");

        //Requests
        firstRegisterRequest = new RegisterRequest("ElonMusk", "admin", "elon@spacex.com",
                "Elon", "Musk", "m");
        secondRegisterRequest = new RegisterRequest("XAE12", "securePassword123", "itsAme@mario.com",
                "XAE12", "Musk", "m");

        firstLoginRequest = new LoginRequest("ElonMusk", "admin");
        secondLoginRequest = new LoginRequest("XAE12", "securePassword123");

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(firstUser);
        userArrayList.add(secondUser);
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(firstPerson);
        personArrayList.add(secondPerson);
        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(firstEvent);
        eventArrayList.add(secondEvent);
        loadRequest = new LoadRequest(userArrayList,personArrayList,eventArrayList);
        loadEventsRequest = new LoadRequest(null, null, eventArrayList);
        loadPersonsRequest = new LoadRequest(null, personArrayList, null);
        loadUsersRequest = new LoadRequest(userArrayList, null, null);

        //Connect to fresh database
        cService = new ClearService();
        cService.clear();

        //make the rest of the services
        eService = new EventService();
        fService = new FillService();
        loadService = new LoadService();
        loginService = new LoginService();
        pService = new PersonService();
        rService = new RegisterService();

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
    }

    @Test
    public void clearPass() throws DataAccessException, UserAlreadyRegisteredException, InvalidInputException {
        rService.register(firstRegisterRequest);

        LoginResult lResult =  loginService.login(firstLoginRequest);
        cService.clear();
        //try to login (should fail)
        assertThrows(DataAccessException.class, () -> loginService.login(firstLoginRequest));
        assertThrows(DataAccessException.class, () -> pService.person(new PersonRequest("Person_num_1", lResult.getAuthtoken())));
        assertThrows(DataAccessException.class, () -> eService.event(new EventRequest("Biking_123A", lResult.getAuthtoken())));
    }

    @Test
    public void clearPassTwo() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //add a user, person, event, and authtoken
        rService.register(firstRegisterRequest);
        loadService.load(loadRequest);
        //clear it and check its empty
        LoginResult lResult =  loginService.login(firstLoginRequest);
        cService.clear();
        //try to login (should fail)
        assertThrows(DataAccessException.class, () -> loginService.login(firstLoginRequest));
        assertThrows(DataAccessException.class, () -> pService.person(new PersonRequest("Person_num_1", lResult.getAuthtoken())));
        assertThrows(DataAccessException.class, () -> eService.event(new EventRequest("Biking_123A", lResult.getAuthtoken())));
    }

    @Test
    public void registerPass() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //add user, check its there and equal (copy from before)
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);

        assertTrue(lResult.isSuccess());
        assertEquals(firstRegisterRequest.getUsername(), lResult.getUsername());
        assertNotNull(lResult.getAuthtoken());
        assertNotNull(lResult.getPersonID());
    }

    @Test
    public void registerFail() throws UserAlreadyRegisteredException, DataAccessException {
        //username already in db
        rService.register(firstRegisterRequest);
        assertThrows(UserAlreadyRegisteredException.class, () -> rService.register(firstRegisterRequest));
        //missing values in registration
        RegisterRequest incompleteRegisterRequest = new RegisterRequest(null, "password", null, "firstName", null, null);
        assertThrows(DataAccessException.class, () -> rService.register(incompleteRegisterRequest));
        //bad gender value in registration
        RegisterRequest badRegisterRequest = new RegisterRequest("someUsername", "password", "someEmail", "firstName", "someLastname", "NotAGender");
        assertThrows(UserAlreadyRegisteredException.class, () -> rService.register(badRegisterRequest));
    }

    @Test
    public void eventPass() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //add two events to db
        loadService.load(loadEventsRequest);
        //login
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        //see if retrieval gets just the one
        EventResult eventResult = eService.event(new EventRequest("Biking_123A", lResult.getAuthtoken()));
        assertNotNull(eventResult);
        assertEquals(firstEvent.getEventID(), eventResult.getEventID());
        assertEquals(firstEvent.getEventType(), eventResult.getEventType());
    }

    @Test
    public void eventFail() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //no auth token and no event
        assertThrows(DataAccessException.class, () -> eService.event(new EventRequest(null, null)));

        //auth token, but no event
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        //see if retrieval gets just the one
        assertThrows(DataAccessException.class, () -> eService.event(new EventRequest("Biking_123A", lResult.getAuthtoken())));
    }

    @Test
    public void allEventPass() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //add two events to db
        loadService.load(loadEventsRequest);
        //login
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        //see if retrieval gets ball events (keeping in mind the default # of events generated is 91)
        allEventRequest = new AllEventRequest(lResult.getAuthtoken());
        ArrayList<Event> compareTest = eService.AllEvent(allEventRequest).getData();
        assertNotNull(compareTest);
        assertEquals(91 + 2, compareTest.size());
    }

    @Test
    public void allEventFail() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //no auth token and no event
        assertThrows(DataAccessException.class, () -> eService.AllEvent(new AllEventRequest("randomAuthToken")));
    }

    @Test
    public void fillPass() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //check for success message I guess
        //login
        loadService.load(loadUsersRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        fService.fill(new FillRequest(firstUser.getUsername(), 2));
        int numEvents = eService.AllEvent(new AllEventRequest(lResult.getAuthtoken())).getData().size();
        int numPersons = pService.AllPerson(new AllPersonRequest(lResult.getAuthtoken())).getData().size();
        assertEquals(19, numEvents);
        assertEquals(7, numPersons);

    }

    @Test
    public void fillFail() throws DataAccessException {
        //invalid username
        loadService.load(loadUsersRequest);
        assertThrows(DataAccessException.class, () -> fService.fill(new FillRequest("IncorrectUsername", 2)));


        //invalid gens parameter (negative)
        assertThrows(InvalidInputException.class, () -> fService.fill(new FillRequest("ElonMusk", -9)));
    }

    @Test
    public void loadPass() throws InvalidInputException, DataAccessException {
        //check for success message
        loadService.load(loadRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        assertNotNull(lResult);
        LoginResult lResultTwo = loginService.login(secondLoginRequest);
        assertNotNull(lResult);
        AllEventResult allEventResult = eService.AllEvent(new AllEventRequest(lResult.getAuthtoken()));
        assertNotNull(allEventResult);
        AllPersonResult allPersonRequest = pService.AllPerson(new AllPersonRequest(lResult.getAuthtoken()));
        assertNotNull(allPersonRequest);
    }

    @Test
    public void loadFail() {
        //missing/incomplete values
        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(firstEvent);
        Event incompleteEvent = new Event(null, null, "something", 0,0,"Canada", "Toronto", "birth", 2019);
        eventArrayList.add(incompleteEvent);

        LoadRequest badLoadRequest = new LoadRequest(null, null, eventArrayList);
        assertThrows(DataAccessException.class,() -> loadService.load(badLoadRequest));
    }

    @Test
    public void loginPass() throws DataAccessException, InvalidInputException {
        loadService.load(loadRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);

        assertTrue(lResult.isSuccess());
        assertEquals(firstUser.getUsername(), lResult.getUsername());
        assertEquals(firstUser.getPersonID(), lResult.getPersonID());

    }

    @Test
    public void loginFail() throws DataAccessException{
        //no password
        loadService.load(loadRequest);
        LoginRequest noPasswordRequest = new LoginRequest("ElonMusk", null);
        assertThrows(DataAccessException.class, () -> loginService.login(noPasswordRequest));
        //wrong password
        loadService.load(loadRequest);
        LoginRequest wrongPasswordRequest = new LoginRequest("ElonMusk", "TryingAPassword");
        assertThrows(DataAccessException.class, () -> loginService.login(wrongPasswordRequest));
    }

    @Test
    public void personPass() throws DataAccessException, UserAlreadyRegisteredException, InvalidInputException {
        //add two events to db
        loadService.load(loadPersonsRequest);
        //login
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        //see if retrieval gets just the one
        PersonResult personResult = pService.person(new PersonRequest(firstPerson.getPersonID(), lResult.getAuthtoken()));
        assertNotNull(personResult);
        assertEquals(firstPerson.getPersonID(), personResult.getPersonID());
        assertEquals(firstPerson.getFirstName(), personResult.getFirstName());
        assertEquals(firstPerson.getLastName(), personResult.getLastName());
    }

    @Test
    public void personFail() throws UserAlreadyRegisteredException, DataAccessException, InvalidInputException {
        //no auth token and no event
        assertThrows(InvalidInputException.class, () -> pService.person(new PersonRequest(null, null)));

        //auth token, but no event
        rService.register(firstRegisterRequest);
        LoginResult lResult =  loginService.login(firstLoginRequest);
        //see if retrieval gets just the one
        assertThrows(DataAccessException.class, () -> pService.person(new PersonRequest(firstPerson.getPersonID(), lResult.getAuthtoken())));
    }

    @Test
    public void allPersonPass() {
        //add 2 people for user

    }

    @Test
    public void allPersonFail() {
        //invalid auth token
        //invalid
    }

}
