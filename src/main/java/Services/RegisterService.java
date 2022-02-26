package Services;

import DAOs.Database;
import DAOs.PersonDao;
import DAOs.UserDao;
import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.UserAlreadyRegisteredException;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

import javax.xml.crypto.Data;
import java.sql.Connection;

/**
 * Handles the registration of a user given a RegisterRequest
 */
public class RegisterService {


    /**
     * Attempts to register the user given credentials in a RegisterRequest and returns the result
     *
     * @param r Information about the user to register in the form of a RegisterRequest
     * @return The result of the operation in the form of a RegisterResult
     */
    public RegisterResult register(RegisterRequest r) throws UserAlreadyRegisteredException, DataAccessException {
        //make user from registerRequest by getting new person id from treegen

        TreeGeneration treeGen = new TreeGeneration();
        String newUserPersonID = treeGen.getNewPersonID(r.getFirstName(), r.getLastName());
        User newUser = new User(r.getUsername(), r.getPassword(), r.getEmail(),
                r.getFirstName(), r.getLastName(), r.getGender(),newUserPersonID);
        boolean commit = false;
        //sends off registerRequest data to userDao to insertUser
        Database db = new Database();
        Connection dbConnection = db.getConnection();

        //****
        //IMPORTANT NOTE:
        //This class, the RegisterService class, is the only one of any functions it calls
        // to actually open/close a connection!!
        // all the others are just helping themselves to just the connection given
        //****


        try {
            // Open database connection & make DAOs
            System.out.println("registering user...");
            UserDao uDao = new UserDao(dbConnection);
            System.out.println("made udao...");
            //insert new User
            User userInDB = uDao.insertUser(newUser);
            System.out.println("inserted user...");
            // Close database connection, COMMIT transaction
            System.out.println("should have got a good user...");
            db.closeConnection(true);
        } catch (UserAlreadyRegisteredException ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            throw new UserAlreadyRegisteredException();
        }
        commit = false;
        // if all good, treeGen and authtoken the user
        Connection newConnection = db.getConnection();
        try {
            // Open database connection & make DAOs
            //UserDao uDao = new UserDao(newConnection);

            treeGen.generateTree(r.getGender(),4, r.getUsername(), r.getFirstName(),
                    r.getLastName(), 2000, newUserPersonID, newConnection);

            //add them to authtoken db
            db.closeConnection(true);
            LoginService loggerInner = new LoginService();
            LoginResult lr = loggerInner.login(new LoginRequest(r.getUsername(), r.getPassword()));


            commit = true;
            return new RegisterResult(lr.getUsername(), lr.getAuthtoken(), lr.getPersonID(), true);

        } catch (Exception ex) { //issue adding stuff or logging them in
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction
            return new RegisterResult("Error: " + ex.getMessage(), false);
        }

    }
}
