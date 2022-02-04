package RequestResult;

import Models.Event;

public class AllEventResult {
    private Event[] data;
    private boolean success;
    private String message;

    public AllEventResult(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

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
