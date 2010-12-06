package layar;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;

@SuppressWarnings("serial")
public class VirtualGraffitiImageWebServlet extends HttpServlet {
	
	final static String name = "virtualgraffiti";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
			String imageName = req.getParameter("name");
			String quality = req.getParameter("quality");
			
			if (imageName == null){
			    resp.setContentType("text/plain");
			    resp.getOutputStream().println("Please specify an image name.");
				
			}else{
				 // find desired image
				 Blob image = DatabaseAccess.getImage(imageName);
				
				 if (image != null){
				    // serve the first image
				    resp.setContentType("image/png");
				    resp.getOutputStream().write(image.getBytes());
				 }else{
					 resp.setContentType("text/plain");
					 resp.getOutputStream().println("No image found.");	
				 }
			}
		}
			
}
