package Services;

import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.UserDao;
import Models.AuthToken;
import Models.User;
import MyExceptions.DataAccessException;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;

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
    public LoginResult login(LoginRequest r){
        Database db= new Database();
        try {
            // Checks if request has nonnull username and password
            if (r.getUsername() == null || r.getPassword() == null) {
                throw new DataAccessException("Username or Password is null");
            }
            // Open database connection & make DAOs
            db.openConnection();
            UserDao userdata = new UserDao(db.getConnection());
            AuthTokenDao authdata = new AuthTokenDao(db.getConnection());

            // Sends request to DAO to try and get the relevant user
            User currUser = userdata.getUser(r.getUsername(), r.getPassword());
            // make sure user was not null
            if (currUser == null) {
                throw new DataAccessException("User not found with given credentials");
            }
            // User exists, so then have the authtoken DAO make a new row
            String newAuthToken = currUser.getUsername() + System.currentTimeMillis();
            authdata.addAuthToken(new AuthToken(newAuthToken, currUser.getUsername()));

            // Close database connection, COMMIT transaction
            db.closeConnection(true);

            // Create and return SUCCESS Result object
            LoginResult result= new LoginResult(currUser.getUsername(), newAuthToken,
                    currUser.getPersonID(),true);
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            // make and send back a failed login result object
            String errorMessage = "Error: " + ex.getMessage();
            LoginResult result = new LoginResult(errorMessage, false);
            return result;
        }
    }
}
