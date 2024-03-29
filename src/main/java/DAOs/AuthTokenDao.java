package DAOs;

import Logger.MyLogger;
import Models.AuthToken;
import Models.Event;
import MyExceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Provides access to authentication token related queries to the SQLite database.
 */
public class AuthTokenDao {
    /**
     * Connection to the database
     */
    private final Connection conn;
    /**
     * @param conn Generates an event DAO object with a connection to the database
     */
    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * Searches the SQLite database for a row in the AuthToken table corresponding to a given authentication token.
     *
     * @param authToken The authentication token of the current user.
     * @return AuthToken object of the user with the given authToken.
     * @throws DataAccessException If unable to find a corresponding row to the given authToken.
     */
    public AuthToken getAuthToken(String authToken) throws DataAccessException {
        AuthToken output;
        ResultSet rs = null;
        String sql = "SELECT * FROM Authtoken WHERE authtoken = ?;";
        //Make sure we have the authtoken
        if (authToken == null || authToken.equals("")) {
            throw new DataAccessException("No authToken given");
        }
        //search authtoken table for a row with matching authtoken
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            //If we found something, return AuthToken object otherwise DataAccessexception
            if (rs.next()) {
                output = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return output;
            } else {
                throw new DataAccessException("No AuthToken object found for given authtoken string");
            }
        } catch (SQLException e) {
            //had an error searching for it
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            //close the connection
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //didn't find it?
    }

    /**
     * Adds an authentication token to the AuthToken table given a username and authenticaton token.
     *
     * @param authtoken Authtoken object with username and authtoken info
     * @throws SQLException If error in adding the AuthToken
     */
    public void addAuthToken(AuthToken authtoken) throws DataAccessException {
        String sql = "INSERT INTO Authtoken (authtoken, username) VALUES(?,?)";
        //Making sure we have all data
        if (authtoken.getUsername() == null || authtoken.getAuthtoken() == null) {
            throw new DataAccessException("No username or authtoken given");
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //replace question marks with given data
            stmt.setString(1, authtoken.getAuthtoken());
            stmt.setString(2, authtoken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            MyLogger.log(Level.INFO, "Authtoken error: " + e.getMessage());
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
}
