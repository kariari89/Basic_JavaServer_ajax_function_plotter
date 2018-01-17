import java.awt.Image;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Cache.java
 * 
 * Wrapper class to hold the last twenty graphs.
 * Allows easy storage and access to the cache.
 * An ArrayList of Graphs supplemented by useful methods.
 * 
 * Only one instance needs to be spawned for the whole life of the server.
 * 
 * @author Arian Chitgar
 */
public class Cache {
    static ArrayList<GraphGenerator> cache = new ArrayList<GraphGenerator>();
    private static final int SIZE = 20;
    
    public static void addGraphe(String graphe, String center) {
        GraphGenerator g = new GraphGenerator(graphe, center);
        
        if(cache.size() <SIZE)
       	 cache.add(g);
        else{
        	for(int i = 0; i<SIZE-1; i++)
        	   cache.set(i, cache.get(i+1));
        	
        	cache.add(SIZE-1, g);
        }
        
    }
    
    public static int size() {
        return cache.size();
    }
    
    /*
     * Returns an ArrayList of the graphs stored in the cache
     * 
     */
    public ArrayList<GraphGenerator> getCache() {
        ArrayList<GraphGenerator> newest = new ArrayList<GraphGenerator>();
       
        
        ListIterator<GraphGenerator> li = cache.listIterator(cache.size());
        while (li.hasPrevious()) {
            newest.add(li.previous());
        } 
        return newest;
    }
    
    public static boolean inCache(String function){
    	for (int i = 0; i<cache.size(); i++)
    	{
    		if(function == (cache.get(i).getPloynom()))
    		{
    			return true;
    		}
    	}

    	return false;
    }
    
    public static String getInCache(String function)
    {
    	for (int i = 0; i<cache.size(); i++)
    	{
    		if(function == (cache.get(i).getPloynom()))
    		{
    			return cache.get(i).getGraphe();
    		}
    	}
    	return "";
    }
    
}