import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import Handlers.*;
import com.sun.net.httpserver.*;

/*
	command-line argument is the port number that accepts connections
*/
public class Server {
    // The maximum number of waiting incoming connections to queue (dont need to know)
    private static final int MAX_WAITING_CONNECTIONS = 12;

    // server initialized in run method (below)
    private HttpServer server;

    // Initialize/run the server, using a given port number
    private void run(String portNumber) {

        //about to initialize server
        System.out.println("Initializing HTTP Server");
        try {
            // Make new server given port and max waiting connections
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //using default executor (dont need to know)
        server.setExecutor(null);

        //about to make contexts, aka handlers?
        System.out.println("Creating contexts");

        // forwards requests for /user/register to RegisterHandler for handling
        server.createContext("/user/register", new RegisterHandler());

        // forwards requests for /user/login to LoginHandler for handling
        server.createContext("/user/login", new LoginHandler());

        // forwards requests for /fill/ to FillHandler for handling
        server.createContext("/fill/", new FillHandler());

        // forwards requests for /load to LoadHandler for handling
        server.createContext("/load", new LoadHandler());

        server.createContext("/clear", new ClearHandler());

        // forwards requests for /person/ to  PersonHandler for handling
        server.createContext("/person/", new PersonHandler());

        // forwards requests for /event/ to EventHandler for handling
        server.createContext("/event/", new EventHandler());

        // Create and install the "default"/"file" handler, aka the "other" handler.
        server.createContext("/", new FileHandler());

        // about to start server
        System.out.println("Starting server");

        //server starts, main method completes but program will still run since server is still running
        server.start();

        //server has successfully started.
        System.out.println("Server started");
    }

    // "args" is just one number, the port
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}