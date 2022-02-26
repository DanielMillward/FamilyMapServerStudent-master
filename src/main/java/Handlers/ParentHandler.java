package Handlers;

import DAOs.Database;
import Logger.MyLogger;
import MyExceptions.DataAccessException;
import MyExceptions.InvalidInputException;
import MyExceptions.UserAlreadyRegisteredException;
import RequestResult.*;
import Services.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.logging.Level;

public class ParentHandler {


    void parentHandle(HttpExchange exchange, String reqMethod, ReqResType myType) throws IOException{
        boolean success = false;

        if (exchange.getRequestMethod().toLowerCase().equals(reqMethod.toLowerCase())) {
            //get request data in JSON
            Headers reqHeaders = exchange.getRequestHeaders(); // Get header
            InputStream reqBody = exchange.getRequestBody(); // Get input stream
            String parameters =  exchange.getRequestURI().toString();
            String reqData = readString(reqBody); // turn input stream into string
            Gson gson = new Gson();
            Object result = null; //setup result


            try {
                //send off to the respective service. Note that DataAccessExceptions are dealt with in the service
                switch (myType) {
                    case LOGIN:
                        LoginRequest loginrequest = (LoginRequest) gson.fromJson(reqData, LoginRequest.class);
                        LoginService loginservice = new LoginService();
                        result = loginservice.login(loginrequest);
                        MyLogger.log(Level.INFO, "Logged in user " + loginrequest.getUsername());
                        break;
                    case REGISTER:
                        RegisterRequest registerrequest = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);
                        RegisterService registerservice = new RegisterService();
                        result = registerservice.register(registerrequest);
                        MyLogger.log(Level.INFO, "Registered user " + registerrequest.getUsername());
                        break;
                    case CLEAR:
                        ClearService clearservice = new ClearService();
                        result = clearservice.clear();
                        MyLogger.log(Level.INFO, "Cleared database");
                        break;
                    case FILL:
                        FillRequest fillRequest = getFillRequestFromParams(parameters);
                        FillService fillservice = new FillService();
                        result = fillservice.fill(fillRequest);
                        MyLogger.log(Level.INFO, "Filled database for user " + fillRequest.getUsername());
                        break;
                    case LOAD:
                        LoadRequest loadRequest = (LoadRequest) gson.fromJson(reqData, LoadRequest.class);
                        LoadService loadService = new LoadService();
                        result = loadService.load(loadRequest);
                        MyLogger.log(Level.INFO, "Loaded data into database successfully");
                        break;
                    case PERSON:
                        String reqPersonID = getIDFromParameter(parameters);
                        PersonService personService = new PersonService();
                        if (reqPersonID == null) {
                            AllPersonRequest personRequest = getAllPersonRequestFromParams(reqHeaders);
                            result = personService.AllPerson(personRequest);
                            MyLogger.log(Level.INFO, "Retrieved all Person objects for user with authToken " + personRequest.getAuthToken());
                        } else {
                            PersonRequest singlePersonReq = getPersonRequestFromParams(reqHeaders, reqPersonID);
                            result = personService.person(singlePersonReq);
                            MyLogger.log(Level.INFO, "Retrieved Person object with ID "+singlePersonReq.getPersonID()+" for user with authToken " + singlePersonReq.getAuthToken());
                        }
                        break;
                    case EVENT:
                        String reqEventID = getIDFromParameter(parameters);
                        EventService eventService = new EventService();
                        if (reqEventID == null) {
                            AllEventRequest eventRequest = getAllEventRequestFromParams(reqHeaders);
                            result = eventService.AllEvent(eventRequest);
                            MyLogger.log(Level.INFO, "Retrieved all Event objects for user with authToken " + eventRequest.getAuthToken());
                        } else {
                            EventRequest singleEventReq = getEventRequestFromParams(reqHeaders, reqEventID);
                            result = eventService.event(singleEventReq);
                            MyLogger.log(Level.INFO, "Retrieved Person object with ID "+singleEventReq.getEventID()+" for user with authToken " + singleEventReq.getAuthToken());
                        }
                        break;
                }
            } catch (UserAlreadyRegisteredException uare) {
                MyLogger.log(Level.INFO, "Failed to register user already in database: " + uare.getMessage());
                sendBadResponse("Error: User already registered", exchange, gson);
                return;
            } catch (DataAccessException dae) {
                MyLogger.log(Level.INFO, "Got DataAccessError: " + dae.getMessage());
                sendBadResponse("Error: " + dae.getMessage(), exchange, gson);
                return;
            } catch (InvalidInputException e) {
                sendBadResponse("Error: Invalid credentials", exchange, gson);
                MyLogger.log(Level.INFO, "Failed to fulfill request due to incorrect input: " + e.getMessage());
                return;
            }
            //send our response with data from the above switch cases
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Writer resBody= new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(result, resBody);
            resBody.close();

            success = true;
        }

        // The HTTP request was invalid somehow, so we return a "bad request" response
        if (!success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            MyLogger.log(Level.WARNING, "Got a bad HTTP request: " + exchange.getResponseBody());
        }
    }

    private void sendBadResponse(String message, HttpExchange exchange, Gson gson) {
        try {
            Object result = new ErrorResult(message, false);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            Writer resBody= new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(result, resBody);
            resBody.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    private EventRequest getEventRequestFromParams(Headers reqHeaders, String reqEventID) {
        //Authorization
        String authToken = null;
        for (Map.Entry<String, List<String>> header : reqHeaders.entrySet()) {
            if (Objects.equals(header.getKey(), "Authorization")) {
                authToken = header.getValue().get(0);
            }
        }
        if (authToken != null) {
            return new EventRequest(reqEventID, authToken);
        } else {
            return null;
        }
    }

    private AllEventRequest getAllEventRequestFromParams(Headers reqHeaders) {
        //Authorization
        String authToken = null;
        for (Map.Entry<String, List<String>> header : reqHeaders.entrySet()) {
            if (Objects.equals(header.getKey(), "Authorization")) {
                authToken = header.getValue().get(0);
            }
        }
        if (authToken != null) {
            return new AllEventRequest(authToken);
        }
        return null;
    }

    private PersonRequest getPersonRequestFromParams(Headers requestHeaders, String personID) {
        //Authorization
        String authToken = null;
        for (Map.Entry<String, List<String>> header : requestHeaders.entrySet()) {
            if (Objects.equals(header.getKey(), "Authorization")) {
                authToken = header.getValue().get(0);
            }
        }
        if (authToken != null) {
            return new PersonRequest(personID, authToken);
        } else {
            return null;
        }
    }

    private AllPersonRequest getAllPersonRequestFromParams(Headers requestHeaders) {
        //Authorization
        String authToken = null;
        for (Map.Entry<String, List<String>> header : requestHeaders.entrySet()) {
            if (Objects.equals(header.getKey(), "Authorization")) {
                authToken = header.getValue().get(0);
            }
        }
        if (authToken != null) {
            return new AllPersonRequest(authToken);
        }
        return null;
    }

    private String getIDFromParameter(String parameters) {
        String[] params = parameters.split("/");
        // handles a string of fill/username/4 as well as just fill/username
        if (params.length == 3) {
            return params[2];
        } else {
            return null;
        }
    }


    private FillRequest getFillRequestFromParams(String parameters) {
        String[] params = parameters.split("/");
        // handles a string of fill/username/4 as well as just fill/username
        if (params.length == 4) {
            //it has a generation fill parameter
            return new FillRequest(params[2], Integer.parseInt(params[3]));
        } else if (params.length == 3) {
            //doesn't have generation fill parameter
            return new FillRequest(params[2], -1);
        } else {
            //got too few or too many parameters
            return new FillRequest(null, -1);
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public enum ReqResType {
        ALLEVENT,
        ALLPERSON,
        CLEAR,
        EVENT,
        FILL,
        LOAD,
        LOGIN,
        PERSON,
        REGISTER
    }
}
