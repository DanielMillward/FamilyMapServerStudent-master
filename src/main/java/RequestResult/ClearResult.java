package RequestResult;

/**
 * Stores the data for an HTTP result with all the information of the outcome of clearing the database.
 */
public class ClearResult {
    /**
     * Whether the data access was successful
     */
    private boolean success;
    /**
     * Message relating to the success/failure of the data access
     */
    private String message;

    /**
     * Constructor for the ClearResult data class.
     *
     * @param message explanation of whether the clearing was successful and if not why not
     * @param success whether the clearing was successful
     */
    public ClearResult(String message, Boolean success){
        this.message = message;
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
