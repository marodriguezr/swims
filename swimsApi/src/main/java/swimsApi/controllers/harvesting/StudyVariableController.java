package swimsApi.controllers.harvesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.managers.StudyVariableManager;

@RequestScoped
@Path("harvesting/study-variables")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudyVariableController {
	@EJB
	private StudyVariableManager studyVariableManager;

	@GET
	@Path(value = "")
	public List<HashMap<String, Object>> findStudyVariables() {
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		List<StudyVariable> studyVariables = studyVariableManager.findAllStudyVariables();
		for (StudyVariable studyVariable : studyVariables) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", studyVariable.getId());
			hashMap.put("name", studyVariable.getName());
			hashMap.put("is_numeric_continuous", studyVariable.getIsNumericContinuous());
			hashMap.put("is_numeric_discrete", studyVariable.getIsNumericDiscrete());
			hashMap.put("is_categorical_nominal", studyVariable.getIsCategoricalNominal());
			hashMap.put("is_categorical_ordinal", studyVariable.getIsCategoricalOrdinal());
			hashMap.put("study_variable_class_id", studyVariable.getStudyVariableClass().getId());

			hashMaps.add(hashMap);
		}
		return hashMaps;
	}
}
