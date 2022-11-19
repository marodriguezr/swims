package swimsWeb.services;

import static swimsEJB.constants.SystemEnvironmentVariables.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import swimsWeb.dtos.LimesurveySurveyDto;

public class LimesurveyService {
	private static HttpClient httpClient = HttpClientBuilder.create().build();
	private static Gson gson = new Gson();

	public static HttpPost getHttpPost() {
		HttpPost post = new HttpPost(LIMESURVEY_REMOTECONTROL_USES_SSL ? "https://"
				: "http://"
						+ (LIMESURVE_ON_SAME_NETWORK ? LIMESURVEY_HOST + ":" + LIMESURVEY_PORT
								: LIMESURVEY_PUBLIC_HOST + ":" + LIMESURVEY_PUBLIC_PORT)
						+ LIMESURVEY_BASE_URL + "/index.php/admin/remotecontrol");
		post.setHeader("Content-type", "application/json");
		return post;
	}

	public static JsonObject executeHttpPostRequest(String method, Object... params) throws Exception {
		HttpPost post = getHttpPost();
		String stringifiedParams = gson.toJson(params);
		post.setEntity(
				new StringEntity("{\"method\": \"" + method + "\", \"params\": " + stringifiedParams + ", \"id\": 1}"));
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String stringifiedResponse = EntityUtils.toString(response.getEntity());
			return JsonParser.parseString(stringifiedResponse).getAsJsonObject();
		}
		throw new Exception("Request failed, Status code: " + response.getStatusLine().getStatusCode());
	}

	public static String getSessionKey() throws Exception {
		try {
			return executeHttpPostRequest("get_session_key", LIMESURVEY_ADMIN_USER, LIMESURVEY_FIRST_ADMIN_PASSWORD)
					.get("result").getAsString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(
					"Ha ocurrido un error en la interconexión con Limesurvey. Es probable que el nombre de usuario o la contraseña sean incorrectos.");
		}
	}

	public static List<LimesurveySurveyDto> listAllSurveys() throws Exception {
		try {
			JsonObject response = executeHttpPostRequest("list_surveys", getSessionKey(), LIMESURVEY_ADMIN_USER);
			JsonArray jsonArray = response.get("result").getAsJsonArray();
			List<LimesurveySurveyDto> LimesurveySurveyDtos = new ArrayList<>();

			jsonArray.forEach(arg0 -> {
				JsonObject jobject2 = arg0.getAsJsonObject();
				LimesurveySurveyDtos.add(new LimesurveySurveyDto(jobject2.get("sid").getAsInt(),
						jobject2.get("surveyls_title").getAsString(),
						jobject2.get("active").getAsString() == "N" ? false : true));
			});
			return LimesurveySurveyDtos;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la adquisición de todas las encuestas.");
		}
	}
}
