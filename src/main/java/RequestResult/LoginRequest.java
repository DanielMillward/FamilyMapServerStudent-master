package RequestResult;

/**
 * Stores the data for an HTTP request to login
 */
public class LoginRequest {
    /**
     * username of the user trying to login
     */
    private String username;
    /**
     * password of the user trying to login
     */
    private String password;


    /**
     * Generates a LoginRequest object
     *
     * @param username username of the user trying to login
     * @param password password of the user trying to login
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
