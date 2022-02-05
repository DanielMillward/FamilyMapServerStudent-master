package DAOs;

import Models.User;
import MyExceptions.InvalidInputException;

import java.sql.SQLException;

public class UserDao {
    //Model objects are used to represent the data stored in
    // a single table row and are used as parameters and return values of the DAO methods.

    //other classes basically ask, I would like to see/change this User. can you do that for me?
    // see: https://www.oracle.com/java/technologies/data-access-object.html

    User getUser(String username, String password) throws InvalidInputException, SQLException {
        return null;
    }
    User createUser(String username, String password, String email, String firstName,
                    String lastName, String gender) throws InvalidInputException, SQLException {
        return null;
    }

    boolean fillUser(String username, int numGens) throws InvalidInputException, SQLException {
        return false;
    }
}

