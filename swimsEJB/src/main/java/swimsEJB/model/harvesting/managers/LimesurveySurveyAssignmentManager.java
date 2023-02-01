package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.entities.LimesurveyAnswer;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.entities.views.LimesurveySurveyIdsInnerThesisAssignment;
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
	public List<Integer> findLimesurveySurveyIdsByThesisRecordId(String thesisRecordId) {
		List<LimesurveySurveyIdsInnerThesisAssignment> limesurveySurveyIdsInnerThesisAssignments = daoManager
				.findManyWhere(LimesurveySurveyIdsInnerThesisAssignment.class,
						"o.thesisRecordId = '" + thesisRecordId + "'", null);
		return limesurveySurveyIdsInnerThesisAssignments.stream().map(arg0 -> arg0.getLimesurveySurveyId())
				.collect(Collectors.toList());
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
		String sessionKey = LimesurveyService.getSessionKey();

		HashMap<String, String> responsesMap = LimesurveyService.exportResponse(sessionKey,
				surveyAssignment.getLimesurveySurveyId(), surveyAssignment.getLimesurveySurveyToken(), true);

		List<LimesurveyAnswer> expectedAnswers = new ArrayList<>();
		List<LimesurveyUnexpectedAnswer> unexpectedAnswers = new ArrayList<>();

		HashMap<String, LimesurveyQuestion> questionsMap = new HashMap<>();
		for (LimesurveyQuestion question : limesurveyQuestionManager
				.findAllQuestionsByLimesurveySurveyId(surveyAssignment.getLimesurveySurveyId())) {
			questionsMap.put(question.getId(), question);
		}
		HashMap<String, LimesurveyQuestionDto> limesurveyQuestionDtosMap = LimesurveyService.listQuestions(sessionKey,
				surveyAssignment.getLimesurveySurveyId());

		for (LimesurveyQuestion question : questionsMap.values()) {
			String answer = responsesMap.get(question.getLimesurveyQuestionTitle() + "[other]");
			if (answer != null) {
				if (answer.isBlank())
					continue;
				unexpectedAnswers.add(limesurveyUnexpectedAnswerManager.createOneLimesurveyUnexpectedAnswer(question,
						answer, surveyAssignment));
				continue;
			}

			answer = responsesMap.get(question.getLimesurveyQuestionTitle());
			if (answer != null) {
				if (answer.isBlank())
					continue;
				expectedAnswers.add(limesurveyExpectedAnswerManager.createOneLimesurveyExpectedAnswer(question, answer,
						surveyAssignment));
				continue;
			}

			LimesurveyQuestionDto currentQuestionLimesurveyQuestionDto = limesurveyQuestionDtosMap
					.get(question.getId());
			LimesurveyQuestionDto parentLimesurveyQuestionDto = limesurveyQuestionDtosMap.values().stream()
					.filter(t -> t.getLimesurveyQuestionId() == currentQuestionLimesurveyQuestionDto.getParentQid())
					.findFirst().orElse(null);
			if (parentLimesurveyQuestionDto == null)
				continue;
			LimesurveyQuestion parentQuestion = questionsMap
					.get(currentQuestionLimesurveyQuestionDto.getSid() + "0" + parentLimesurveyQuestionDto.getTitle());
			if (parentQuestion == null)
				continue;
			answer = responsesMap.get(
					parentQuestion.getLimesurveyQuestionTitle() + "[" + question.getLimesurveyQuestionTitle() + "]");
			if (answer != null) {
				if (answer.isBlank())
					continue;
				expectedAnswers.add(limesurveyExpectedAnswerManager.createOneLimesurveyExpectedAnswer(question, answer,
						surveyAssignment));
				continue;
			}
		}

		if (expectedAnswers.isEmpty() && unexpectedAnswers.isEmpty())
			throw new Exception("Ha ocurrido un error en el proceso de despacho.");
		surveyAssignment = updateOneSurveyAssignmentAsDispatched(surveyAssignment);

		surveyAssignment.setLimesurveyAnswers(expectedAnswers);
		surveyAssignment.setLimesurveyUnexpectedAnswers(unexpectedAnswers);

		LimesurveyService.releaseSessionKey(sessionKey);
		return surveyAssignment;
	}

}
