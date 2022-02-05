package RequestResult;

/**
 * Stores the data for an HTTP request to make the family tree of a given user
 */
public class FillRequest {
    /**
     * username of the user who we want to fill the family tree of
     */
    private String username;
    /**
     * number of generations we want to fill up with data
     */
    private int numGens;

    /**
     * Creates a FillRequest object
     *
     * @param username username of the user who we want to fill the family tree of
     * @param numGens number of generations we want to fill up with data
     */
    public FillRequest(String username, int numGens) {
        this.username = username;
        this.numGens = numGens;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumGens() {
        return numGens;
    }

    public void setNumGens(int numGens) {
        this.numGens = numGens;
    }
}
