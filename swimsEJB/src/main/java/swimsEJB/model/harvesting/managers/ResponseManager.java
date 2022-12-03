package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.google.gson.JsonObject;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionPropertiesDto;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.Response;
import swimsEJB.model.harvesting.entities.SurveyAssignment;
import swimsEJB.model.harvesting.services.LimesurveyService;

/**
 * Session Bean implementation class ResponseManager
 */
@Stateless
@LocalBean
public class ResponseManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private QuestionManager questionManager;

	/**
	 * Default constructor.
	 */
	public ResponseManager() {
		// TODO Auto-generated constructor stub
	}

	public Response createOneResponse(Question question, String limesurveyAnswerCode, SurveyAssignment surveyAssignment)
			throws Exception {
		Response response = new Response();
		response.setQuestion(question);
		response.setLimesurveyAnswerCode(limesurveyAnswerCode);
		response.setSurveyAssignment(surveyAssignment);
		response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		response.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (Response) daoManager.createOne(response);
	}

	public Response createOneResponse(String limesurveyQuestionTitle, String limesurveyAnswerCode,
			SurveyAssignment surveyAssignment) throws Exception {
		Question question = questionManager.findOneByQuestionTitle(limesurveyQuestionTitle);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneResponse(question, limesurveyAnswerCode, surveyAssignment);
	}

	/**
	 * In this methods brackets are aded around question titles when they are
	 * subquestions because limesurvey would return subquestions keyed with the
	 * format question_title[subquestion_title]
	 * 
	 * @param limesurveySurveyId
	 * @param limesurveySurveyToken
	 * @param surveyAssignment
	 * @return
	 * @throws Exception
	 */
	public List<Response> createManyResponsesByLimesurveySurveyIdAndLimesurveySurveyToken(
			SurveyAssignment surveyAssignment) throws Exception {
		List<Response> responses = new ArrayList<>();

		List<Question> questions = questionManager
				.findAllQuestionsByLimesurveySurveyId(surveyAssignment.getLimesurveySurveyId());
		HashMap<String, LimesurveyQuestionDto> questionDtos = LimesurveyService
				.listQuestions(surveyAssignment.getLimesurveySurveyId());

		List<Integer> parentQuestionIds = questionDtos.values().stream().map(arg0 -> arg0.getParentQid())
				.collect(Collectors.toList());
		parentQuestionIds.removeIf(arg0 -> arg0 == 0);
		parentQuestionIds = new ArrayList<>(new HashSet<>(parentQuestionIds));
		HashMap<Integer, LimesurveyQuestionPropertiesDto> parentQuestionProperties = new HashMap<>();
		for (Integer integer : parentQuestionIds) {
			parentQuestionProperties.put(integer, LimesurveyService.getQuestionProperties(integer));
		}

		JsonObject response = LimesurveyService.exportResponse(surveyAssignment.getLimesurveySurveyId(),
				surveyAssignment.getLimesurveySurveyToken());

		for (Question question : questions) {
			if (questionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid() != 0) {
				if (response.get(parentQuestionProperties
						.get(questionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid()).getTitle() + "["
						+ question.getLimesurveyQuestionTitle() + "]").isJsonNull())
					continue;
				if (response.get(parentQuestionProperties
						.get(questionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid()).getTitle() + "["
						+ question.getLimesurveyQuestionTitle() + "]").getAsString().isBlank())
					continue;
				responses.add(createOneResponse(question,
						response.get(parentQuestionProperties
								.get(questionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid()).getTitle()
								+ "[" + question.getLimesurveyQuestionTitle() + "]").getAsString(),
						surveyAssignment));
				continue;
			}
			if (response.get(question.getLimesurveyQuestionTitle()).isJsonNull())
				continue;
			if (response.get(question.getLimesurveyQuestionTitle()).getAsString().isBlank())
				continue;
			responses.add(createOneResponse(question, response.get(question.getLimesurveyQuestionTitle()).getAsString(),
					surveyAssignment));
		}
		return responses;
	}
}
