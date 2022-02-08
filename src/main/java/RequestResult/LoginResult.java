package RequestResult;

/**
 * Stores the data for an HTTP request for the result of a login request
 */
public class LoginResult {

    /**
     * username of the user that tried to login
     */
    private String username;
    /**
     * authentication token of the user that tried to login
     */
    private String authtoken;
    /**
     * personID of the person object of the user that logged in
     */
    private String personID;
    /**
     * whether the login was successful
     */
    private boolean success;
    /**
     * info on whether the login was successful
     */
    private String message;

    /**
     *  Generates a LoginResult object of a successful login
     *
     * @param username username of the user that tried to login
     * @param authtoken  authentication token of the user that tried to login
     * @param personID personID of the person object of the user that logged in
     * @param success whether the login was successful
     */
    public LoginResult(String username, String authtoken, String personID,
                          Boolean success) {
        this.username = username;
        this.authtoken = authtoken;
        this.personID = personID;
        this.success = success;

    }

    /**
     * Generates a LoginResult object of an unsuccessful login
     *
     * @param message info on whether the login was successful
     * @param success whether the login was successful
     */
    public LoginResult(String message, Boolean success){
        this.message = message;
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
