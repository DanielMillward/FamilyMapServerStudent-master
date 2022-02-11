package Models;

/**
 * Event of a person Object for a user. Corresponds to a row in the Event table
 */
public class Event {
    /**
     * unique ID for the given event
     */
    private String eventID;
    /**
     * name of the user whose person the events relates to corresponds to
     */
    private String associatedUsername;
    /**
     * ID for the person the event relates to
     */
    private String personID;
    /**
     * Latitude coordinate of the event
     */
    private float latitude;
    /**
     * Longitude coordinate of the event
     */
    private float longitude;
    /**
     * Country location of the event
     */
    private String country;
    /**
     * City location of the event
     */
    private String city;
    /**
     * Type of event (e.g. birth, death, marriage)
     */
    private String eventType;
    /**
     * Year the event took place
     */
    private int year;

    /**
     * Generate an Event object for a given person and user
     *
     * @param eventID  unique ID for the given event
     * @param associatedUsername name of the user whose person the events relates to corresponds to
     * @param personID ID for the person the event relates to
     * @param latitude Latitude coordinate of the event
     * @param longitutde Longitude coordinate of the event
     * @param country Country location of the event
     * @param city City location of the event
     * @param eventType Type of event (e.g. birth, death, marriage)
     * @param year Year the event took place
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitutde, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitutde;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        } else {
            return false;
        }
    }
}
