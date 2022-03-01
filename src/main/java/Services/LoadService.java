package Services;

import DAOs.Database;
import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import MyExceptions.DataAccessException;
import MyExceptions.UserAlreadyRegisteredException;
import RequestResult.ClearResult;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;

import java.sql.Connection;

/**
 * Handles the loading of data given a LoadRequest
 */
public class LoadService {

    /**
     * Loads the data for a user given a LoadRequest and returns the result
     *
     * @param r LoadRequest object with the information on who/what to load data for
     * @return The result of the operation in the form of a LoadResult
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {

        /*
        First we clear the database
         */
        Database db= new Database();
        boolean commit = false;
        try {
            // Open database connection
            db.getConnection();
            //clear everything
            db.clearUserTable();
            db.clearAuthTokenTable();
            db.clearEventTable();
            db.clearPersonTable();
            commit = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new LoadResult("Error: Failed to clear before loading data.", false);
        } finally {
            try {
                db.closeConnection(commit);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        /*
        Now we load the data
         */
        boolean shouldCommit = false;
        try {
            // getting the User DAO and calling insertUser enough times
            boolean success = true;
            int latest = 0;
            Connection daoConnection = db.getConnection();
            UserDao uDao = new UserDao(daoConnection);
            if (r.getUsers() != null) {
                for (int i = 0; i < r.getUsers().size(); ++i) {
                    try {
                        System.out.println("Trying to insert  "+ r.getUsers().get(i).getUsername());
                        uDao.insertUser(r.getUsers().get(i));

                        latest = i;

                    } catch (UserAlreadyRegisteredException e) {
                        e.printStackTrace();
                        System.out.println("Had a repeating username???? "+ r.getUsers().get(i).getUsername());
                        success = false;
                    }
                }
            }
            if (!success) {
                return new LoadResult("Error: Multiple users with username " + r.getUsers().get(latest).getUsername(), false);
            }

            // getting the Person DAO and calling insertPerson enough times
            PersonDao pDao = new PersonDao(daoConnection);
            if (r.getPersons() != null) {
                for (int i = 0; i < r.getPersons().size(); ++i) {
                    pDao.insert(r.getPersons().get(i));
                }
            }

            // getting the Event DAO and calling insertEvent enough times
            EventDao eDao = new EventDao(daoConnection);
            if (r.getEvents() != null) {
                for (int i = 0; i < r.getEvents().size(); ++i) {
                    eDao.insert(r.getEvents().get(i));
                }
            }

            //loading was successful!
            shouldCommit = true;
            int usersSize = 0;
            int peopleSize = 0;
            int eventsSize = 0;
            if (r.getUsers() != null) {
                usersSize = r.getUsers().size();
            }
            if (r.getPersons() != null) {
                peopleSize = r.getPersons().size();
            }
            if (r.getEvents() != null) {
                eventsSize = r.getEvents().size();
            }
            return new LoadResult("Successfully added " + usersSize + " users, " + peopleSize + " persons, and " + eventsSize + " events to the database.", true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to add the given data to the database");
        } finally {
            try {
                db.closeConnection(shouldCommit);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
