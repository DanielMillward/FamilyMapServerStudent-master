package Services;

import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.UserDao;
import Models.AuthToken;
import Models.User;
import MyExceptions.DataAccessException;
import RequestResult.ClearResult;
import RequestResult.LoginResult;

import java.util.logging.Logger;
/**
 * Handles the clearing of the database
 */
public class ClearService {

    /**
     * Clears the database and returns the result
     *
     * @return ClearResult data of whether it was successful
     */
    public ClearResult clear() {
        Database db= new Database();
        boolean commit = false;
        try {
            // Open database connection
            db.getConnection();
            //clear everything
            db.clearUserTable();
            db.clearAuthTokenTable();
            db.clearEventTable();
            db.clearPersonTable();

            commit = true;
            // Create and return SUCCESS Result object
            return new ClearResult("Clear succeeded.", true);

        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = "Error: " + ex.getMessage();
            ClearResult result = new ClearResult(errorMessage, false);
            return result;
        } finally {
            try {
                db.closeConnection(commit);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
