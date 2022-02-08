package Services;

import RequestResult.AllEventRequest;
import RequestResult.AllEventResult;
import RequestResult.EventRequest;
import RequestResult.EventResult;

/**
 * Handles the retrieval of event objects for the Handler classes
 */
public class EventService {

    /**
     * Retrives the data of one event request
     *
     * @param r information about the request
     * @return an EventResult object with data of the request (if successful)
     */
    EventResult event(EventRequest r) {
        return null;
    }

    /**
     * Retrieves the data of all the events for an AllEventRequest
     *
     * @param r information about the request
     * @return an AllEventResult object with data of all the event requests (if successful)
     */
    AllEventResult AllEvent(AllEventRequest r) {return null;}
}
