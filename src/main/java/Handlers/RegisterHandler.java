package Handlers;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;

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
public class RegisterHandler implements HttpHandler {


    // The "exchange" parameter == HttpExchange object, an HTTP request/response pair
    // The HttpExchange object has info like request type (get/post), headers, body, etc.
    // The HttpExchange object also lets handler make response and send it back
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        //rewrite comments from here
        try {
            // Make sure request method is POST
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
                RegisterRequest request = (RegisterRequest) gson.fromJson(reqData, RegisterRequest.class);
                //send off RegisterRequest to the service
                RegisterService service = new RegisterService();
                RegisterResult result = service.register(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resBody = exchange.getResponseBody();
                //sending the JSON from the result to the output stream resbody
                gson.toJson((Object) result, (Type) resBody);
                //Say we're done responding
                resBody.close();


                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();

                success = true;
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
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