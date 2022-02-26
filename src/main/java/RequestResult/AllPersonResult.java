package RequestResult;

import Models.Person;

import java.util.ArrayList;

/**
 * Stores the data for an HTTP result with all the Person data for the user.
 */
public class AllPersonResult {
    /**
     * Array of all the Person data
     */
    private ArrayList<Person> data;
    /**
     * Whether the data access was successful
     */
    private boolean success;
    /**
     * Message relating to the success/failure of the data access
     */
    private String message;

    /**
     * Constructor for a successful AllPersonResult request
     *
     * @param data all the person data for the user
     * @param success whether it was successful
     */
    public AllPersonResult(ArrayList<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Constructor for an unsuccessful AllPersonResult request
     *
     * @param success whether it was successful
     * @param message explanation of why the data access probably wasn't successful
     */
    public AllPersonResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    public boolean getSuccess() {
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
