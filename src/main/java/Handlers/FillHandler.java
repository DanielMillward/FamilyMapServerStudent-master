package Handlers;

import Logger.MyLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

public class FillHandler extends ParentHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MyLogger.log(Level.INFO, "Recieved Fill Request");
        try {
            parentHandle(exchange, "post", ReqResType.FILL);
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
