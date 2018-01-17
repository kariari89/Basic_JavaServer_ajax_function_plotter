import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import java.util.ArrayList;

/**
 * 
 * HTTPReader class used to read and hold HTTP requests.
 * 
 * For each request, the server/thread spawns a HTTPReader and passes the
 * incoming stream to it (wrapped in a BufferedReader). The HTTPReader stores
 * the whole request, separating the different parts (Request Line, Headers and
 * Message Body (if available)). While separating, it performs some checks to
 * ensure the request is not invalid. Note that the checks don't guarantee all
 * the headers are valid.
 * 
 * Once the whole request is read, the only modifications to the state of this
 * object should be done internally. As such, set methods should never be 
 * implemented in this class. This insures integrity and allows users to
 * believe this object holds the true request.
 * 
 * @author Arian Chitgar
 */

public class HTTPReader {
    
    boolean badRequest = false;
    
    String requestLine;
    Method method;
    URI    uri;
    String path;
    String query;
    
    ArrayList<String> headers = new ArrayList<String>();
    
    char[]  messageBody;

    /**
     * A HTTPReader can only exist if there's a HTTP request to be read.
     * 
     * The whole reading is done in the constructor to avoid complications and
     * dangers from passing around the buffer and having to check the current
     * position in the stream.
     * 
     * If at some point the request is deemed to be bad, the reader sets an
     * internal flag (badRequest) and stops.
     * 
     * @param br The BufferedReader wrapping the InputStream containing the
     *           request of the client. br is guaranteed to be ready.
     * @throws IOException The various reads done by the BufferedReader can
     *         throw an IOException. Should one occur, the Worker thread will
     *         catch it and terminate itself.
     */
    public HTTPReader(BufferedReader br) throws IOException {
        requestLine = br.readLine();
        
        /*
         * Check if request is empty.
         */
        if (requestLine == null) {
            throw new EOFException("Connection closed by client");
        }
        
        /*
         * Process  the request line. Any error would mean a badRequest.
         */
        try {
            processRequestLine();
        }
        catch(BadRequestException bre) {
            badRequest = true;
            return;
        }
        
        /*
         * Read the headers.
         * 
         * NOTE : no guard against an overflow of headers. Supplying an
         * infinite loop of headers will crash (hopefully only) the thread.
         */
        String line;
        while(true) {
            line = br.readLine();
            if (line.isEmpty())
                break;
            else {
                headers.add(line);
            }
        }
        
        /*
         * Scan the headers for various info.
         * 
         * Current headers of interest are :
         *   Content-Length: to see if there is an attached message,
         */
        boolean foundContentLength = false;
        for (String field : headers) {
            if (field.matches("Content-Length:.*")) {
                int idx = field.indexOf(":");
                int contentLength = Integer.parseInt(field.substring(idx+1).trim());
                messageBody = new char[contentLength];
                br.read(messageBody, 0, contentLength);
                foundContentLength = true;
            }

           
        }
        

    }
    
    /**
     * This method processes a freshly read Request Line.
     * 
     * It checks the Method and URI parts and extracts them.
     * 
     * If at any point something goes wrong, it throws a BadRequestException
     * and flags the request as bad.
     * 
     */
    private void processRequestLine() {
        if (requestLine.isEmpty()) {
            throw new BadRequestException("The Request Line is empty.");
        }
        
        String rlCopy = requestLine;
        // Method get and check
        int idx;
        idx = rlCopy.indexOf(" ");
        String requestMethod = rlCopy.substring(0, idx);
        if (requestMethod.equals("GET")){
            method = Method.GET;}
        else if (requestMethod.equals("POST"))
            method = Method.POST;
        else {
            method = Method.OTHER;
            throw new BadRequestException("Unsupported method.");
        
        }
        
        // URI get
        try {
            rlCopy = rlCopy.substring(idx + 1).trim();
        } catch (IndexOutOfBoundsException ioobe) {
            throw new BadRequestException("Request Line misformat.");
        }
        idx = rlCopy.indexOf(" ");
        try {
            uri = new URI(rlCopy.substring(0, idx));
        }
        catch(URISyntaxException urise) {
            throw new BadRequestException("Bad URI syntax.");
        }
        path = uri.getPath();
        query = uri.getQuery();
        
        
    }
    
    
    /*
     * Various getters to obtain information about the request.
     */

    public boolean badRequest() {
        return badRequest;
    }
    
    public Method getMethod() {
        return this.method;
    }
      
    public URI getURI() {
        return uri;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getQuery() {
        return query;
    }
}