import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer.java
 * 
 * Main class for the socket-based HTTP WebServer.
 *
 * @author Arian Chitgar
 */
public class WebServer {
    /**
     * Main method for running the program. It handles the launch arguments,
     * declaration and initialization of some server-wide variables (containers)
     * and the spawning of threads for each new socket, backed by a thread pool.
     *   
     * @param args The args array that should the maxThreads parameter
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.err.println(
             "Usage: java WebServer maxThreads \n Example: java WebServer 50");
            return;
        }*/
        
    	int argis = 10;
        int maxThreads;
        try {
            //maxThreads = Integer.parseInt(args[0]);
        	maxThreads = argis;
        } catch (NumberFormatException nfe) {
            System.err.println(
             "The number of maximum threads must be a valid positive integer");
            return;
        }
        
        ServerSocket ss;
        try {
            ss = new ServerSocket(8020);
        } catch (IOException ioe) {
            System.err.println(
             "An error occured while trying to initialize the server socket");
            ioe.printStackTrace();
            return;
        }
        Cache cache = new Cache();
        
        ExecutorService executor;
        try {
            executor = Executors.newFixedThreadPool(maxThreads);
        } catch(IllegalArgumentException iae) {
            System.err.println(
             "The number of maximum threads must be positive");
            return;
        }
        
        while(true) {
            Socket ts;
            try {
                ts = ss.accept();
            } catch (IOException ioe) {
                continue;
            }
            executor.execute(new Worker(ts,cache));
            
        }
    }
    
}