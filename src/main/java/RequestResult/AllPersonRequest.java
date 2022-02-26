package RequestResult;

/**
 * Stores the data of an HTTP request for a request for every Person related to a given user.
 */
public class AllPersonRequest {
    /**
     * Authentication token of the user attempting to access the event data.
     */
    private String authToken;


    /**
     * Instantiates an AllPersonRequest with the given data of an authToken.
     *
     * @param authToken authentication token of the current user.
     */
    public AllPersonRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
