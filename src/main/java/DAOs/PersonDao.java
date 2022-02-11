package DAOs;

import Models.Event;
import Models.Person;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidAuthTokenException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides access to Person-object related queries to the SQLite database.
 */
public class PersonDao {
    /**
     * Connection to the database
     */
    private final Connection conn;
    /**
     * @param conn Generates an event DAO object with a connection to the database
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Adds a person into the database
     *
     * @param person person object whose data is being added
     */
    public void insert(Person person) throws DataAccessException {

        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("person SQL error " + e.getMessage());
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Gets either all or just one of the people associated with a given user.
     * @param authToken Current user's authentication token
     * @param personID ID of requested person. If not null, returns just that person.
     * @return array of Persons corresponding to the user if personID is null, else just array of one.
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    public ArrayList<Person> getPersons(String authToken, String personID) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        ResultSet rs = null;
        if (personID != null) {
            String sql = "SELECT * FROM Person WHERE personID = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, personID);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    persons.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID")));
                    return persons;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while finding event");
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (authToken != null) {

            String usersql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
            String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
            String currUser = new String();
            // 1. Get the corresponding username for the authtoken
            try (PreparedStatement stmt = conn.prepareStatement(usersql)) {
                stmt.setString(1, authToken);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    currUser = rs.getString("username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered when trying to find a user for the authtoken");
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
            //2. Get all the events for the user
            rs = null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, currUser);
                rs = stmt.executeQuery();
                //add all the rows we got to the persons arraylist
                while (rs.next()) {
                    persons.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID")));
                }
                return persons;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while finding event");
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }


}
