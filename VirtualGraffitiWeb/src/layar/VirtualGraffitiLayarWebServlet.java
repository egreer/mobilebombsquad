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

import com.google.appengine.api.datastore.Blob;

/** The Layar Web Servlet
 * 
 * @author Eric Greer
 *
 */
@SuppressWarnings("serial")
public class VirtualGraffitiLayarWebServlet extends HttpServlet {

	final static String name = "virtualgraffiti";

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		String layerName = req.getParameter("layerName");
		String lon = req.getParameter("lon");
		String lat = req.getParameter("lat");
		String radius = req.getParameter("radius");
		String alt = req.getParameter("alt");

		if (lon == null || lat == null || radius == null || layerName == null || !layerName.equals(name)){
			resp.setContentType("text/plain");
			resp.getWriter().println("Please specify the lat, lon, radius and layerName");
		}else{
			
			double currentLon = Double.parseDouble(lon);
			double currentLat = Double.parseDouble(lat);
			double currentRadius = Double.parseDouble(radius);
			
			int currentAlt = Integer.MIN_VALUE;
			if (alt != null && !alt.isEmpty()) currentAlt = (int) Math.round(Double.parseDouble(alt));
			
			ArrayList<POI> pois = DatabaseAccess.getPoints(currentLat, currentLon, currentRadius, currentAlt);

			if(pois.size()!= 0){
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

			else{
				resp.setContentType("text/plain");

				resp.getWriter().println("{");
				//Output
				resp.getWriter().println("\"layer\": \"" + name + "\",");
				resp.getWriter().println("\"errorCode\": 20," );
				resp.getWriter().println("\"errorString\": \"No Graffiti found please adjust range\",");
				resp.getWriter().println("\"hotspots\": []");
				resp.getWriter().println("}");	
			}
		}	
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
	throws IOException {

		String layerName = null;
		String paramLon = null;
		String paramLat = null;
		String paramAttribution= null;
		String paramTitle = null;
		String paramAlt = null;

		Blob imageBlob = null;
		MyImage myImage = null;

		ServletFileUpload upload = new ServletFileUpload();
		try {

			FileItemIterator itr = upload.getItemIterator(req);
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


					if(fieldName.equals("layerName"))	layerName = sb.toString();
					else if(fieldName.equals("lon"))		paramLon = sb.toString();
					else if(fieldName.equals("lat"))		paramLat = sb.toString();
					else if(fieldName.equals("attribution")) paramAttribution =sb.toString();
					else if(fieldName.equals("title")) paramTitle =sb.toString();
					else if(fieldName.equals("alt")) paramAlt =sb.toString();

				} else {
					/*
					 * Write file to the ultimate location.
					 */
					imageBlob = new Blob(IOUtils.toByteArray(item.openStream()));
					myImage = new MyImage(item.getName(),imageBlob); 
				}
			}

			if(imageBlob== null  || myImage == null ||  paramLon == null || paramLon.equals("") || paramLat == null || paramLat.equals("") || layerName == null || !layerName.equals(name)){
				resp.getWriter().println("Please specify the key, lat, lon, File and layerName");
				resp.getWriter().println("Lat: " + paramLat);
				resp.getWriter().println("Lon: " + paramLon);
				resp.getWriter().println("layerName: " + layerName);
				if(imageBlob== null) resp.getWriter().println("imageBlob: null");
				if(myImage == null) resp.getWriter().println("myImage: null");
			} else{

				double lon;
				double lat;
				int alt = Integer.MIN_VALUE;
				
				try{
					lon = Double.parseDouble(paramLon);
					lat = Double.parseDouble(paramLat);
					if (paramAlt != null && !paramAlt.isEmpty()) alt = Integer.parseInt(paramAlt);
				}catch(NumberFormatException e){
					resp.getWriter().println("Lattitude and Longitude must be numbers");
					return;
				}

				//Sig figs
				if(lon >= 100 || lon <= -100)	lon = (new BigDecimal(lon, (new MathContext(9)))).doubleValue();
				else lon = (new BigDecimal(lon, (new MathContext(8)))).doubleValue();
				lat = (new BigDecimal(lat, (new MathContext(8)))).doubleValue();

				if (lon > 180) lon = 180;
				else if (lon < -180) lon = -180;

				if (lat > 90) lat = 90;
				else if (lat < -90) lat = -90;

				POI point = new POI(lat, lon);
				point.setAttribution(paramAttribution);
				point.setTitle(paramTitle);
				point.setImage(myImage.getName());
				point.setAlt(alt);
				
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


		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
