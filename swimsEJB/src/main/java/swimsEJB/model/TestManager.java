package swimsEJB.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import swimsEJB.model.core.managers.DaoManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

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

	@EJB
	private DaoManager daoManager;

	public static String parse(String jsonLine) {
		JsonObject jobject = JsonParser.parseString(jsonLine).getAsJsonObject();
		String result = jobject.get("result").getAsString();
		return result;
	}

	public void test() throws UnsupportedEncodingException {
		HttpClient client = HttpClientBuilder.create().build();
		final String limesurveyHost = System.getenv().getOrDefault("LIMESURVEY_HOST",
				"http://swims-limesurvey-dev:8080/index.php/admin/remotecontrol");
		HttpPost post = new HttpPost(limesurveyHost);
		post.setHeader("Content-type", "application/json");
		post.setEntity(
				new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"admin\", \"foobar\" ], \"id\": 1}"));
		try {
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String stringifiedEntity = EntityUtils.toString(entity);
				System.out.println(stringifiedEntity);
//				JsonObject jobject = JsonParser.parseString(stringifiedEntity).getAsJsonObject();
//				String result = jobject.get("result").getAsString();
				String sessionKey = parse(stringifiedEntity);
				System.out.println(sessionKey);
//				post.setEntity(new StringEntity("{\"method\": \"list_groups\", \"params\": [ \"" + sessionKey
//						+ "\", \"ID_SURVEY\" ], \"id\": 1}"));
//				response = client.execute(post);
//				if (response.getStatusLine().getStatusCode() == 200) {
//					entity = response.getEntity();
//					System.out.println(EntityUtils.toString(entity));
//				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test4(byte[] bytes) {

	}

}
