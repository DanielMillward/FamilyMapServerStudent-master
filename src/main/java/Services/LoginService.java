package Services;

import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.UserDao;
import Models.AuthToken;
import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidInputException;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;

import java.sql.Connection;

/**
 * Handles the login of a user given a LoginRequest
 */
public class LoginService {

    /*
     * Checks if request has nonnull username and password
     * Sends request to DAO to try and get the relevant user
     * If so, then have the authtoken DAO make a new row
     * Make a loginrequest result with all the stuff and send it
     * */

    /**
     * Attempts to login the user given credentials in a LoginRequest and returns the result
     *
     * @param r Information about the user to login in the form of a LoginRequest
     * @return The result of the operation in the form of a LoginResult
     */
    public LoginResult login(LoginRequest r) throws InvalidInputException, DataAccessException{
        Database db= new Database();
        boolean commit = false;
        if (r.getUsername() == null || r.getPassword() == null) {
            throw new DataAccessException("Username or Password is null");
        }
        try {
            // Checks if request has nonnull username and password

            // Open database connection & make DAOs
            Connection dbConnection = db.getConnection();
            UserDao userdata = new UserDao(dbConnection);
            AuthTokenDao authdata = new AuthTokenDao(dbConnection);

            System.out.println("Login request for user " + r.getUsername() + " and password " + r.getPassword());
            // Sends request to DAO to try and get the relevant user
            User currUser = userdata.getUser(r.getUsername(), r.getPassword());
            // make sure user was not null
            if (currUser == null) {
                System.out.println("Got an incorrect login");
                throw new InvalidInputException();
            } else {
                System.out.println("Got a successful login of user " + currUser.getUsername() + " " + currUser.getPassword());
            }
            // User exists, so then have the authtoken DAO make a new row
            String newAuthToken = currUser.getUsername() + System.currentTimeMillis();
            authdata.addAuthToken(new AuthToken(newAuthToken, currUser.getUsername()));


            // Create and return SUCCESS Result object
            LoginResult result= new LoginResult(currUser.getUsername(), newAuthToken,
                    currUser.getPersonID(),true);
            commit = true;
            return result;

        } catch (InvalidInputException e) {
            System.out.println("Rethrowing invalid input error");
            throw new InvalidInputException();
        }
        catch (DataAccessException ex) {
            ex.printStackTrace();
            // make and send back a failed login result object
            throw new DataAccessException("Failed to login: " + ex.getMessage());
        } finally {
            try {
                db.closeConnection(commit);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
