package Services;

import DAOs.Database;
import DAOs.EventDao;
import DAOs.PersonDao;
import Models.Event;
import Models.Person;
import MyExceptions.DataAccessException;
import RequestResult.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Handles the retrieval of event objects for the Handler classes
 */
public class EventService {

    /**
     * Retrives the data of one event request
     *
     * @param r information about the request
     * @return an EventResult object with data of the request (if successful)
     */
    public EventResult event(EventRequest r) throws DataAccessException {
        //make sure both values aren't null, if so throw an error result
        if (r.getEventID() == null || r.getAuthToken() == null) {
            throw new DataAccessException("Incomplete Event Requests details given");
        }
        //make persondao, get it
        Database db = new Database();
        boolean success = false;
        try {
            EventDao eDao = new EventDao(db.getConnection());
            ArrayList<Event> result = eDao.getEvents(r.getAuthToken(), r.getEventID());
            Event event = result.get(0);
            System.out.println(event);
            success = true;
            return new EventResult(event.getAssociatedUsername(), event.getEventID(), event.getPersonID(),
                                   event.getCountry(), event.getCity(), event.getEventType(), event.getLatitude(),
                                   event.getLongitude(), event.getYear(), true);
        } catch (DataAccessException e) {
            //e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        } finally {
            try {
                db.closeConnection(success);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the data of all the events for an AllEventRequest
     *
     * @param r information about the request
     * @return an AllEventResult object with data of all the event requests (if successful)
     */
    public AllEventResult AllEvent(AllEventRequest r) throws DataAccessException {
        //make sure both values aren't null, if so throw an error result
        if (r.getAuthToken() == null) {
            return new AllEventResult(false, "Error: Auth token null");
        }
        Database db = new Database();
        boolean success = false;
        try {
            //make eventdao, get it
            System.out.println("Trying to get lots of events...");
            Connection conn = db.getConnection();
            EventDao eDao = new EventDao(conn);
            ArrayList<Event> result = eDao.getEvents(r.getAuthToken(), null);
            System.out.println("Event ID is " + result.get(0).getPersonID());
            success = true;
            return new AllEventResult(result, true);
        } catch (DataAccessException e) {
            System.out.println("Had an error");
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        } finally {
            try {
                db.closeConnection(success);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
