package swimsEJB.model.analytics.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.lang3.math.NumberUtils;

import swimsEJB.model.analytics.entities.views.CompoundAnswer;
import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.StudyVariableClassManager;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;

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
	public List<HashMap<String, Object>> findAllCompoundAnswersByStudyVariableClassId(String studyVariableClassId)
			throws Exception {
		List<StudyVariable> studyVariables = studyVariableClassId == null ? studyVariableManager.findAllStudyVariables()
				: studyVariableManager.findAllStudyVariablesByStudyVariableClassId(studyVariableClassId);
		List<ThesisRecord> thesisRecords = thesisRecordManager.findAllThesisRecords();
		int maxAnswerCountForEachStudyVariable = 0;

		HashMap<String, HashMap<StudyVariable, List<CompoundAnswer>>> thesisMap = new HashMap<>();
		for (ThesisRecord thesisRecord : thesisRecords) {
			HashMap<StudyVariable, List<CompoundAnswer>> studyVariablesMap = new HashMap<>();
			List<CompoundAnswer> compoundAnswers = daoManager.findManyWhere(CompoundAnswer.class,
					"o.thesisRecordId = '" + thesisRecord.getId() + (studyVariableClassId == null ? "'"
							: ("' AND o.studyVariableClassId = '" + studyVariableClassId + "'")),
					null);
			for (StudyVariable studyVariable : studyVariables) {
				List<CompoundAnswer> studyVariableCompoundAnswers = compoundAnswers.stream()
						.filter(t -> t.getStudyVariableId().equals(studyVariable.getId())).collect(Collectors.toList());
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
				HashMap<StudyVariable, List<CompoundAnswer>> studyVariableMap = (HashMap<StudyVariable, List<CompoundAnswer>>) thesisMap
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
					map.put(studyVariable.getId(),
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
		return findAllCompoundAnswersByStudyVariableClassId(null);
	}
}
