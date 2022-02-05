package DAOs;

import Models.User;
import MyExceptions.InvalidInputException;

import java.sql.SQLException;

/**
 * Provides access to user related queries to the database, such as retrieving and creating users
 */
public class UserDao {
    //Model objects are used to represent the data stored in
    // a single table row and are used as parameters and return values of the DAO methods.

    //other classes basically ask, I would like to see/change this User. can you do that for me?
    // see: https://www.oracle.com/java/technologies/data-access-object.html

    /**
     * Gets the user associated with a given username and password
     *
     * @param username the username of a user
     * @param password the corresponding password of the user
     * @return User object with given username/password
     * @throws InvalidInputException Missing or invalid username/password
     * @throws SQLException if encounters an error in adding it to the table
     */
    User getUser(String username, String password) throws InvalidInputException, SQLException {
        return null;
    }

    /**
     * Adds a user to the database given all the information required
     *
     * @param username username of the new user
     * @param password password of the new user
     * @param email email of the new user
     * @param firstName first name of the new user
     * @param lastName last name of the new user
     * @param gender gender of the new user
     * @return the brand new User object added to the database
     * @throws InvalidInputException Missing or invalid parameters
     * @throws SQLException if encounters an error in adding it to the table
     */
    User createUser(String username, String password, String email, String firstName,
                    String lastName, String gender) throws InvalidInputException, SQLException {
        return null;
    }

    /**
     * Creates a family tree for a given user and number of generations
     *
     * @param username username of the corresponding user
     * @param numGens number of generations to create for the user
     * @return whether the operation was successful
     * @throws InvalidInputException missing or invalid parameters
     * @throws SQLException if encounters an error in adding it to the table
     */
    boolean fillUser(String username, int numGens) throws InvalidInputException, SQLException {
        return false;
    }
}

