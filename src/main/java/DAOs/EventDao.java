package DAOs;

import DAOs.HelperClasses.AuthFromUsername;
import Models.Event;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidAuthTokenException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides access to Event-object related queries to the SQLite database.
 */
public class EventDao {
    /**
     * Connection to the database
     */
    private final Connection conn;
    /**
     * @param conn Generates an event DAO object with a connection to the database
     */
    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert an event (w/ data from an event object) into the database
     *
     * @param event event to insert into the database
     * @throws DataAccessException If error encountered when inserting into the db
     */
    public void insert(Event event) throws DataAccessException {
        //SQL command to enter the event object into the database
        String sql = "INSERT INTO Events (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        //Make sure we have all the necessary values
        if (event == null || event.getEventID() == null || event.getAssociatedUsername() == null || event.getPersonID() == null ||event.getCountry() == null ||event.getCity() == null ||event.getEventType() == null ) {
            throw new DataAccessException("Null or Incomplete data given");
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Replacing question marks with actual data
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("yo yo " + e.getMessage());
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Gets either one or all the events of a given user.
     * @param authToken Current user's authentication token
     * @param eventID ID of requested event. If not null, only returns that event.
     * @return array of Events corresponding to the user (can be just 1)
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    public ArrayList<Event> getEvents(String authToken, String eventID) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        //Get the username from the authToken table
        AuthFromUsername afu = new AuthFromUsername();
        String currUser = afu.getUsernameFromAuthToken(authToken,conn);
        //Case 1: Looking for specific Event
        if (eventID != null && authToken != null) {
            events = getEventsWithUsername(currUser, eventID, true);
            return events;
        //Case 2: Want all the events
        } else if (authToken != null) {
            events = getEventsWithUsername(currUser, eventID, false);
            return events;
        } else {
            throw new DataAccessException("No authtoken given");
        }
    }

    private ArrayList<Event> getEventsWithUsername(String currUser, String eventID, boolean isSingle) throws DataAccessException{
        ResultSet rs = null;
        ArrayList<Event> output = new ArrayList<>();
        String sql;
        //Switch SQL statement depending on whether we want one or all Events
        if (isSingle) {
            sql = "SELECT * FROM Events WHERE associatedUsername = ? AND eventID = ?;";
        } else {
            sql = "SELECT * FROM Events WHERE associatedUsername = ?;";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Setting data
            stmt.setString(1, currUser);
            if (isSingle) {
                stmt.setString(2, eventID);
            }
            rs = stmt.executeQuery();
            //Dealing with just one event requested
            if (isSingle) {
                if (rs.next()) {
                    output.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                            rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                            rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                            rs.getInt("year")));
                    return output;
                } else {
                    throw new DataAccessException("No event found for this eventID and authToken username");
                }
            //dealing with all events requested
            } else {
                while (rs.next()) {
                    output.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                            rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                            rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                            rs.getInt("year")));
                }
                if (!output.isEmpty()) {
                    return output;
                } else {
                    throw new DataAccessException("No events found for this authToken username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
