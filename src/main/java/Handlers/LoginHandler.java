package Handlers;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import Services.LoginService;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class LoginHandler implements HttpHandler {

    // The "exchange" parameter == HttpExchange object, an HTTP request/response pair
    // The HttpExchange object also lets handler make response and send it back

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("got a login request!!!");
        System.out.println(exchange.getRequestBody());
        boolean success = false;
        //rewrite comments from here
        try {
            // Make sure request method is POST
            System.out.println(exchange.getRequestMethod());
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // Get header
                Headers reqHeaders = exchange.getRequestHeaders();
                // Get input stream
                InputStream reqBody = exchange.getRequestBody();
                // turn input stream into JSON
                String reqData = readString(reqBody);
                // Display/log the request JSON data
                System.out.println(reqData);

                //turn request JSON into a RegisterRequest object
                Gson gson = new Gson();
                LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);
                //send off loginRequest to the service
                LoginService service = new LoginService();
                LoginResult result = service.login(request);
                System.out.println(result);
                //Supposedly everything went well, so send a code 200
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // make a writer so we can make the response body
                Writer resBody= new OutputStreamWriter(exchange.getResponseBody());
                //TODO: actually send stuff to the resBody?
                //send json result off to the writer which writes our HTTP response
                gson.toJson(result, resBody);
                //say we're done
                resBody.close();

                success = true;
            } else if (exchange.getRequestMethod().toLowerCase().equals("options")) {
                //Do something?
            }
            if (!success) { // The HTTP request was invalid somehow, so we return a "bad request"
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // close response to say we're done
                System.out.println("Bad request...");
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // there was a server error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // close response to say we're done
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /*
        The readString method shows how to read a String from an InputStream.
        From example code
    */
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
}
