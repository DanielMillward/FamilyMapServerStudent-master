package Handlers;

import DAOs.Database;
import MyExceptions.DataAccessException;
import MyExceptions.UserAlreadyRegisteredException;
import RequestResult.*;
import Services.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Locale;

public class ParentHandler {


    void parentHandle(HttpExchange exchange, String reqMethod, ReqResType myType) throws IOException{
        boolean success = false;

        if (exchange.getRequestMethod().toLowerCase().equals(reqMethod.toLowerCase())) {
            //get request data in JSON
            Headers reqHeaders = exchange.getRequestHeaders(); // Get header
            InputStream reqBody = exchange.getRequestBody(); // Get input stream
            String parameters =  exchange.getRequestURI().toString();
            System.out.println("parameters: " + parameters); // gets everything after localhost, e.g. /fill/user
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
                        break;
                    case REGISTER:
                        System.out.println("Got a REGISTER request");
                        RegisterRequest registerrequest = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);
                        RegisterService registerservice = new RegisterService();
                        result = registerservice.register(registerrequest);
                        System.out.println("should have successfully registered");
                        System.out.println(result);
                        break;
                    case CLEAR:
                        ClearService clearservice = new ClearService();
                        result = clearservice.clear();
                        break;
                    case FILL:
                        System.out.println("got a fill request!");
                        FillRequest fillRequest = getFillRequestFromParams(parameters);
                        FillService fillservice = new FillService();
                        result = fillservice.fill(fillRequest);
                        break;
                }
            } catch (UserAlreadyRegisteredException uare) {
                uare.printStackTrace();
                result = new ErrorResult(uare.getMessage(), false);
                System.out.println("Got a user already registered error");
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                result = new ErrorResult(dae.getMessage(), false);
                System.out.println("Got a Data Access Exception");
            }
            System.out.println(result);
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
            System.out.println("Bad request...");
            exchange.getResponseBody().close();
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
