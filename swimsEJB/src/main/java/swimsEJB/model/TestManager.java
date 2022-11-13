package swimsEJB.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
//import org.apache.hc.core5.http.HttpEntity;
//import org.apache.hc.core5.http.HttpResponse;
//import org.apache.hc.core5.http.io.entity.StringEntity;

//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;

/**
 * Session Bean implementation class TestManager
 */
@Stateless
@LocalBean
public class TestManager {

	/**
	 * Default constructor.
	 */
	public TestManager() {
		// TODO Auto-generated constructor stub
	}

//	public static String parse(String jsonLine) {
////		JsonElement jelement = new JsonParser().parse(jsonLine);
//		JsonObject jobject = JsonParser.parseString(jsonLine).getAsJsonObject();
//		String result = jobject.get("result").getAsString();
//		return result;
//	}
	
	public void test() throws UnsupportedEncodingException {
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpClient client = HttpClientBuilder.create().build();
//        
//	      HttpPost post = new HttpPost("https://PATH_OF_YOUR_SERVER/index.php/admin/remotecontrol");
//	      post.setHeader("Content-type", "application/json");
//	      post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"YOUR_USERNAME\", \"YOUR_PASSWORD\" ], \"id\": 1}"));
//	      try {
//	        HttpResponse response = client.execute(post);
//	        if(response.getCode() == 200){
//	            HttpEntity entity = response.getEntity();
//	            String sessionKey = parse(EntityUtils.toString(entity));
//	            post.setEntity( new StringEntity("{\"method\": \"list_groups\", \"params\": [ \""+sessionKey+"\", \"ID_SURVEY\" ], \"id\": 1}"));
//	            response = client.execute(post);
//	            if(response.getStatusLine().getStatusCode() == 200){
//	                entity = response.getEntity();
//	                System.out.println(EntityUtils.toString(entity));
//	                }
	           }
	       
	       
//	      } catch (IOException e) {
//	        e.printStackTrace();
//	      }
//	}

}
