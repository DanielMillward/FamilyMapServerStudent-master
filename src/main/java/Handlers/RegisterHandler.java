package Handlers;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.logging.Level;

import Logger.MyLogger;
import MyExceptions.DataAccessException;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import Services.LoginService;
import Services.RegisterService;
import com.sun.net.httpserver.*;
import com.google.gson.*;

/*
	 When the server gets a  "/user/register" URL path, it calls ListGamesHandler.handle()
*/
public class RegisterHandler extends ParentHandler implements HttpHandler {


    // The "exchange" parameter == HttpExchange object, an HTTP request/response pair
    // The HttpExchange object has info like request type (get/post), headers, body, etc.
    // The HttpExchange object also lets handler make response and send it back
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MyLogger.log(Level.INFO, "Recieved Registration Request");
        try {
            parentHandle(exchange, "post", ReqResType.REGISTER);
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

}