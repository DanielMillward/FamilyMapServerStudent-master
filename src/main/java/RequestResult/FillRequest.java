package RequestResult;

public class FillRequest {
    private String username;
    private int numGens;

    public FillRequest(String username, int numGens) {
        this.username = username;
        this.numGens = numGens;
    }

    public FillRequest(String username) {
        this.username = username;
        numGens = 4;
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
