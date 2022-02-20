package Handlers;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

/*
	 When the server gets a  "/user/register" URL path, it calls ListGamesHandler.handle()
*/
public class RegisterHandler implements HttpHandler {


    // The "exchange" parameter == HttpExchange object, an HTTP request/response pair
    // The HttpExchange object has info like request type (get/post), headers, body, etc.
    // The HttpExchange object also lets handler make response and send it back
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // The game list returned as JSON in body. Things done:
        // 1. How to get the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to return the desired status code (200, 404, etc.) in an HTTP response
        // 4. How to write JSON data to the HTTP response body
        // 5. How to check an incoming HTTP request for an auth token

        // To actually do in handler class:
        // 1. Get HTTP request type, make sure its type correct, if not return
        // 2. convert everything into the right request object type
        // 3. call the function of the respective service and get its result object back
        // 4. decode object into json
        // 5. depending on result, return desired status code and json data body.

        //To do in server classes:
        // 1. Make sure that all the parameters are present, if not, return a fail result object
        // 2. make sure all parameters are within tolerance? if not, return a fail result object
        // 3. run given request on server using DAO object
        // 4. depending on null-ness, Convert it into the desired type of result object

        boolean success = false;

        try {
            //TODO: Change this into post

            // Determine the HTTP request type, only allowing GET since it's read only here??
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");
                    // Verify that the auth token is the one we're looking for
                    // (this is not realistic, because clients will use different
                    // auth tokens over time, not the same one all the time).
                    if (authToken.equals("afj232hj2332")) {

                        // This is the JSON data we will return in the HTTP response body
                        // (this is unrealistic because it always returns the same answer).
                        String respData =
                                "{ \"game-list\": [" +
                                        "{ \"name\": \"fhe game\", \"player-count\": 3 }," +
                                        "{ \"name\": \"work game\", \"player-count\": 4 }," +
                                        "{ \"name\": \"church game\", \"player-count\": 2 }" +
                                        "]" +
                                        "}";

                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        // Now that the status code and headers have been sent to the client,
                        // next we send the JSON data in the HTTP response body.

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();

                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);

                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete.
                        respBody.close();

                        success = true;
                    }
                }
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Since the client request was invalid, they will not receive the
                // list of games, so we close the response body output stream,
                // indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
