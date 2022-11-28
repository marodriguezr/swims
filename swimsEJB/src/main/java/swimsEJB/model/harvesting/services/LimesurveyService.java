package swimsEJB.model.harvesting.services;

import static swimsEJB.constants.SystemEnvironmentVariables.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.utilities.ArrayUtilities;

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
			JsonObject jsonObject = JsonParser.parseString(stringifiedResponse).getAsJsonObject();
			if (!jsonObject.get("error").isJsonNull())
				throw new Exception("Request failed, error: " + jsonObject.get("error").getAsString());
			return jsonObject;
		}
		throw new Exception("Request failed, Status code: " + response.getStatusLine().getStatusCode());
	}

	public static String getSessionKey() throws Exception {
		return executeHttpPostRequest("get_session_key", LIMESURVEY_ADMIN_USER, LIMESURVEY_FIRST_ADMIN_PASSWORD)
				.get("result").getAsString();
	}

	public static void releaseSessionKey(String sessionKey) throws Exception {
		executeHttpPostRequest("release_session_key", sessionKey);
	}

	public static JsonObject executeHttpPostRequestWithoutSessionKey(String method, Object... params) throws Exception {
		String sessionKey = getSessionKey();
		JsonObject jsonObject = executeHttpPostRequest(method, ArrayUtilities.add2BeginningOfArray(params, sessionKey));
		releaseSessionKey(sessionKey);
		return jsonObject;
	}

	public static List<LimesurveySurveyDto> listAllSurveys() throws Exception {
		try {
			JsonObject response = executeHttpPostRequestWithoutSessionKey("list_surveys", LIMESURVEY_ADMIN_USER);
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

	public static List<LimesurveySurveyDto> listAllActiveSurveys() throws Exception {
		return listAllSurveys().stream().filter(arg0 -> arg0.isActive()).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param limesurveySurveyId
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return Access token that the user is meant to use to access the survey.
	 */
	public static String addParticipant(int limesurveySurveyId, String email) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		map.put("email", email);

		List<HashMap<String, String>> hashMaps = new ArrayList<>();
		hashMaps.add(map);
		try {
			JsonObject response = executeHttpPostRequestWithoutSessionKey("add_participants", limesurveySurveyId,
					hashMaps);
			JsonArray jsonArray = response.get("result").getAsJsonArray();
			return jsonArray.get(0).getAsJsonObject().get("token").getAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(e.getMessage());
			throw new Exception("Ha ocurrido un error en la adición del participante " + email);
		}
	}

	public static JsonObject exportResponse(int limesurveySurveyId, String token) throws Exception {
		JsonObject response = executeHttpPostRequestWithoutSessionKey("export_responses_by_token", limesurveySurveyId,
				"json", token, null, "complete");
		if (response.get("result").isJsonObject()) {
			throw new Exception("La encuesta no ha sido respondida aún.");
		}
		byte[] decodedBytes = Base64.getDecoder().decode(response.get("result").getAsString());
		String decodedString = new String(decodedBytes);
		JsonArray jsonArray = JsonParser.parseString(decodedString).getAsJsonObject().get("responses").getAsJsonArray();
		if (jsonArray.isEmpty())
			throw new Exception("La encuesta no ha sido respondida aún.");
		;
		return jsonArray.get(0).getAsJsonObject();
	}

	public static int importSurvey(String base64EncodedSurvey) throws Exception {
		JsonObject response = executeHttpPostRequestWithoutSessionKey("import_survey", base64EncodedSurvey, "lss");
		return response.get("result").getAsInt();
	}

	public static HashMap<String, LimesurveyQuestionDto> listQuestions(int limesurveySurveyId) throws Exception {
		JsonObject response = executeHttpPostRequestWithoutSessionKey("list_questions", limesurveySurveyId);
		JsonArray jsonArray = response.get("result").getAsJsonArray();
		HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtos = new HashMap<>();
		jsonArray.forEach(arg0 -> {
			JsonObject jsonObject = arg0.getAsJsonObject();
			limesurveyQuestionDtos.put(jsonObject.get("title").getAsString(),
					new LimesurveyQuestionDto(jsonObject.get("id").getAsInt(), jsonObject.get("question").getAsString(),
							jsonObject.get("sid").getAsInt(), jsonObject.get("gid").getAsInt(),
							jsonObject.get("title").getAsString(), jsonObject.get("parent_qid").getAsInt()));
		});
		return limesurveyQuestionDtos;
	}

	public static void activateSurvey(int surveyId) throws Exception {
		executeHttpPostRequestWithoutSessionKey("activate_survey", surveyId);
	}

	public static void initializeSurveyParticipants(int surveyId) throws Exception {
		executeHttpPostRequestWithoutSessionKey("activate_tokens", surveyId);
	}

	public static void activateSurveyWithParticipants(int surveyId) throws Exception {
		activateSurvey(surveyId);
		initializeSurveyParticipants(surveyId);
	}
}
