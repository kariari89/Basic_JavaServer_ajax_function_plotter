import java.awt.Image;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * HTMLGenerator.java
 * 
 * Class to generate HTML code held in ArrayLists and ready to be fed to
 * an output stream.
 * 
 * The server need only return the HTML for two of its pages : 
 *      grapheView.html
 *      viewCache.html
 *      
 * @author Arian Chitgar
 */
public class HTMLGenerator {
    private ArrayList<String> msg = new ArrayList<String>();
    
    String path;
    
        
    public HTMLGenerator(String path) {
        if (path.matches("/grapheView.(?i)html"))
            generateGrapheView();
        if (path.matches("/viewCache.(?i)html"))
            generateViewPosts();
    }
    
    
    private void generateGrapheView() {
        msg.add("<!DOCTYPE html>\n");
        msg.add("<html>\n");
        msg.add("<head>\n");
        msg.add("<title>Polynomial Visualize</title>\n");
        
        msg.add("</head>\n");
        msg.add("\n");
        msg.add("<body bgcolor=#6f8fbc>\n");
        msg.add("<center>");
        msg.add("<div style=\"color:#fff\"><h1>Polinomial Visualizer</h1></div>\n");
        msg.add("<label for=\"function\">Function :</label>\n");
        msg.add("<input type=\"text\" id=\"function\" /><br />\n");
        msg.add("<br>\n");
        msg.add("<label for=\"x\">x :</label>\n");
        msg.add("<input type=\"text\" id=\"x\" />\n");
        msg.add("<br>\n");
        msg.add("<label for=\"y\">y :</label>\n");
        msg.add("<input type=\"text\" id=\"y\" />\n");
        msg.add("<br>\n");
        msg.add("<input type=\"button\" onclick=\"request(readData);\" value=\"Executer\" />\n");
        msg.add("<div id=\"salam\"> <div id=\"txt\"> </div> </div>");
        
        
        
        
        msg.add("</center>");
        msg.add("</body>\n");
        msg.add("<script>\n"
        		+ "function mouseUp(e) {\n"
                + "   var newX = ((e.pageX -(document.getElementById(\"graphe\").offsetLeft))/40)-5;\n"
                + "   var newY = (((document.getElementById(\"graphe\").offsetTop)- e.pageY)/40)+5;\n"
                + "   var points = centerPoint.split(' ');\n"
                + "   newX += + (points[0]);\n"
                + "   newY += + (points[1]);\n"
                + "   centerPoint = newX + \" \" + newY;\n"
                + "   var toRemove = document.getElementById(\"txt\");\n"
                + "   if(toRemove != null) toRemove.remove();\n"
                + "   var toRemove2 = document.getElementById(\"graphe\");"
                + "   toRemove2.remove();"
                + "   request2(readData);\n"
                + "}\n"
        		+ " var toSend;\n"
        		+ " var centerPoint;"
        		+ "function getXMLHttpRequest() {\n" 
	            + "   var xhr = null;\n" 
	
	            + "   if (window.XMLHttpRequest || window.ActiveXObject) {\n"
		        + "      if (window.ActiveXObject) {\n" 
			    + "         try {\n"
				+ "            xhr = new ActiveXObject(\"Msxml2.XMLHTTP\");\n"
			    + "         } catch(e) {\n"
				+ "              xhr = new ActiveXObject(\"Microsoft.XMLHTTP\");\n"
			    + "         }\n"
		        + "      } else {\n"
			    + "           xhr = new XMLHttpRequest();\n"
		        + "        }\n"
	            + " } else {\n"
		        + "       alert(\"Votre navigateur ne supporte pas l'objet XMLHTTPRequest...\");\n"
		        + "return null;\n"
	            + "}\n"
	
	            + "return xhr;\n"
                + "}"
	            
                + "function request(callback) {\n"
                + "   var xhr = getXMLHttpRequest();\n"
                	
                + "	  xhr.onreadystatechange = function() {\n"
                + "      if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 0)) {\n"
                + "         callback(xhr.responseText);\n"
                + "	     }\n"
                + "   };\n"
                	
                + "	var funct = encodeURIComponent(document.getElementById(\"function\").value);\n"
                + "	var x = encodeURIComponent(document.getElementById(\"x\").value);\n"
                + "	var y = encodeURIComponent(document.getElementById(\"y\").value);\n"
                + " if(isPolynom(funct) && isCenterPoint(x, y)){\n"
                + "	xhr.open(\"GET\", \"grapheView.html?funct=\" + toSend + \"&center=\" + centerPoint , true);\n"
                + "	xhr.send(null);\n"
                + "}\n"
                + "else{\n"
                + "document.getElementById(\"txt\").insertAdjacentHTML(\"beforeend\",\"enter a polynomial expression\");\n"
                + "}\n"
                
                	
                
                + "}\n"
                
				+ "function request2(callback) {\n"
				+ "   var xhr = getXMLHttpRequest();\n"
	
				+ "	  xhr.onreadystatechange = function() {\n"
				+ "      if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 0)) {\n"
				+ "         callback(xhr.responseText);\n"
				+ "	     }\n"
				+ "   };\n"
	

				+ "	xhr.open(\"GET\", \"grapheView.html?funct=\" + toSend + \"&center=\" + centerPoint , true);\n"
				+ "	xhr.send(null);\n"


				+ "}\n"
                
                
                + "function readData(data) {\n"
                + "var img = \"src='data:image/png;base64,\" + data + \"'\";\n"
                + "var tmp = \"mouseUp(event)\"; \n"
                + "var tmp2 = \"graphe\";\n "
                + "var tmp3 = tmp2 + \" \" + \"onmouseup= \" + tmp;\n"
                + "img = \"<img id=\" + tmp3+ \" \"+ img + \"/>\";\n"
                
                + " document.getElementById(\"salam\").insertAdjacentHTML(\"beforeend\", img)"
                
                + "}\n"
                + "function isPolynom(str){\n"
                + "var element = document.getElementById(\"txt\"),index;"
                + "for (index = element.length - 1; index >= 0; index--) {\n"
                + "   element[index].parentNode.removeChild(element[index]);\n"
                + "}\n"
                + "   str = str.replace(/%20/g,'');\n"
                + "   str = str.replace(/%2B/g,'+');\n"
                + "   str = str.replace(/-/g,'+-');\n"
                + "   str = str.trim();\n"
                + "   var exp = str.split(\"+\");\n"
                + "   errMsg = document.getElementById(\"txt\");\n"
                + "   var dec;\n"
                + "   toSend = \"\";"
                + "   for(i = 0; i< exp.length; i++)\n"
                + "   {\n"
                + "      dec = exp[i].split('x');\n"
                + "      if(dec.length == 2){\n"
                + "         if(!( (dec[0] == \"\" || !isNaN(dec[0]) ) && (dec[1] == \"\" || !isNaN(dec[1])) ))\n"
                + "         {\n"
                + "            return false;\n"
                + "         }\n"
                + "         dec[0]==\"\" ? (toSend += \"1 \") : (toSend +=  dec[0] + \" \");\n"
                + "         dec[1]==\"\" ? (toSend += \"1 \") : (toSend +=  dec[1] + \" \");\n"
                + "      }\n"
                + "      else if(dec.length == 1){\n"
                + "         if(!isNaN(dec)){\n"
                + "            toSend += dec[0] + \" 0 \" ;\n"
                + "            }\n"
                + "         else{ return false;}\n"
                + "      }\n"
                + "      if( (dec.length != 1 ) && (dec.length != 2) ) {\n"
                + "         var errMsg = document.getElementById(\"txt\");\n"
                + "         errMsg.insertAdjacentHTML('beforeend',\"Incorrect Expression\");\n"
                + "         return false;\n"
                + "      }\n"
                + "         errMsg.insertAdjacentHTML('beforeend',\"plot of y = \" + str);\n"
                + "   }\n"
                + "         return true;\n"
                + "}\n"
                
                + "function isCenterPoint(x, y)\n"
                +" {\n"
                + "   if(isNaN(x)) return false;\n"
                + "   if(isNaN(y)) return false;\n"
                + "   centerPoint = (x + \" \" + y);\n"
                + "   return true;\n"
                + "}\n"

                
        		
        		+ "</script>\n");
        msg.add("</html>");
    }
    
    private void generateViewPosts() {
        msg.add("<!DOCTYPE html>\n");
        msg.add("<html>\n");
        msg.add("<head>\n");
        msg.add("<title>Chat server</title>\n");
        msg.add("</head>\n");
        msg.add("<body bgcolor=daddc6>\n");
        msg.add("<br><br>\n");
        msg.add("<center>\n");
        
        for (int i = 0; i<Cache.cache.size(); i++) 
        {
        	msg.add("<img src=\"data:image/png;base64,"
        			+ (Cache.cache.get(i).getGraphe()) + "\">");
        }
            
        msg.add("<br>\n");
        msg.add("</center>\n");
        msg.add("</body>\n");
        msg.add("</html>");
    }
    
    public ArrayList<String> getMsg() {
        return this.msg;
    }
    
}