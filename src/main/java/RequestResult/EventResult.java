package RequestResult;

/**
 * Stores the data for an HTTP result with all the event data for the requested single event.
 */
public class EventResult {
    /**
     *Username of user to which this event belongs
     */
    private String associatedUsername;
    /**
     *Unique identifier for this event
     */
    private String eventID;
    /**
     *ID of person to which this event belongs
     */
    private String personID;
    /**
     *
     * Country in which event occurred
     */
    private String country;
    /**
     *City in which event occurred
     */
    private String city;
    /**
     *Type of event
     */
    private String eventType;
    /**
     *Latitude of event’s location
     */
    private float latitude;
    /**
     *Longitude of event’s location
     */
    private float longitude;
    /**
     *Year in which event occurred
     */
    private int year;
    /**
     * Whether the data access was successful
     */
    private boolean success;
    /**
     * Message relating to the success/failure of the data access
     */
    private String message;

    /**
     * Generate an EventResult object for a given retrieved event
     *
     * @param eventID  unique ID for the given event
     * @param associatedUsername name of the user whose person the events relates to corresponds to
     * @param personID ID for the person the event relates to
     * @param latitude Latitude coordinate of the event
     * @param longitude Longitude coordinate of the event
     * @param country Country location of the event
     * @param city City location of the event
     * @param eventType Type of event (e.g. birth, death, marriage)
     * @param year Year the event took place
     */
    public EventResult(String associatedUsername, String eventID, String personID, String country, String city, String eventType, float latitude, float longitude, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
        this.success = success;
    }

    public EventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
