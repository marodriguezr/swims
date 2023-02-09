package swimsEJB.model.analytics.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.lang3.math.NumberUtils;

import swimsEJB.model.analytics.entities.views.CompoundAnswer;
import swimsEJB.model.analytics.entities.views.CompoundLimesurveyAnswer;
import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.StudyVariableClassManager;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;
import swimsEJB.utilities.StringHelpers;

import javax.ejb.EJB;

/**
 * Session Bean implementation class AnalyticsManager
 */
@Stateless
@LocalBean
public class CompoundAnswerManager {

	@EJB
	private StudyVariableClassManager studyVariableClassManager;
	@EJB
	private StudyVariableManager studyVariableManager;
	@EJB
	private ThesisRecordManager thesisRecordManager;
	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public CompoundAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> findCompoundAnswers(String studyVariableClassId) throws Exception {
		List<StudyVariable> studyVariables = studyVariableClassId == null ? studyVariableManager.findAllStudyVariables()
				: studyVariableManager.findAllStudyVariablesByStudyVariableClassId(studyVariableClassId);
		List<ThesisRecord> thesisRecords = thesisRecordManager.findAllThesisRecords();
		int maxAnswerCountForEachStudyVariable = 0;

		/**
		 * Possible performance improvement replace findManyWhere with Native Queries at
		 * the expense of Class definition and overall portability
		 */
		List<CompoundLimesurveyAnswer> compoundLimesurveyAnswers = studyVariableClassId == null
				? daoManager.findAll(CompoundLimesurveyAnswer.class)
				: daoManager.findManyWhere(CompoundLimesurveyAnswer.class,
						"o.studyVariableClassId = '" + studyVariableClassId + "'", null);
		List<CompoundAnswer> compoundAnswers = studyVariableClassId == null ? daoManager.findAll(CompoundAnswer.class)
				: daoManager.findManyWhere(CompoundAnswer.class,
						"o.studyVariableClassId = '" + studyVariableClassId + "'", null);

		HashMap<String, HashMap<StudyVariable, List<CompoundLimesurveyAnswer>>> thesisMap = new HashMap<>();
		for (ThesisRecord thesisRecord : thesisRecords) {
			HashMap<StudyVariable, List<CompoundLimesurveyAnswer>> studyVariablesMap = new HashMap<>();
			List<CompoundLimesurveyAnswer> thesisCompoundLimesurveyAnswers = compoundLimesurveyAnswers.stream()
					.filter(t -> t.getThesisRecordId().equals(thesisRecord.getId())).collect(Collectors.toList());
			List<CompoundAnswer> thesisCompoundAnswers = compoundAnswers.stream()
					.filter(t -> t.getThesisRecordId().equals(thesisRecord.getId())).collect(Collectors.toList());
			for (StudyVariable studyVariable : studyVariables) {
				List<CompoundLimesurveyAnswer> studyVariableCompoundAnswers = thesisCompoundLimesurveyAnswers.stream()
						.filter(t -> t.getStudyVariableId().equals(studyVariable.getId())).collect(Collectors.toList());
				studyVariableCompoundAnswers.addAll(thesisCompoundAnswers.stream()
						.filter(t -> t.getStudyVariableId().equals(studyVariable.getId())).map(t -> {
							CompoundLimesurveyAnswer compoundLimesurveyAnswer = new CompoundLimesurveyAnswer();
							compoundLimesurveyAnswer.setAnswer(t.getExpectedAnswerId().toString());
							compoundLimesurveyAnswer.setIsCategoricalNominal(t.getIsCategoricalNominal());
							compoundLimesurveyAnswer.setIsCategoricalOrdinal(t.getIsCategoricalOrdinal());
							compoundLimesurveyAnswer.setIsNumericContinuous(t.getIsNumericContinuous());
							compoundLimesurveyAnswer.setIsNumericDiscrete(t.getIsNumericDiscrete());
							compoundLimesurveyAnswer.setLimesurveyQuestionTitle(t.getStudyVariableId());
							compoundLimesurveyAnswer.setStudyVariableClassId(t.getStudyVariableClassId());
							compoundLimesurveyAnswer.setStudyVariableId(t.getStudyVariableId());
							compoundLimesurveyAnswer.setThesisRecordId(t.getThesisRecordId());
							return compoundLimesurveyAnswer;
						}).collect(Collectors.toList()));
				if (studyVariableCompoundAnswers.isEmpty())
					continue;
				studyVariablesMap.put(studyVariable, studyVariableCompoundAnswers);
				if (studyVariableCompoundAnswers.size() < maxAnswerCountForEachStudyVariable)
					continue;
				maxAnswerCountForEachStudyVariable = studyVariableCompoundAnswers.size();
			}
			if (studyVariablesMap.values().isEmpty())
				continue;
			thesisMap.put(thesisRecord.getId(), studyVariablesMap);
		}

		List<HashMap<String, Object>> maps = new ArrayList<>();
		for (int i = 0; i < thesisMap.values().size(); i++) {
			for (int j = 0; j < maxAnswerCountForEachStudyVariable; j++) {
				HashMap<String, Object> map = new HashMap<>();
				HashMap<StudyVariable, List<CompoundLimesurveyAnswer>> studyVariableMap = (HashMap<StudyVariable, List<CompoundLimesurveyAnswer>>) thesisMap
						.values().toArray()[i];
				for (StudyVariable studyVariable : studyVariableMap.keySet()) {
					if (studyVariableMap.get(studyVariable).size() <= j)
						continue;
					String answer = studyVariable.getLimesurveyQuestions().size() > 1
							? studyVariableMap.get(studyVariable).get(j).getLimesurveyQuestionTitle()
							: studyVariableMap.get(studyVariable).get(j).getAnswer();
					/**
					 * Expecting numbers with comma as decimal separator
					 */
					if (studyVariable.getIsNumericContinuous()) {
						answer = answer.replace(".", "");
						answer = answer.replace(',', '.');
					}
					map.put(StringHelpers.cammelCaseToSnakeCase(studyVariable.getId()),
							NumberUtils.isCreatable(answer) && !studyVariable.getIsCategoricalNominal()
									&& !studyVariable.getIsCategoricalOrdinal()
											? studyVariable.getIsNumericContinuous() ? Float.parseFloat(answer)
													: Integer.parseInt(answer)
											: answer);
				}
				if (map.values().isEmpty())
					continue;
				map.put("thesis_record_id", thesisMap.keySet().toArray(new String[thesisMap.keySet().size()])[i]);
				maps.add(map);
			}
		}

		return maps;
	}

	public List<HashMap<String, Object>> findAllCompoundAnswers() throws Exception {
		return findCompoundAnswers(null);
	}
}
