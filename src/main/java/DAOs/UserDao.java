package DAOs;

import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidInputException;
import MyExceptions.UserAlreadyRegisteredException;

import java.sql.*;

/**
 * Provides access to user related queries to the database, such as retrieving and creating users
 */
public class UserDao {
    //Model objects are used to represent the data stored in
    // a single table row and are used as parameters and return values of the DAO methods.

    //other classes basically ask, I would like to see/change this User. can you do that for me?
    // see: https://www.oracle.com/java/technologies/data-access-object.html

    /**
     * Connection to the database
     */
    private final Connection conn;
    /**
     * @param conn Generates an event DAO object with a connection to the database
     */
    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Gets the user associated with a given username and password
     *
     * @param username the username of a user
     * @param password the corresponding password of the user
     * @return User object with given username/password
     * @throws DataAccessException if it encounters an error retrieving the user.
     */
    public User getUser(String username, String password) throws DataAccessException {
        if (username == null || password == null) {
            throw new DataAccessException("Incomplete login data given");
        }

        User currUser;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                currUser = new User(rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return currUser;
            } else {
                throw new DataAccessException("No user found with given credentials");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
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

    /**
     * Gets a User object with just the username
     *
     * @param username username of user we want to get the object of
     * @return User object of requested user
     * @throws DataAccessException can't find user or had an error
     */
    public User getUserByUsername(String username) throws DataAccessException {
        User currUser;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                currUser = new User(rs.getString("username"),
                        rs.getString("password"), rs.getString("email"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return currUser;
            } else {
                throw new DataAccessException("No user found with given username");
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
    }

    /**
     * Adds a user to the database given all the information required
     *
     * @param user User object with all the data required to make a new user
     * @return the brand new User object added to the database
     * @throws DataAccessException if it encounters an error in adding the user to the database
     */
    public User insertUser(User user) throws UserAlreadyRegisteredException, DataAccessException {
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        if (user == null) {
            throw new DataAccessException("User is null");
        }
        if (user.getUsername() == null || user.getPassword() == null ||user.getEmail() == null ||user.getFirstName() == null ||user.getLastName() == null ||user.getGender() == null ||user.getPersonID() == null) {
            throw new DataAccessException("Incomplete data for user given");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new UserAlreadyRegisteredException("Error encountered when trying to add you to the database. " +
                    "Are you already registered? If so, log in.");
        }
        return null;
    }

    public void clearUsers() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}

