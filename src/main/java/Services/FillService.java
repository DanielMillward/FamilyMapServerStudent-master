package Services;

import DAOs.Database;
import DAOs.PersonDao;
import DAOs.UserDao;
import Models.User;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidInputException;
import RequestResult.*;

import java.sql.Connection;

/**
 * Handles the Filling of data given a FilLRequest
 */
public class FillService {

    /**
     * Fills the data for a user given a FillRequest and returns the result
     *
     * @param r FillRequest with the information on who/what to fill data for
     * @return The result of the operation in the form of a FillResult
     */
    public FillResult fill(FillRequest r) throws InvalidInputException, DataAccessException {
        /*
        if username is null, send a failed fillresult
         */
        if (r.getUsername() == null) {
            throw new InvalidInputException("Username and/or number of generations not given for fill request");
        }
        if (r.getNumGens() < 0) {
            throw new InvalidInputException("Number of generations given is less than 0");
        }

        /*
        if numgens is negative, then do default of 4 gens, else do whatever it is
         */
        int numGens = 0;
        if (r.getNumGens() < 0) {
            numGens = 4;
        } else {
            numGens = r.getNumGens();
        }
        /*
        Fill it up!
         */
        Database db = new Database();
        boolean commit = false;
        try {
            // Open database connection & make DAOs
            Connection dbConnection = db.getConnection();
            UserDao uDao = new UserDao(dbConnection);
            PersonDao pDao = new PersonDao(dbConnection);
            TreeGeneration treeGen = new TreeGeneration();
            System.out.println("Fill for " + r.getUsername());
            //get the User object of the user who we're filling for
            User user = uDao.getUserByUsername(r.getUsername());
            treeGen.generateTree(user.getGender(),numGens, user.getUsername(), user.getFirstName(),
                    user.getLastName(), 2000, user.getPersonID(), dbConnection);

            // Close database connection, COMMIT transaction
            commit = true;
            int numPersonsAdded = getNumPersonsByGen(numGens);
            int numEventsAdded = getNumEventsByGen(numGens);
            System.out.println("numGens is " + numGens + " numpeople is " + numPersonsAdded + " num events is " + numEventsAdded);
            return new FillResult("Successfully added " + numPersonsAdded + " persons and " + numEventsAdded + " events to the database.", true);

        } catch (DataAccessException dae) {
            throw new DataAccessException("Error accessing requested data");
        }
        catch (Exception ex) { //issue adding stuff or logging them in
            ex.printStackTrace();
            // Close database connection, ROLLBACK transaction

            return new FillResult("Error: " + ex.getMessage(), false);
        } finally {
            try {
                db.closeConnection(commit);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private int getNumEventsByGen(int numGens) {
        //adds one birth event for everyone, including user
        int output = getNumPersonsByGen(numGens);
        //getnumpersons, subtract 1 for rest
        int allExceptUser = output - 1;
        //add marriage (half of allExceptUser)
        output += allExceptUser / 2;
        //add death
        output += allExceptUser;
        return output;

    }

    private int getNumPersonsByGen(int numGens) {
        if (numGens == 0) {
            return 1;
        }
        int output = 0;
        for (int i = 0; i <= numGens; ++i) {
            output += Math.pow(2,i);
        }
        return output;
    }
}
