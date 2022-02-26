package Services;

import DAOs.Database;
import DAOs.PersonDao;
import Models.Person;
import MyExceptions.DataAccessException;
import RequestResult.AllPersonRequest;
import RequestResult.AllPersonResult;
import RequestResult.PersonRequest;
import RequestResult.PersonResult;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *  Handles the retrieval of Person objects for the Handler classes
 */
public class PersonService {

    /**
     * Retrives the data of one person request
     *
     * @param r information about the request
     * @return a PersonResult object with data of the request (if successful)
     */
    public PersonResult person(PersonRequest r) throws DataAccessException {
        //make sure both values aren't null, if so throw an error result
        if (r.getPersonID() == null || r.getAuthToken() == null) {
            return new PersonResult(false, "Error: AuthToken or PersonID is null");
        }
        //make persondao, get it
        Database db = new Database();
        boolean success = false;
        try {
            PersonDao pDao = new PersonDao(db.getConnection());
            ArrayList<Person> result = pDao.getPersons(r.getAuthToken(), r.getPersonID());
            Person person = result.get(0);
            //TODO: Deal with no result
            System.out.println(person);
            success = true;
            return new PersonResult(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(),
                    person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        } finally {
            try {
                db.closeConnection(success);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the data of all the Person objects requested
     *
     * @param r Information on the user for whom we're retrieving all the person objects
     * @return an AllPersonResult object with data of all the person requests (if successful)
     */
    public AllPersonResult AllPerson(AllPersonRequest r) throws DataAccessException {
        //make sure both values aren't null, if so throw an error result
        if (r.getAuthToken() == null) {
            return new AllPersonResult(false, "Error: AuthToken is null");
        }
        Database db = new Database();
        boolean success = false;
        try {
            //make persondao, get it
            System.out.println("Trying to get lots of people...");
            Connection conn = db.getConnection();
            PersonDao pDao = new PersonDao(conn);
            ArrayList<Person> result = pDao.getPersons(r.getAuthToken(), null);
            System.out.println("Person ID is " + result.get(0).getPersonID());
            success = true;
            return new AllPersonResult(result, true);
        } catch (DataAccessException e) {
            System.out.println("Had an error");
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        } finally {
            try {
                db.closeConnection(success);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
