package RequestResult;

/**
 * Stores the data of an HTTP request for a request for a single event for a given user's family.
 */
public class EventRequest {


    /**
     * Unique ID of the event requested
     */
    private String eventID;
    /**
     * Authentication token of the user attempting to access the event data.
     */
    private String authToken;


    /**
     * Constructor for the EventRequest.
     *
     * @param eventID Unique ID of the event requested
     * @param authToken Authentication token of the user attempting to access the event data.
     */
    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
