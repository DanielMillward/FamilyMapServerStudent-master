package Handlers;

import Logger.MyLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

public class PersonHandler extends ParentHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MyLogger.log(Level.INFO, "Recieved Person Request");
        try {
            parentHandle(exchange, "get", ReqResType.PERSON);
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}