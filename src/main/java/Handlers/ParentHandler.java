package Handlers;

import MyExceptions.DataAccessException;
import RequestResult.*;
import Services.LoadService;
import Services.LoginService;
import Services.RegisterService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class ParentHandler {


    void parentHandle(HttpExchange exchange, String reqMethod, ReqResType myType) throws IOException{
        boolean success = false;

        if (exchange.getRequestMethod().toLowerCase().equals(reqMethod.toLowerCase())) {
            //get request data in JSON
            Headers reqHeaders = exchange.getRequestHeaders(); // Get header
            InputStream reqBody = exchange.getRequestBody(); // Get input stream
            String reqData = readString(reqBody); // turn input stream into string
            Gson gson = new Gson();
            Object result = null; //setup result

            //send off to the respective service. Note that DataAccessExceptions are dealt with in the service
            switch (myType) {
                case LOGIN:
                    LoginRequest loginrequest = (LoginRequest) gson.fromJson(reqData, LoginRequest.class);
                    LoginService loginservice = new LoginService();
                    result = loginservice.login(loginrequest);
                    break;
                case REGISTER:
                    RegisterRequest registerrequest = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);
                    RegisterService registerservice = new RegisterService();
                    result = registerservice.register(registerrequest);
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
            System.out.println("Bad request...");
            exchange.getResponseBody().close();
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
