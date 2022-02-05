package RequestResult;

import Models.Event;

/**
 * Stores the data for an HTTP result with all the event data for the user.
 */
public class AllEventResult {
    /**
     * Array of all the events
     */
    private Event[] data;
    /**
     * Whether the data access was successful
     */
    private boolean success;
    /**
     * Message relating to the success/failure of the data access
     */
    private String message;

    /**
     * Constructor for a successful AllEventResult request
     *
     * @param data all the event data for the user
     * @param success whether it was successful
     */
    public AllEventResult(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Constructor for an unsuccessful AllEventResult request
     *
     * @param success whether it was successful
     * @param message explanation of why the data access probably wasn't successful
     */
    public AllEventResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
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
