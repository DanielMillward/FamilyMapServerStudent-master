package DAOs.HelperClasses;

import MyExceptions.DataAccessException;
import MyExceptions.InvalidAuthTokenException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthFromUsername {

    public String getUsernameFromAuthToken(String authToken, Connection conn) throws DataAccessException {
        ResultSet rs = null;
        String usersql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
        String currUser = new String();

        try (PreparedStatement stmt = conn.prepareStatement(usersql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            //found a username authtoken row
            if (rs.next()) {
                currUser = rs.getString("username");
            } else {
                throw new InvalidAuthTokenException();
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error: problem encountered when inserting into database");
        } catch (InvalidAuthTokenException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error: Incorrect Auth Token");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        if (currUser == null) {
            throw new DataAccessException("No user found for given authToken");
        }
        return currUser;
    }

}
