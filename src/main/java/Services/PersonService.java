package Services;

import RequestResult.AllPersonRequest;
import RequestResult.AllPersonResult;
import RequestResult.PersonRequest;
import RequestResult.PersonResult;

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
    PersonResult person(PersonRequest r) {
        return null;
    }

    /**
     * Retrieves the data of all the Person objects requested
     *
     * @param r Information on the user for whom we're retrieving all the person objects
     * @return an AllPersonResult object with data of all the person requests (if successful)
     */
    AllPersonResult AllPerson(AllPersonRequest r) {
        return null;
    }
}
