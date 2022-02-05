package DAOs;

import Models.AuthToken;

import java.sql.SQLException;

/**
 * Provides access to authentication token related queries to the SQLite database.
 */
public class AuthTokenDao {

    /**
     * Searches the SQLite database for a row in the AuthToken table corresponding to a given authentication token.
     *
     * @param authToken The authentication token of the current user.
     * @return AuthToken object of the user with the given authToken.
     * @throws SQLException If unable to find a corresponding row to the given authToken.
     */
    AuthToken getAuthToken(String authToken) throws SQLException {
        return null;
    }

    /**
     * Adds an authentication token to the AuthToken table given a username and authenticaton token.
     *
     * @param username Username data to add to the table.
     * @param authToken Authentication Token id for the row in the table.
     * @throws SQLException If error in adding the AuthToken
     */
    void addAuthToken(String username, String authToken) throws SQLException {

    }
}
