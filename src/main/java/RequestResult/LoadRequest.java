package RequestResult;

/**
 * Stores data for an HTTP request to load data
 */
public class LoadRequest {
    /**
     * the users we want to load data for
     */
    private String users;
    /**
     * person objects we want to add to the database
     */
    private String persons;
    /**
     * event objects we want to add to the database
     */
    private String events;


    /**
     * Generates a LoadRequest object
     *
     * @param users  the users we want to load data for
     * @param persons person objects we want to add to the database
     * @param events event objects we want to add to the database
     */
    public LoadRequest(String users, String persons, String events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }


    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }
}
