package DAOs;

import Models.Event;
import MyExceptions.InvalidAuthTokenException;

import java.sql.SQLException;

public class EventDao {

    Event getEvent(String eventID, String authToken) throws InvalidAuthTokenException, SQLException {
        return null;
    }

    Event[] getEvents(String authToken) throws InvalidAuthTokenException, SQLException {
        return null;
    }


}
