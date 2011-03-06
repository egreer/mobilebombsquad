package edu.wpi.cs525h.ayeg.virtualgraffiti.tag;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


public class ConnectionTest {

	static final String LAYER_NAME = "virtualgraffiti";
	static final String SERVER_URI = "http://virtual-graffiti.appspot.com/layar";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Tag tag = new Tag(90, 0, 0, "TestPoint", "Test Point North Pole");
		tag.setImagePath("C:\\questionmark.jpg");


		
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
		StringBody title = new StringBody(tag.getTitle());
		StringBody attribution = new StringBody(tag.getAttribution());
		
		MultipartEntity multipartContent = new MultipartEntity();
		multipartContent.addPart("image", isb);
		multipartContent.addPart("lat", lat);
		multipartContent.addPart("lon", lon);
		multipartContent.addPart("title", title);
		multipartContent.addPart("attribution", attribution);
		multipartContent.addPart("layerName", new StringBody(LAYER_NAME));
		
		httpPost.setEntity(multipartContent);
		HttpResponse res = httpClient.execute(httpPost);
		res.getEntity().getContent().close();
	}

}
