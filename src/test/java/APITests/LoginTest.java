package APITests;

import DAOs.AuthTokenDao;
import DAOs.UserDao;
import Models.User;
import MyExceptions.DataAccessException;
import DAOs.Database;
import DAOs.EventDao;
import Models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;
    private AuthTokenDao aDao;
    private String baseUrlString;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestUser = new User("ElonMuskratty", "admin", "elon@spacex.com",
                "Elon", "Musk", "m", "elon1234");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO so it can access the database
        uDao = new UserDao(conn);
        //clear any lingering users
        uDao.clearUsers();

        baseUrlString = "http://" + "localhost" + ":" + "8080";

        //Put in a user, make sure it's all good
        uDao.insertUser(bestUser);
        User compareUser = uDao.getUser(bestUser.getUsername(), bestUser.getPassword());
        assertNotNull(compareUser);
        assertEquals(bestUser, compareUser);
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {

    }

    @Test
    public void loginPass() throws DataAccessException, IOException {

        // make URL to access login
        URL url = new URL(baseUrlString + "/user/login");

        // Start constructing our HTTP request
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        // Specify that we are sending an HTTP POST request in JSON
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; utf-8");

        // Indicate that this request will indeed contain an HTTP request body
        http.setDoOutput(true);

        // Add an auth token to the request in the HTTP "Authorization" header
        //http.addRequestProperty("Authorization", "afj232hj2332");

        // Specify that we would like to receive the server's response in JSON
        // format by putting an HTTP "Accept" header on the request (this is not
        // necessary because our server only returns JSON responses, but it
        // provides one more example of how to add a header to an HTTP request).
        http.addRequestProperty("Accept", "application/json");

        //add the login request
        String jsonInputString = "{\"username\": \""+ bestUser.getUsername() +
                "\", \"password\": \""+ bestUser.getPassword() + "\"}";
        try(OutputStream os = http.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Connect to the server and send the HTTP request
        http.connect();

        // By the time we get here, the HTTP response has been received from the server.
        // Check to make sure that the HTTP response from the server contains a 200
        // status code, which means "success".  Treat anything else as a failure.
        assertEquals(http.getResponseCode(), HttpURLConnection.HTTP_OK);

        InputStream respBody = http.getInputStream();
        String respData = readString(respBody);
        System.out.println(respData);
        // make sure the json output matches here!
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
