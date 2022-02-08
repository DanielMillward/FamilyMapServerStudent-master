package RequestResult;

/**
 * Stores the data for an HTTP result for information on a specific Person object
 */
public class PersonResult {
    /**
     * Stores the data for an HTTP request
     */
    private String associatedUsername;
    /**
     *Stores the data for an HTTP request
     */
    private String personID;
    /**
     *Stores the data for an HTTP request
     */
    private String firstName;
    /**
     *Stores the data for an HTTP request
     */
    private String lastName;
    /**
     *Stores the data for an HTTP request
     */
    private String gender;
    /**
     *Stores the data for an HTTP request
     */
    private String fatherID;
    /**
     *Stores the data for an HTTP request
     */
    private String motherID;
    /**
     *Stores the data for an HTTP request
     */
    private String spouseID;
    /**
     *whether the login was successful
     */
    private boolean success;
    /**
     * info on whether the login was successful
     */
    private String message;

    /**
     * Generates a PersonResult if the search was successful
     *
     * @param associatedUsername whether the login was successful
     * @param personID whether the login was successful
     * @param firstName whether the login was successful
     * @param lastName whether the login was successful
     * @param gender whether the login was successful
     * @param fatherID  Person ID of person’s father
     * @param motherID Person ID of person’s mother
     * @param success whether the login was successful
     */
    public PersonResult(String associatedUsername, String personID, String firstName, String lastName,
                        String gender, String fatherID, String motherID, boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.success = success;
    }

    /**
     * Generates a PersonResult if the search was not successful
     *
     * @param success whether the login was successful
     * @param message info on whether the login was successful
     */
    public PersonResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
