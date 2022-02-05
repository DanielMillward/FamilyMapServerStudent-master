package RequestResult;

/**
 * Stores the data of an HTTP request for a request for every event for a given user.
 */
public class AllEventRequest {
    /**
     * Authentication token of the user attempting to access the event data.
     */
    private String authToken;

    /**
     * Instantiates an AllEventRequest with the given data of an authToken.
     *
     * @param authToken authentication token of the current user.
     */
    public AllEventRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
