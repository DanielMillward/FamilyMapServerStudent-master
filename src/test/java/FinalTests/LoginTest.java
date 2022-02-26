package FinalTests;

import DAOs.AuthTokenDao;
import DAOs.UserDao;
import Models.User;
import MyExceptions.DataAccessException;
import DAOs.Database;
import MyExceptions.UserAlreadyRegisteredException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public void setUp() throws DataAccessException, UserAlreadyRegisteredException {
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

        // make URL and open an HTTP connection
        URL url = new URL(baseUrlString + "/user/login");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        // Specify that we are sending an HTTP POST request in JSON
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setDoOutput(true); // will have a response body
        http.addRequestProperty("Accept", "application/json"); // want a JSON response
        // Add an auth token to the request in the HTTP "Authorization" header
        //http.addRequestProperty("Authorization", "afj232hj2332");

        //add the login request to the output stream of the httpurlconnection
        String jsonInputString = "{\"username\": \""+ bestUser.getUsername() +
                "\", \"password\": \""+ bestUser.getPassword() + "\"}";
        try(OutputStream os = http.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Connect to the server and send the HTTP request
        http.connect();

        // make sure we get an OK response
        assertEquals(http.getResponseCode(), HttpURLConnection.HTTP_OK);
        InputStream respBody = http.getInputStream();
        String respData = readString(respBody); // read the response body
        System.out.println(respData); // print it out
        //assert start with everything before the authToken
        String startJSON = "{\"username\":\"ElonMuskratty\",\"authtoken\":";
        String endJSON = ",\"personID\":\"elon1234\",\"success\":true}";
        assertTrue(respData.startsWith(startJSON));
        //asert end with everything after the authToken
        assertTrue(respData.endsWith(endJSON));
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
