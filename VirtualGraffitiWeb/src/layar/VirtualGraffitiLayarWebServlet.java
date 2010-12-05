package layar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.*;

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
			resp.getWriter().println("],");
			resp.getWriter().println("}");
		}	
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		resp.setContentType("text/plain");
		
		String layerName = req.getParameter("layerName");
		String key = req.getParameter("key");
		String paramLon = req.getParameter("lon");
		String paramLat = req.getParameter("lat");
				
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
			
			
			if (DatabaseAccess.storePOI(point)){
				resp.getWriter().println("Data Stored");
			}else{
				resp.getWriter().println("Data Not Stored");
			}
		}
		
	}
}
