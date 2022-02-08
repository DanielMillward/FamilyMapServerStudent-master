package RequestResult;

/**
 * Data for an HTTP result that tells whether a register attempt was successful
 */
public class RegisterResult {
    /**
     * Unique username for user
     */
    private String username;
    /**
     * Authentication for the user for this session
     */
    private String authtoken;
    /**
     *
     *
     * Unique Person ID assigned to this user’s generated Person
     */
    private String personID;
    /**
     * Whether the registration was successful
     */
    private boolean success;
    /**
     * Information on whether the registration was successful
     */
    private String message;

    /**
     * Generates a RegisterResult object for a successful registration
     *
     * @param username Unique username for user
     * @param authtoken Authentication for the user for this session
     * @param personID Unique Person ID assigned to this user’s generated Person
     * @param success Whether the registration was successful
     */
    public RegisterResult(String username, String authtoken, String personID,
                          Boolean success) {
        this.username = username;
        this.authtoken = authtoken;
        this.personID = personID;
        this.success = success;
    }

    /**
     * Generates a RegisterResult object for a non-successful registration
     *
     * @param message Information on whether the registration was successful
     * @param success Whether the registration was successful
     */
    public RegisterResult(String message, Boolean success){
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
