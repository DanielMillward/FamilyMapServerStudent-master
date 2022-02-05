package Models;

/**
 * Authentication token of a user. Corresponds to a row in the AuthToken database.
 */
public class AuthToken {

    /**
     * Authentication token of the given user
     */
    private String authtoken;
    /**
     * username of the user with the corresponding authentication token
     */
    private String username;

    /**
     * Generates an AuthToken object given an authtoken and username
     *
     * @param authtoken Authentication token of the given user
     * @param username username of the user with the corresponding authentication token
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
