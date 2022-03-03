package DAOs;

import DAOs.HelperClasses.AuthFromUsername;
import Models.Person;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidAuthTokenException;

import java.sql.*;
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
        //Catching null values
        if (person == null) {
            throw new DataAccessException("Person is null");
        }
        if (person.getPersonID() == null || person.getAssociatedUsername() == null ||person.getFirstName() == null ||person.getLastName() == null ||person.getGender() == null ) {
            throw new DataAccessException("Incomplete data given");
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Replacing question marks with data
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
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Gets either all or just one of the people associated with a given user.
     * If want all persons and there's none, returns empty arraylist.
     * If want one person and there's none, returns null.
     * @param authToken Current user's authentication token
     * @param personID ID of requested person. If not null, returns just that person.
     * @return array of Persons corresponding to the user if personID is null, else just array of one.
     * @throws SQLException if encounters an error in adding it to the table
     * @throws InvalidAuthTokenException if the authentication token doesn't exist in the database.
     */
    public ArrayList<Person> getPersons(String authToken, String personID) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        //Getting username from authToken row
        AuthFromUsername afu = new AuthFromUsername();
        String currUser = afu.getUsernameFromAuthToken(authToken,conn);
        //Case 1: Looking for specific Person
        if (personID != null && authToken != null) {
            persons = getPersonsWithUsername(currUser, personID, true);
            return persons;
        //Case 2: Want all the persons
        } else if (authToken != null) {
            persons = getPersonsWithUsername(currUser, personID, false);
            return persons;
        } else {
            throw new DataAccessException("No authtoken given");
        }
    }

    private ArrayList<Person> getPersonsWithUsername(String currUser, String personID, boolean isSingle) throws DataAccessException {
        ResultSet rs = null;
        ArrayList<Person> output = new ArrayList<>();
        String sql;
        //Setting SQL statement based on whether want just one or many people
        if (isSingle) {
            sql = "SELECT * FROM Person WHERE associatedUsername = ? AND personID = ?;";
        } else {
            sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, currUser);
            if (isSingle) {
                stmt.setString(2, personID);
            }
            rs = stmt.executeQuery();
            //Just inserting one Person if given PersonID
            if (isSingle) {
                if (rs.next()) {
                    output.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID")));
                    return output;
                } else {
                    throw new DataAccessException("No person found for given PersonID and AuthToken");
                }
            //Inserting all Persons
            } else {
                boolean foundSomething = false;

                while (rs.next()) {
                    output.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID")));
                    foundSomething = true;
                }
                if (foundSomething) {
                    return output;
                } else {
                    throw new DataAccessException("No people found for given authToken username");
                }
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } catch (DataAccessException e) {
            //e.printStackTrace();
            throw new DataAccessException(e.getMessage());
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

    public void clearPersons() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    public void setSpouseID(String personID, String spouseID) throws DataAccessException {
        String sql = "UPDATE Person SET spouseID=? WHERE personID=?;";
        if (personID == null || spouseID == null) {
            throw new DataAccessException("Incomplete data given to set SpouseID");
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Replace question marks with data
            stmt.setString(1, spouseID);
            stmt.setString(2, personID);

            stmt.executeUpdate();
        } catch (SQLException e) {
           // e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
}
