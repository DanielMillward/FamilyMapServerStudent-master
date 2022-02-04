package RequestResult;

public class LoginResult {
    private String username;
    private String authtoken;
    private String personID;
    private boolean success;
    private String message;

    public LoginResult(String username, String authtoken, String personID,
                          Boolean success) {
        this.username = username;
        this.authtoken = authtoken;
        this.personID = personID;
        this.success = success;
    }

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
