package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * http://w3mentor.com/learn/java/android-development/android-http-services/example-of-multipart-post-using-android/
 * 
 * @author Andrew Yee
 *
 */
public class ConnectionUtil {
	
	static final String LAYER_NAME = "virtualgraffiti";
	static final String SERVER_URI = "http://virtual-graffiti.appspot.com/layar";
	
	public static void postImage(Tag tag) {
		try {
			//Read image file
			File image = new File(tag.getImagePath());
			
			FileInputStream stream = new FileInputStream(image);
			//FileInputStream stream = new FileInputStream(tag.getImagePath());
			byte[] data = new byte[stream.available()];
			stream.read(data);
			
			//HTTP POST
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(SERVER_URI);
			InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(data), image.getName());
			StringBody lat = new StringBody("" + tag.getLat());
			StringBody lon = new StringBody("" + tag.getLon());
			StringBody key = new StringBody(tag.getKey());
			StringBody title = new StringBody(tag.getTitle());
			StringBody attribution = new StringBody(tag.getAttribution());
			
			MultipartEntity multipartContent = new MultipartEntity();
			multipartContent.addPart("image", isb);
			multipartContent.addPart("key", key);
			multipartContent.addPart("lat", lat);
			multipartContent.addPart("lon", lon);
			multipartContent.addPart("title", title);
			multipartContent.addPart("attribution", attribution);
			multipartContent.addPart("layerName", new StringBody(LAYER_NAME));
			
			httpPost.setEntity(multipartContent);
			HttpResponse res = httpClient.execute(httpPost);
			res.getEntity().getContent().close();
					
		} catch (Exception e) {
			
		}
	}
	
}
