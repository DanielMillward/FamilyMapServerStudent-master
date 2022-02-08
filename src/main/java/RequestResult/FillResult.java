package RequestResult;

/**
 * Data for an HTTP response as for how the filing of a user's tree went
 */
public class FillResult {
    /**
     * Whether the data filling was successful
     */
    private boolean success;
    /**
     * Information on the success of the data filling
     */
    private String message;


    /**
     * Generates a FillResult object
     *
     * @param message  Information on the success of the data filling
     * @param success Whether the data filling was successful
     */
    public FillResult(String message, Boolean success){
        this.message = message;
        this.success = success;
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
