package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

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
			FileInputStream stream = new FileInputStream(tag.getImagePath());
			byte[] data = new byte[stream.available()];
			stream.read(data);
			
			//HTTP POST
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(SERVER_URI);
			InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(data), "image");
			StringBody lat = new StringBody("" + tag.lat);
			StringBody lon = new StringBody("" + tag.lon);
			StringBody key = new StringBody(tag.key);
			
			MultipartEntity multipartContent = new MultipartEntity();
			multipartContent.addPart("image", isb);
			multipartContent.addPart("key", key);
			multipartContent.addPart("lat", lat);
			multipartContent.addPart("lon", lon);
			
			httpPost.setEntity(multipartContent);
			HttpResponse res = httpClient.execute(httpPost);
			res.getEntity().getContent().close();
					
		} catch (Exception e) {
			
		}
	}
	
}
