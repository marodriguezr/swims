package swimsEJB.model.analytics.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.analytics.entities.views.CompoundUnexpectedLimesurveyAnswer;
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
public class CompoundUnexpectedAnswerManager {

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
	public CompoundUnexpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> findAllCompoundUnexpectedAnswersByStudyVariableClassId(
			String studyVariableClassId) throws Exception {
		List<StudyVariable> studyVariables = studyVariableClassId == null ? studyVariableManager.findAllStudyVariables()
				: studyVariableManager.findAllStudyVariablesByStudyVariableClassId(studyVariableClassId);
		List<ThesisRecord> thesisRecords = thesisRecordManager.findAllThesisRecords();
		int maxAnswerCountForEachStudyVariable = 0;

		HashMap<String, HashMap<StudyVariable, List<CompoundUnexpectedLimesurveyAnswer>>> thesisMap = new HashMap<>();
		for (ThesisRecord thesisRecord : thesisRecords) {
			HashMap<StudyVariable, List<CompoundUnexpectedLimesurveyAnswer>> studyVariablesMap = new HashMap<>();
			List<CompoundUnexpectedLimesurveyAnswer> compoundLimesurveyAnswers = daoManager.findManyWhere(
					CompoundUnexpectedLimesurveyAnswer.class,
					"o.thesisRecordId = '" + thesisRecord.getId() + (studyVariableClassId == null ? "'"
							: ("' AND o.studyVariableClassId = '" + studyVariableClassId + "'")),
					null);
			for (StudyVariable studyVariable : studyVariables) {
				List<CompoundUnexpectedLimesurveyAnswer> studyVariableCompoundAnswers = compoundLimesurveyAnswers
						.stream().filter(t -> t.getStudyVariableId().equals(studyVariable.getId()))
						.collect(Collectors.toList());
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
				HashMap<StudyVariable, List<CompoundUnexpectedLimesurveyAnswer>> studyVariableMap = (HashMap<StudyVariable, List<CompoundUnexpectedLimesurveyAnswer>>) thesisMap
						.values().toArray()[i];
				for (StudyVariable studyVariable : studyVariableMap.keySet()) {
					if (studyVariableMap.get(studyVariable).size() <= j)
						continue;

					map.put(studyVariable.getId(), studyVariableMap.get(studyVariable).get(j).getAnswer());
				}
				if (map.values().isEmpty())
					continue;
				map.put("thesisRecordId", thesisMap.keySet().toArray(new String[thesisMap.keySet().size()])[i]);
				maps.add(map);
			}
		}

		return maps;
	}

	public List<HashMap<String, Object>> findAllCompoundUnexpectedAnswers() throws Exception {
		return findAllCompoundUnexpectedAnswersByStudyVariableClassId(null);
	}
}
