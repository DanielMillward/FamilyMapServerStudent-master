package Models;

/**
 * Person Object for a family member of a user. Corresponds to a row in the Person table.
 */
public class Person {
    /**
     * unique ID for a given person
     */
    private String personID;
    /**
     * name of the user whose person the events relates to corresponds to
     */
    private String associatedUsername;
    /**
     * First name of the person
     */
    private String firstName;
    /**
     * Last name of the person
     */
    private String lastName;
    /**
     * Gender of the person
     */
    private String gender;
    /**
     * ID of the father person object (optional, can be null)
     */
    private String fatherID;
    /**
     *Person ID of person’s mother
     */
    private String motherID;
    /**
     * Person ID of person’s spouse
     */
    private String spouseID;

    /**
     * Generates a Person object for a user's ancestor/relative
     *
     * @param personID Unique identifier for this person
     * @param associatedUsername Username of user to which this person belongs
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender
     * @param fatherID Person ID of person’s father
     * @param motherID Person ID of person’s mother
     * @param spouseID Person ID of person’s spouse
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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
}
