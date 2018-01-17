import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Hashtable;
/**
 * Worker.java
 * 
 * Worker class holding the actual logic of the server that must be run
 * by each thread.
 * 
 * @author Arian Chitgar
 */
public class Worker implements Runnable {
    
    Socket s;
    Cache cache;
    
    OutputStream out;
    InputStream in;
    BufferedReader br;
    
    /**
     * A Worker needs a socket granted by the server to operate.
     */
    Worker(Socket s, Cache cache){
    	this.s = s;
    }
    
    /**
     * Implementation of the run() method as required by the Runnable interface.
     * Contains all the worker logic. The workflow is a loop of the following
     * sequence of operations :
     *   - wait for data to come in on the InputStream,
     *   - read, store and process using a HTTPReader object,
     *   - perform any requested supported actions such as logging in and out,
     *     cookie setting and message posting,
     *   - generate and send the response with the help of a HTTPWriter object.
     * The loop breaks once the socket is closed by the client and the thread
     * should normally terminate.
     */
    @Override
    public void run() {
        try {
            out = s.getOutputStream();
            in  = s.getInputStream();

            while(true) {

                br = new BufferedReader(new InputStreamReader(in));
                
                HTTPReader r;
                try {
                    r = new HTTPReader(br);
                } 
                catch(EOFException eofe) {
                    /*
                     * The client closed the connection. Terminate thread.
                     */
                    break;
                }
                catch(IOException ioe) {
                    /*
                     * An error occurred during reading. Terminate thread.
                     */
                    break;
                }
                
                HTTPWriter w = new HTTPWriter(r);
                w.generateResponse();
                ArrayList<String> response = new ArrayList<String>();
                response = w.getResponse();
                
                for (String string : response) {
                    out.write(string.getBytes());
                }

                out.flush();

            }
            
            
        }
        catch(IOException ioe) {
            System.err.println("An error occured. Terminating thread.");
        }
        finally {
            closeAll();
        }
        
    }
    
    private void closeAll() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (br != null) br.close();
            if (s != null) s.close();
        }
        catch (IOException ioe) {
            /* 
             * In the rare event this exception does get thrown,
             * the program doesn't need to be terminated because
             * the exception is not threatening since the thread is terminating.
             * However, repeated offenses should be addressed by the
             * administrator since they represent an underlying problem.
             */
            ioe.printStackTrace();
        } 
        finally {
            out = null;
            in = null;
            br = null;
            s = null;
        }
    }
}