package DAOs;

import Models.Person;
import MyExceptions.InvalidAuthTokenException;

import java.sql.SQLException;

/**
 * Provides access to Person-object related queries to the SQLite database.
 */
public class PersonDao {


    /**
     *
     * Searches the SQLite database for a row in the Person table corresponding to a given personID.
     * @param personID id of ther person being searched for
     * @param authToken Current user's authentication token
     * @return the given person corresponding to the personID, if exists
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't correspond to the user associated with the person or doesn't exist in the database.
     */
    Person getPerson(String personID, String authToken) throws  SQLException, InvalidAuthTokenException {
        return null;
    }

    /**
     * Gets all the people associated with a given user.
     * @param authToken Current user's authentication token
     * @return array of Persons corresponding to the user
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    Person[] getPersons(String authToken) throws SQLException, InvalidAuthTokenException {
        return null;
    }
}
