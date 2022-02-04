package RequestResult;

public class ClearResult {
    private boolean success;
    private String message;

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
