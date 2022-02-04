package RequestResult;

import Models.Person;

public class AllPersonResult {
    private Person[] data;
    private boolean success;

    public AllPersonResult(Person[] data, boolean success){

        this.data = data;
        this.success = success;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
