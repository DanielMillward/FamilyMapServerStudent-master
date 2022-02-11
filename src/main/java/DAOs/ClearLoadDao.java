package DAOs;

import Models.Event;
import Models.Person;
import Models.User;
import MyExceptions.InvalidInputException;

import java.sql.SQLException;

/**
 * Provides access to database-wide queries such as clearing and loading
 */
public class ClearLoadDao {
    /**
     * Clears all entries in the entire database.
     * @throws SQLException if encounters an error in adding it to the table
     */
    void clearEverything() throws SQLException {

    }

    /**
     * Loads information into the database.
     *
     * @param users Array of users to add to the database
     * @param persons Array of persons corresponding to the users in the first parameter
     * @param events Array of events coresponding to the persons given
     * @throws InvalidInputException if missing/have invalid users, persons, or events.
     * @throws SQLException if encounters an error in adding it to the table
     */
    void load(User[] users, Person[] persons, Event[] events) throws InvalidInputException, SQLException {

    }
}
