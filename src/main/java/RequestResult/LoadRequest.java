package RequestResult;

import Models.Event;
import Models.Person;
import Models.User;

import java.util.ArrayList;

/**
 * Stores data for an HTTP request to load data
 */
public class LoadRequest {
    /**
     * the users we want to load data for
     */
    private ArrayList<User> users;
    /**
     * person objects we want to add to the database
     */
    private ArrayList<Person> persons;
    /**
     * event objects we want to add to the database
     */
    private ArrayList<Event> events;

    /**
     * Generates a LoadRequest object
     *
     * @param users  the users we want to load data for
     * @param persons person objects we want to add to the database
     * @param events event objects we want to add to the database
     */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
