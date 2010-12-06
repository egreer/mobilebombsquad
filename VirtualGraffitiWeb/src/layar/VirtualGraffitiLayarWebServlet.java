package layar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.datanucleus.util.MathUtils;

import com.google.appengine.api.datastore.Blob;

@SuppressWarnings("serial")
public class VirtualGraffitiLayarWebServlet extends HttpServlet {
	
	final static String name = "virtualgraffiti";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String layerName = req.getParameter("layerName");
		String lon = req.getParameter("lon");
		String lat = req.getParameter("lat");
		String radius = req.getParameter("radius");
		
		if (lon == null || lat == null || radius == null || layerName == null || !layerName.equals(name)){
			resp.setContentType("text/plain");
			resp.getWriter().println("Please specify the lat, lon, radius and layerName");
		}else{
			double currentLon = Double.parseDouble(lon);
			double currentLat = Double.parseDouble(lat);
			double currentRadius = Double.parseDouble(radius);
			
			ArrayList<POI> pois = DatabaseAccess.getPoints(currentLat, currentLon, currentRadius);
			
			//Response
			resp.setContentType("text/plain");
			
			resp.getWriter().println("{");
			
			//Output
			resp.getWriter().println("\"layer\": \"" + name + "\",");
			resp.getWriter().println("\"errorCode\": 0," );
			resp.getWriter().println("\"errorString\": \"ok\",");
			
			//Output hotspots
			resp.getWriter().println("\"hotspots\": [");
			Iterator<POI> iter = pois.iterator();
			while(iter.hasNext()){
				POI point = iter.next();
				resp.getWriter().println(point.toJSONString());
				if(iter.hasNext()) resp.getWriter().println(",");
			}
			resp.getWriter().println("]");
			resp.getWriter().println("}");
		}	
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		
		String layerName = null;// = req.getParameter("layerName");
		String key = null;
		String paramLon = null;
		String paramLat = null;
		String paramAttribution= null;
		String paramTitle = null;
		
		Blob imageBlob = null;
		MyImage myImage = null;
		
		  ServletFileUpload upload = new ServletFileUpload();
		try {
			//List items = upload.getItemIterator(req);//.parseRequest(req);

			FileItemIterator itr = upload.getItemIterator(req);//items.iterator();
			while(itr.hasNext()) {
				FileItemStream item = itr.next();
				
				/* Handle Form Fields. */
				if(item.isFormField()) {
					String fieldName = item.getFieldName();
					
			    	//read it with BufferedReader
			    	BufferedReader br
			        	= new BufferedReader(
			        		new InputStreamReader(item.openStream()));
			 
			    	StringBuilder sb = new StringBuilder();
			 
			    	String line;
			    	while ((line = br.readLine()) != null) {
			    		sb.append(line);
			    	} 

					
					if(fieldName.equals("layerName"))	layerName = sb.toString();//getString();
					else if(fieldName.equals("key"))	key = sb.toString();
					else if(fieldName.equals("lon"))		paramLon = sb.toString();
					else if(fieldName.equals("lat"))		paramLat = sb.toString();
					else if(fieldName.equals("attribution")) paramAttribution =sb.toString();
					else if(fieldName.equals("title")) paramTitle =sb.toString();
					
				} else {
					/*
					 * Write file to the ultimate location.
					 */
					imageBlob = new Blob(IOUtils.toByteArray(item.openStream()));
					myImage = new MyImage(item.getName(),imageBlob); 
				}
			}

			if(imageBlob== null || myImage == null || key== null || paramLon == null || paramLat == null || layerName == null || !layerName.equals(name)){
				resp.getWriter().println("Please specify the key, lat, lon, radius and layerName");
				resp.getWriter().println("Lat: " + paramLat);
				resp.getWriter().println("Lon: " + paramLon);
				resp.getWriter().println("Key: " + key);
				resp.getWriter().println("layerName: " + layerName);
				if(imageBlob== null) resp.getWriter().println("imageBlob: null");
				if(myImage == null) resp.getWriter().println("myImage: null");
			} else{
			double lon = Double.parseDouble(paramLon);
			double lat = Double.parseDouble(paramLat);
			
			//Sig figs
			if(lon >= 100 || lon <= -100)	lon = (new BigDecimal(lon, (new MathContext(9)))).doubleValue();
			else lon = (new BigDecimal(lon, (new MathContext(8)))).doubleValue();
			lat = (new BigDecimal(lat, (new MathContext(8)))).doubleValue();
			
			if (lon > 180) lon = 180;
			else if (lon < -180) lon = -180;
			
			if (lat > 90) lat = 90;
			else if (lat < -90) lat = -90;
			
			POI point = new POI(key, lat, lon);
			point.setAttribution(paramAttribution);
			point.setTitle(paramTitle);
			point.setImage(myImage.getName());
		
		    if(DatabaseAccess.storeImage(myImage)){
		    	
		    	if (DatabaseAccess.storePOI(point)){
					resp.getWriter().println("Data & Image Stored");
				}else{
					resp.getWriter().println("Point Not Stored, Image Stored");
				}
		    }else{
				resp.getWriter().println("Point & Image Not Stored");
			}
		}

			
		
/*		resp.setContentType("text/plain");
		
		String layerName = req.getParameter("layerName");
		String key = req.getParameter("key");
		String paramLon = req.getParameter("lon");
		String paramLat = req.getParameter("lat");
		
		 // Get the image representation
	    
	    FileItemIterator iter = null;
	    FileItemStream imageItem = null;
	    try {
			iter = upload.getItemIterator(req);
			imageItem = iter.next();
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream imgStream = imageItem.openStream();
	    
  		
		if(key== null || paramLon == null || paramLat == null || layerName == null || !layerName.equals(name)){
			resp.getWriter().println("Please specify the key, lat, lon, radius and layerName");
			resp.getWriter().println(paramLat);
			resp.getWriter().println(paramLon);
			resp.getWriter().println(key);
			resp.getWriter().println(layerName);
		}else{
			double lon = Double.parseDouble(paramLon);
			double lat = Double.parseDouble(paramLat);
						
			POI point = new POI(key, lat, lon);

		    // construct our entity objects
		    Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
		    MyImage myImage = new MyImage(imageItem.getName(), imageBlob);

		    if(DatabaseAccess.storeImage(myImage)){
		    	
		    	if (DatabaseAccess.storePOI(point)){
					resp.getWriter().println("Data & Image Stored");
				}else{
					resp.getWriter().println("Point Not Stored, Image Stored");
				}
		    }else{
				resp.getWriter().println("Point & Image Not Stored");
			}
			
		}
	*/	
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}


	private double Integer(double d) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
