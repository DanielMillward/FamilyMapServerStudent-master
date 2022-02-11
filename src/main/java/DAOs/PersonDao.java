package DAOs;

import Models.Person;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidAuthTokenException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides access to Person-object related queries to the SQLite database.
 */
public class PersonDao {
    /**
     * Connection to the database
     */
    private final Connection conn;
    /**
     * @param conn Generates an event DAO object with a connection to the database
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Gets either all or just one of the people associated with a given user.
     * @param authToken Current user's authentication token
     * @param personID ID of requested person. If not null, returns just that person.
     * @return array of Persons corresponding to the user if personID is null, else just array of one.
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    Person[] getPersons(String authToken,String personID) throws SQLException, InvalidAuthTokenException, DataAccessException {
        return null;
    }
}
