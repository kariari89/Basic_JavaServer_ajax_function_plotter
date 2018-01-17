
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * HTTPWriter.java
 * 
 * Class to generate HTTP replies to received HTTP requests. This class relies
 * on the HTTPReader class to provide information about the request so as to
 * build a correct reply. It also relies on the server to set its various
 * fields as the request gets processed.
 * 
 * It stores the whole reply in an ArrayList and makes it available to get.
 * It is the server's duty to do the actual sending to the client.
 * 
 * @author Arian Chitgar
 */

public class HTTPWriter {
    boolean authenticated;
    
    int status;
    
    Method method;
    String path;
    String query;
    HTTPReader reader;
    
    int page = 1;
    int maxPosts = 10;
    
    String user;
    static Cache cache;
    
    ArrayList<String> response = new ArrayList<String>();
        
    /**
     * Initiate the HTTPWriter with data from the HTTPReader.
     * 
     * @param r The HTTPReader containing the HTTP request that is being
     *          replied to.
     */
    public HTTPWriter(HTTPReader r) {
        this.reader = r;
        this.method = r.getMethod();
        this.path   = r.getPath();
        this.query  = r.getQuery();
        getFunction();
        
    }
    

    
    
    /**
     * This method is run by the server once it is sure that the object
     * contains all the necessary information to generate a complete and
     * correct reply.
     */
    public void generateResponse() {        
        response.clear();
        
        /*
         * Initial check to weed out bad requests.
         */
        if (reader.badRequest() || method == Method.OTHER) {
            response.add("HTTP/1.1 400 Bad Request\r\n");
            response.add("Content-Type: text/plain\r\n");
            response.add("Content-Length: 32\r\n\r\n");
            response.add("Bad request : unsupported method");
            return;
        }
        
        if (path.matches("/")) {
            if (method == Method.GET) {
                    response.add("HTTP/1.1 303 See Other\r\n");
                    response.add("Location: /grapheView.html\r\n");
                    response.add("Content-Type: text/plain\r\n");
                    response.add("Content-Length: 11\r\n\r\n");
                    response.add("Redirecting");
                    return;
            }
            else {
                response.add("HTTP/1.1 405 Method Not Allowed\r\n");
                response.add("Allow: GET\r\n");
                response.add("Content-Type: text/plain\r\n");
                response.add("Content-Length: 18\r\n\r\n");
                response.add("Method not allowed");
                return;
            }
        }
        
     
        
        if (path.matches("/grapheView.(?i)html")) {
        	
            if (method == Method.GET) {
            		
               
                    response.add("HTTP/1.1 200 OK\r\n");
                    response.add("Server: WebServer\r\n");
                    
                    if(query == null){
                    	response.add("Content-Type: text/html\r\n");
                    	HTMLGenerator h = new HTMLGenerator(path);
                    	response.add("Content-Length: " + computeLength(h.getMsg()) + "\r\n\r\n");
                        response.addAll(h.getMsg());
                    }
                    else
                    {
                    	response.add("Content-Type: text\r\n");
                    	if (Cache.inCache(getFunction()))
                    	{
                    		response.add("Content-Length: " + (Cache.getInCache(getFunction())).length() + "\r\n\r\n");
                        	response.add(Cache.getInCache(getFunction()));
                    	}
                    	else
                    	{
                    	    GraphGenerator gg = new GraphGenerator(getFunction(), getCenter());
                    	    Cache.addGraphe(getFunction(), getCenter());
                    	    response.add("Content-Length: " + gg.getGraphe().length() + "\r\n\r\n");
                        	response.add(gg.getGraphe());
                    	}
                    	
                    }
                               
                    
                    
                    return;
                //}
            }
 
            else {
                response.add("HTTP/1.1 405 Method Not Allowed\r\n");
                response.add("Allow: GET\r\n");
                response.add("Content-Type: text/plain\r\n");
                response.add("Content-Length: 18\r\n\r\n");
                response.add("Method not allowed");
                return;
            }
        }
        
        
        
        if (path.matches("/viewCache.(?i)html")) {
            
            if (method == Method.GET) {

                    
                    
                    response.add("HTTP/1.1 200 OK\r\n");
                    response.add("Server: WebServer\r\n");
                    response.add("Content-Type: text/html\r\n");
                    
                    HTMLGenerator h = new HTMLGenerator(path);
                                
                    response.add("Content-Length: " + computeLength(h.getMsg()) + "\r\n\r\n");
                    response.addAll(h.getMsg());
                    return;

            }
            
            else {
                response.add("HTTP/1.1 405 Method Not Allowed\r\n");
                response.add("Allow: GET\r\n");
                response.add("Content-Type: text/plain\r\n");
                response.add("Content-Length: 18\r\n\r\n");
                response.add("Method not allowed");
                return;
            }
        }
        

    }

    
    /**
     * Method to compute the length of a message for use in the Content-Length
     * header.
     * 
     * @param message The message whose length must be determined.
     * @return The length of the message.
     */
    private int computeLength(ArrayList<String> message) {
        // Compute the Content-Length
        int length = 0;
        for (String string : message) {
            length += string.length();
        }
        return length;
    }
    
    /**
     * Final method that gets called on the object to obtain the sum of its
     * work.
     * 
     * @return reponse An ArrayList of Strings containing the full response
     * to be sent back to the client.
     */
    public ArrayList<String> getResponse() {
        return response;
    }
    
    public String getFunction() {
        String msgbody = query;
        String function ="";
        if(query != null && query.length()>10)
        {
        	int beginIndex = msgbody.indexOf("funct=") + 6;
        	int endIndex = msgbody.indexOf("&center=", beginIndex);
        	function = msgbody.substring(beginIndex, endIndex);
        }
        return function;
    }
    
    public String getCenter() {
        String msgbody = query;
        String center ="";
        if(query != null && query.length()>10)
        {
        	int beginIndex = msgbody.indexOf("&center=")+8;
        	int endIndex = msgbody.length();
        	center = msgbody.substring(beginIndex, endIndex);
        }
        return center;
    }
    
}