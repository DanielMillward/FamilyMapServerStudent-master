package DAOs;

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
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        if (event == null || event.getEventID() == null || event.getAssociatedUsername() == null || event.getPersonID() == null ||event.getCountry() == null ||event.getCity() == null ||event.getEventType() == null ) {
            throw new DataAccessException("Null or Incomplete data given");
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
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
        ResultSet rs = null;
        if (eventID != null) {

            String usersql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
            String sql = "SELECT * FROM Events WHERE eventID = ? AND associatedUsername = ?;";
            String currUser = new String();
            // 1. Get the corresponding username for the authtoken
            try (PreparedStatement stmt = conn.prepareStatement(usersql)) {
                stmt.setString(1, authToken);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    currUser = rs.getString("username");
                } else {
                    throw new DataAccessException("No user found for this authToken");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered when trying to find a user for the authtoken");
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, eventID);
                stmt.setString(2,currUser);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    events.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                            rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                            rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                            rs.getInt("year")));
                    return events;
                } else {
                    throw new DataAccessException("No event found for this eventID and authToken username");
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
        } else if (authToken != null) {
            
            String usersql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
            String sql = "SELECT * FROM Events WHERE associatedUsername = ?;";
            String currUser = new String();
            // 1. Get the corresponding username for the authtoken
            try (PreparedStatement stmt = conn.prepareStatement(usersql)) {
                stmt.setString(1, authToken);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    currUser = rs.getString("username");
                }else {
                    throw new DataAccessException("No user found for this authToken");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered when trying to find a user for the authtoken");
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            //2. Get all the events for the user
            rs = null;
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, currUser);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    events.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                            rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                            rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                            rs.getInt("year")));
                }
                if (!events.isEmpty()) {
                    return events;
                } else {
                    throw new DataAccessException("No events found for this authToken username");
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
        return null;
    }


}
