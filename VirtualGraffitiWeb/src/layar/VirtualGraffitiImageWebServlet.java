package layar;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

@SuppressWarnings("serial")
public class VirtualGraffitiImageWebServlet extends HttpServlet {
	
	final static String name = "virtualgraffiti";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
			String imageName = req.getParameter("name");
			String qualityString = req.getParameter("quality");
			Quality quality = Quality.getQuality(qualityString);
			
	        ImagesService imagesService = ImagesServiceFactory.getImagesService();


			if (imageName == null){
			    resp.setContentType("text/plain");
			    resp.getOutputStream().println("Please specify an image name.");
				
			}else{
				 // find desired image
				 Blob image = DatabaseAccess.getImage(imageName);
				
				 if (image != null){
				    // serve the first image
				        Image oldImage = ImagesServiceFactory.makeImage(image.getBytes());
				        if (Quality.FULL.equals(quality)){
				        	resp.setContentType("image/png");
				        	resp.getOutputStream().write(image.getBytes());
				        }else{
					        Transform resize = ImagesServiceFactory.makeResize(quality.getX(), quality.getY());
	
					        Image newImage = imagesService.applyTransform(resize, oldImage);
	
						    resp.setContentType("image/png");
						    resp.getOutputStream().write(newImage.getImageData());
				        }
				 }else{
					 resp.setContentType("text/plain");
					 resp.getOutputStream().println("No image found.");	
				 }
			}
		}
			
}
