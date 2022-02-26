package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

import Logger.MyLogger;

public class ClearHandler extends ParentHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MyLogger.log(Level.INFO, "Recieved Clear Database Request");
        try {
            parentHandle(exchange, "post", ReqResType.CLEAR);
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}