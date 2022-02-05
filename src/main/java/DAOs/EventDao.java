package DAOs;

import Models.Event;
import MyExceptions.InvalidAuthTokenException;

import java.sql.SQLException;

/**
 * Provides access to Event-object related queries to the SQLite database.
 */
public class EventDao {

    /**
     *
     * Searches the SQLite database for a row in the Event table corresponding to a given eventID.
     * @param eventID id of the event being searched for
     * @param authToken Current user's authentication token
     * @return the given event corresponding to the eventID, if exists
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't correspond to the user associated with the person or doesn't exist in the database.
     */
    Event getEvent(String eventID, String authToken) throws InvalidAuthTokenException, SQLException {
        return null;
    }

    /**
     * Gets all the events associated with a given user.
     * @param authToken Current user's authentication token
     * @return array of Events corresponding to the user
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    Event[] getEvents(String authToken) throws InvalidAuthTokenException, SQLException {
        return null;
    }


}
