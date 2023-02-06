package swimsEJB.model.analytics.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.EJB;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionPropertiesDto;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsEJB.model.harvesting.services.LimesurveyService;

import swimsEJB.model.analytics.entities.views.Label;

/**
 * Session Bean implementation class LabelManager
 */
@Stateless
@LocalBean
public class LabelManager {

	@EJB
	private StudyVariableManager studyVariableManager;
	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public LabelManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> findAllLabelsByStudyVariableId(String studyVariableId) throws Exception {

		StudyVariable studyVariable = studyVariableManager.findOneStudyVariableById(studyVariableId);
		if (studyVariable == null)
			throw new Exception("La variable de estudio especificada no existe.");

		List<HashMap<String, String>> labelsMap = new ArrayList<>();

		List<Label> labels = daoManager.findManyWhere(Label.class, "o.studyVariableId = '" + studyVariableId + "'",
				null);
		for (Label label : labels) {
			HashMap<String, String> map = new HashMap<>();
			map.put("id", label.getId().toString());
			map.put("label", label.getLabel());
			labelsMap.add(map);
		}

		List<Integer> limesurveySurveyIds = studyVariable.getLimesurveyQuestions().stream()
				.map(arg0 -> arg0.getLimesurveySurveyId()).distinct().collect(Collectors.toList());

		String limesurveySessionKey = limesurveySurveyIds.isEmpty() ? null : LimesurveyService.getSessionKey();

		HashMap<Integer, HashMap<String, LimesurveyQuestionDto>> limesurveySurveysMap = new HashMap<>();
		for (Integer limesurveySurveyId : limesurveySurveyIds) {
			limesurveySurveysMap.put(limesurveySurveyId,
					LimesurveyService.listQuestions(limesurveySessionKey, limesurveySurveyId));
		}

		if (studyVariable.getLimesurveyQuestions().size() > 1) {
			for (LimesurveyQuestion limesurveyQuestion : studyVariable.getLimesurveyQuestions()) {
				LimesurveyQuestionDto limesurveyQuestionDto = limesurveySurveysMap
						.get(limesurveyQuestion.getLimesurveySurveyId()).get(limesurveyQuestion.getId());
				if (limesurveyQuestionDto.getParentQid() == 0)
					continue;
				HashMap<String, String> map = new HashMap<>();
				map.put("id", limesurveyQuestion.getLimesurveyQuestionTitle());
				map.put("label", limesurveyQuestionDto.getQuestion());
				labelsMap.add(map);
			}
			LimesurveyService.releaseSessionKey(limesurveySessionKey);
			return labelsMap;
		}

		for (LimesurveyQuestion limesurveyQuestion : studyVariable.getLimesurveyQuestions()) {
			LimesurveyQuestionDto limesurveyQuestionDto = limesurveySurveysMap
					.get(limesurveyQuestion.getLimesurveySurveyId()).get(limesurveyQuestion.getId());
			LimesurveyQuestionPropertiesDto limesurveyQuestionPropertiesDto = LimesurveyService
					.getQuestionProperties(limesurveySessionKey, limesurveyQuestionDto.getLimesurveyQuestionId());
			for (String key : limesurveyQuestionPropertiesDto.getAnswerOptions().keySet()) {
				HashMap<String, String> map = new HashMap<>();
				map.put("id", key);
				map.put("label", limesurveyQuestionPropertiesDto.getAnswerOptions().get(key));
				labelsMap.add(map);
			}
		}
		LimesurveyService.releaseSessionKey(limesurveySessionKey);
		return labelsMap;
	}
}
