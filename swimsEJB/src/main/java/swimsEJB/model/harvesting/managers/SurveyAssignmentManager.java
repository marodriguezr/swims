package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionPropertiesDto;
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.SurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.entities.UncodedExpectedAnswer;
import swimsEJB.model.harvesting.entities.UnexpectedAnswer;
import swimsEJB.model.harvesting.services.LimesurveyService;

/**
 * Session Bean implementation class SurveyAssignmentManager
 */
@Stateless
@LocalBean
public class SurveyAssignmentManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private ExpectedAnswerManager responseManager;
	@EJB
	private QuestionManager questionManager;
	@EJB
	private ExpectedAnswerManager expectedAnswerManager;
	@EJB
	private UnexpectedAnswerManager unexpectedAnswerManager;
	@EJB
	private UncodedExpectedAnswerManager uncodedExpectedAnswerManager;

	/**
	 * Default constructor.
	 */
	public SurveyAssignmentManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findLimesurveySurveyIdsByOaiRecordId(String oaiRecordId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select lsa.limesurvey_survey_id from harvesting.thesis_assignments ta, harvesting.survey_assignments lsa "
						+ "where ta.id = lsa.thesis_assignment_id and ta.oai_record_id = '" + oaiRecordId + "'");
		List<Object> objects = query.getResultList();
		List<Integer> alreadyPresentlimesurveySurveyIds = new ArrayList<>();
		for (Object object : objects) {
			alreadyPresentlimesurveySurveyIds.add(Integer.parseInt(object.toString()));
		}
		return alreadyPresentlimesurveySurveyIds;
	}

	public SurveyAssignment createOneSurveyAssignment(int limesurveySurveyId, String limesurveySurveyAccessToken,
			ThesisAssignment thesisAssignment) throws Exception {
		SurveyAssignment SurveyAssignment = new SurveyAssignment();
		SurveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		SurveyAssignment.setLimesurveySurveyToken(limesurveySurveyAccessToken);
		SurveyAssignment.setThesisAssignment(thesisAssignment);
		SurveyAssignment.setIsDispatched(false);
		SurveyAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		SurveyAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (SurveyAssignment) daoManager.createOne(SurveyAssignment);
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAssignment> findAllSurveyAssignmentsByThesisAssignementId(int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(SurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId, null);
		return SurveyAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAssignment> findAllUndispatchedSurveyAssignmentsByThesisAssignementId(int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(SurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId + " and o.isDispatched = false", null);
		return SurveyAssignments;
	}

	public SurveyAssignment findOneSurveyAssignmentById(int id) throws Exception {
		return (SurveyAssignment) daoManager.findOneById(SurveyAssignment.class, id);
	}

	public SurveyAssignment updateOneSurveyAssignment(SurveyAssignment surveyAssignment,
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
		return (SurveyAssignment) daoManager.updateOne(surveyAssignment);
	}

	public SurveyAssignment updateOneSurveyAssignmentById(int id, ThesisAssignment thesisAssignment,
			Integer limesurveySurveyId, String limesurveySurveyToken, Boolean isDispatched) throws Exception {
		SurveyAssignment surveyAssignment = findOneSurveyAssignmentById(id);
		if (surveyAssignment == null)
			throw new Exception("La Asignación de Encuesta especificada no se encuentra registrada.");
		return updateOneSurveyAssignment(surveyAssignment, thesisAssignment, limesurveySurveyId, limesurveySurveyToken,
				isDispatched);
	}

	public SurveyAssignment updateOneSurveyAssignmentAsDispatched(SurveyAssignment surveyAssignment) throws Exception {
		return updateOneSurveyAssignment(surveyAssignment, null, null, null, true);
	}

	public SurveyAssignment updateOneSurveyAssignmentAsDispatchedById(int id) throws Exception {
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
	public SurveyAssignment dispatchSurvey(SurveyAssignment surveyAssignment) throws Exception {
		JsonObject response = LimesurveyService.exportResponse(surveyAssignment.getLimesurveySurveyId(),
				surveyAssignment.getLimesurveySurveyToken());
		
		List<ExpectedAnswer> expectedAnswers = new ArrayList<>();
		List<UncodedExpectedAnswer> uncodedExpectedAnswers = new ArrayList<>();
		List<UnexpectedAnswer> unexpectedAnswers = new ArrayList<>();

		List<Question> questions = questionManager
				.findAllQuestionsByLimesurveySurveyId(surveyAssignment.getLimesurveySurveyId());
		HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtos = LimesurveyService
				.listQuestions(surveyAssignment.getLimesurveySurveyId());

		HashMap<Integer, LimesurveyQuestionPropertiesDto> limesurveyQuestionPropertiesDtos = new HashMap<>();
		for (Integer integer : limesurveyQuestionDtos.values().stream().map(arg0 -> arg0.getId())
				.collect(Collectors.toList())) {
			limesurveyQuestionPropertiesDtos.put(integer, LimesurveyService.getQuestionProperties(integer));
		}

		for (Question question : questions) {
			JsonElement element = response.get(question.getLimesurveyQuestionTitle() + "[other]");
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				unexpectedAnswers.add(unexpectedAnswerManager.createOneUnexpectedAnswer(question, element.getAsString(),
						surveyAssignment));
				continue;
			}

			element = response.get(question.getLimesurveyQuestionTitle());
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				/**
				 * Within the Answer Options map of a LimesurveyQuestionPropertiesDto remains the "Expected answers" keyed
				 * by its code, which is actually what is returned as response of the question, for instance
				 * element.get("tamanoEmpresa") would return either "micro", "peque" or another 5 character code of the
				 * expected answer which again if exists must be recorded in the answer options map.
				 * When a response is uncoded for example element.get("ingresoBrutoEmpresa"), the value is expected to be a 
				 * numeric string but as not every number would be recorded as an expected answer or answer option the
				 * answer optionsMap won't contain this value making it an uncoded answer but that is expected.
				 * This case can happen when freetext fields are declared as questions in limesurvey.
				 */
				if (limesurveyQuestionPropertiesDtos.get(question.getLimesurveyQuestionId()).getAnswerOptions()
						.get(element.getAsString()) == null) {
					uncodedExpectedAnswers.add(uncodedExpectedAnswerManager.createOneUncodedExpectedAnswer(question,
							element.getAsString(), surveyAssignment));
					continue;
				}
				expectedAnswers.add(expectedAnswerManager.createOneExpectedAnswer(question, element.getAsString(),
						surveyAssignment));
				continue;
			}

			LimesurveyQuestionPropertiesDto parentQuestionPropertiesDto = limesurveyQuestionPropertiesDtos
					.get(limesurveyQuestionDtos.get(question.getLimesurveyQuestionTitle()).getParentQid());
			if (parentQuestionPropertiesDto == null)
				continue;
			element = response
					.get(parentQuestionPropertiesDto.getTitle() + "[" + question.getLimesurveyQuestionTitle() + "]");
			if (element != null) {
				if (element.isJsonNull())
					continue;
				if (element.getAsString().isBlank())
					continue;
				expectedAnswers.add(expectedAnswerManager.createOneExpectedAnswer(question, element.getAsString(),
						surveyAssignment));
				continue;
			}
		}

		if (expectedAnswers.isEmpty() && unexpectedAnswers.isEmpty())
			throw new Exception("Ha ocurrido un error en el proceso de despacho.");
		surveyAssignment = updateOneSurveyAssignmentAsDispatched(surveyAssignment);

		surveyAssignment.setExpectedAnswers(expectedAnswers);
		surveyAssignment.setUncodedExpectedAnswers(uncodedExpectedAnswers);
		surveyAssignment.setUnexpectedAnswers(unexpectedAnswers);

		return surveyAssignment;
	}
}
