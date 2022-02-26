package Handlers;

import Logger.MyLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.logging.Level;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MyLogger.log(Level.INFO, "Recieved File Request");
        boolean success = false;
        try {
            // Make sure request method is GET
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPathTemp = exchange.getRequestURI().toString();
                String urlPath = null;
                //System.out.println("urlPathTemp is " + urlPathTemp);
                if (urlPathTemp == null || urlPathTemp.equals("/")){
                    urlPath = "/index.html";
                } else {
                    urlPath = urlPathTemp;
                }
                //System.out.println("urlPath is " + urlPath);
                //get file path
                String filePath = "web" + urlPath;
                //System.out.println("requested " + filePath);
                File requestedFile = new File(filePath);
                OutputStream respBody = exchange.getResponseBody();
                if (requestedFile.exists()) {
                    //System.out.println("File exists");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(requestedFile.toPath(), respBody);
                    respBody.close();
                } else {
                    File notFoundFile = new File("web/HTML/404.html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    Files.copy(notFoundFile.toPath(), respBody);
                    respBody.close();
                }
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // close response to say we're done
                System.out.println("Bad request...");
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            // there was a server error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // close response to say we're done
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}