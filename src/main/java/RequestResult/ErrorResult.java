package RequestResult;

public class ErrorResult {
    private String message;

    private boolean success;
    public ErrorResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
