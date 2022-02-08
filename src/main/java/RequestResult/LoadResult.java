package RequestResult;

/**
 *  Stores the data for an HTTP request that goes back to the user after a Load request
 */
public class LoadResult {
    /**
     * whether the request was successful
     */
    private boolean success;
    /**
     * Information on the success
     */
    private String message;

    /**
     * Generates a LoadResult object
     *
     * @param message whether the request was successful
     * @param success Information on the success
     */
    public LoadResult(String message, Boolean success){
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
