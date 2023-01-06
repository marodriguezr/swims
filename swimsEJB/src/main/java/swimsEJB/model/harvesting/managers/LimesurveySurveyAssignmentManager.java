package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.entities.LimesurveyExpectedAnswer;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.entities.LimesurveyUnexpectedAnswer;
import swimsEJB.model.harvesting.services.LimesurveyService;

/**
 * Session Bean implementation class SurveyAssignmentManager
 */
@Stateless
@LocalBean
public class LimesurveySurveyAssignmentManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private LimesurveyExpectedAnswerManager responseManager;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;
	@EJB
	private LimesurveyExpectedAnswerManager limesurveyExpectedAnswerManager;
	@EJB
	private LimesurveyUnexpectedAnswerManager limesurveyUnexpectedAnswerManager;
	
	/**
	 * Default constructor.
	 */
	public LimesurveySurveyAssignmentManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findLimesurveySurveyIdsByOaiRecordId(String oaiRecordId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select lsa.limesurvey_survey_id from harvesting.thesis_assignments ta, harvesting.limesurvey_survey_assignments lsa "
						+ "where ta.id = lsa.thesis_assignment_id and ta.oai_record_id = '" + oaiRecordId + "'");
		List<Object> objects = query.getResultList();
		List<Integer> alreadyPresentlimesurveySurveyIds = new ArrayList<>();
		for (Object object : objects) {
			alreadyPresentlimesurveySurveyIds.add(Integer.parseInt(object.toString()));
		}
		return alreadyPresentlimesurveySurveyIds;
	}

	public LimesurveySurveyAssignment createOneSurveyAssignment(int limesurveySurveyId,
			String limesurveySurveyAccessToken, ThesisAssignment thesisAssignment) throws Exception {
		LimesurveySurveyAssignment surveyAssignment = new LimesurveySurveyAssignment();
		surveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		surveyAssignment.setLimesurveySurveyToken(limesurveySurveyAccessToken);
		surveyAssignment.setThesisAssignment(thesisAssignment);
		surveyAssignment.setIsDispatched(false);
		surveyAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		surveyAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveySurveyAssignment) daoManager.createOne(surveyAssignment);
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveySurveyAssignment> findAllSurveyAssignmentsByThesisAssignementId(int thesisAssignementId) {
		List<LimesurveySurveyAssignment> SurveyAssignments = daoManager.findManyWhere(LimesurveySurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId, null);
		return SurveyAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveySurveyAssignment> findAllUndispatchedSurveyAssignmentsByThesisAssignementId(
			int thesisAssignementId) {
		List<LimesurveySurveyAssignment> SurveyAssignments = daoManager.findManyWhere(LimesurveySurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId + " and o.isDispatched = false", null);
		return SurveyAssignments;
	}

	public LimesurveySurveyAssignment findOneSurveyAssignmentById(int id) throws Exception {
		return (LimesurveySurveyAssignment) daoManager.findOneById(LimesurveySurveyAssignment.class, id);
	}

	public LimesurveySurveyAssignment updateOneSurveyAssignment(LimesurveySurveyAssignment surveyAssignment,
			ThesisAssignment thesisAssignment, Integer limesurveySurveyId, String limesurveySurveyToken,
			Boolean isDispatched) throws Exception {
		if (thesisAssignment != null)
			surveyAssignment.setThesisAssignment(thesisAssignment);
		if (limesurveySurveyId != null)
			surveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		if (limesurveySurveyToken != null)
			surveyAssignment.setLimesurveySurveyToken(limesurveySurveyToken);
		if (isDispatched != null)
			surveyAssignment.setIsDispatched(isDispatched);
		return (LimesurveySurveyAssignment) daoManager.updateOne(surveyAssignment);
	}

	public LimesurveySurveyAssignment updateOneSurveyAssignmentById(int id, ThesisAssignment thesisAssignment,
			Integer limesurveySurveyId, String limesurveySurveyToken, Boolean isDispatched) throws Exception {
		LimesurveySurveyAssignment surveyAssignment = findOneSurveyAssignmentById(id);
		if (surveyAssignment == null)
			throw new Exception("La Asignación de Encuesta especificada no se encuentra registrada.");
		return updateOneSurveyAssignment(surveyAssignment, thesisAssignment, limesurveySurveyId, limesurveySurveyToken,
				isDispatched);
	}

	public LimesurveySurveyAssignment updateOneSurveyAssignmentAsDispatched(LimesurveySurveyAssignment surveyAssignment)
			throws Exception {
		return updateOneSurveyAssignment(surveyAssignment, null, null, null, true);
	}

	public LimesurveySurveyAssignment updateOneSurveyAssignmentAsDispatchedById(int id) throws Exception {
		return updateOneSurveyAssignmentById(id, null, null, null, true);
	}

	/**
	 * The following method queries limesurvey for an specific response which is
	 * identified by token and survey ID. Then it gathers the data from the JSON
	 * structure in which answers are keyed by the question's title, if the answer
	 * belongs to a child question the key would be parentTitle[childTitle] and if
	 * the question is not an expected one for instance "other" keyed as
	 * questionTitle[other]
	 * 
	 * @param surveyAssignment
	 * @return
	 * @throws Exception
	 */
	public LimesurveySurveyAssignment dispatchSurvey(LimesurveySurveyAssignment surveyAssignment) throws Exception {
		JsonObject response = LimesurveyService.exportResponse(surveyAssignment.getLimesurveySurveyId(),
				surveyAssignment.getLimesurveySurveyToken());

		List<LimesurveyExpectedAnswer> expectedAnswers = new ArrayList<>();
		List<LimesurveyUnexpectedAnswer> unexpectedAnswers = new ArrayList<>();

		HashMap<Integer, LimesurveyQuestion> questionsMap = new HashMap<>();
		for (LimesurveyQuestion question : limesurveyQuestionManager
				.findAllQuestionsByLimesurveySurveyId(surveyAssignment.getLimesurveySurveyId())) {
			questionsMap.put(question.getLimesurveyQuestionId(), question);
		}
		HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtos = LimesurveyService
				.listQuestions(surveyAssignment.getLimesurveySurveyId());

		for (LimesurveyQuestion question : questionsMap.values()) {
			JsonElement element = response.get(question.getLimesurveyQuestionTitle() + "[other]");
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				unexpectedAnswers.add(limesurveyUnexpectedAnswerManager.createOneLimesurveyUnexpectedAnswer(question,
						element.getAsString(), surveyAssignment));
				continue;
			}

			element = response.get(question.getLimesurveyQuestionTitle());
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				expectedAnswers.add(limesurveyExpectedAnswerManager.createOneLimesurveyExpectedAnswer(question,
						element.getAsString(), surveyAssignment));
				continue;
			}

			LimesurveyQuestion parentQuestion = questionsMap
					.get(limesurveyQuestionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid());
			if (parentQuestion == null)
				continue;
			element = response.get(
					parentQuestion.getLimesurveyQuestionTitle() + "[" + question.getLimesurveyQuestionTitle() + "]");
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				expectedAnswers.add(limesurveyExpectedAnswerManager.createOneLimesurveyExpectedAnswer(question,
						element.getAsString(), surveyAssignment));
				continue;
			}
		}

		if (expectedAnswers.isEmpty() && unexpectedAnswers.isEmpty())
			throw new Exception("Ha ocurrido un error en el proceso de despacho.");
		surveyAssignment = updateOneSurveyAssignmentAsDispatched(surveyAssignment);

		surveyAssignment.setLimesurveyExpectedAnswers(expectedAnswers);
		surveyAssignment.setLimesurveyUnexpectedAnswers(unexpectedAnswers);

		return surveyAssignment;
	}

	
}
