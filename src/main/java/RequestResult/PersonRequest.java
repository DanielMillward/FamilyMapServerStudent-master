package RequestResult;

/**
 *  Stores the data for an HTTP request to get the information on a person object
 */
public class PersonRequest {

    /**
     * the person object we want to get information on
     */
    private String personID;
    /**
     * the authentication token of the user
     */
    private String authToken;

    /**
     * Generates a PersonRequest object
     *
     * @param personID the person object we want to get information on
     * @param authToken the authentication token of the user
     */
    public PersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
