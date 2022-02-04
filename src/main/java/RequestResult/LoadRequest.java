package RequestResult;

public class LoadRequest {

    private String users;
    private String persons;
    private String events;

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
