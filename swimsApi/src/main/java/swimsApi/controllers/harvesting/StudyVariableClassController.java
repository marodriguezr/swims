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

import swimsEJB.model.harvesting.entities.StudyVariableClass;
import swimsEJB.model.harvesting.managers.StudyVariableClassManager;

@RequestScoped
@Path("harvesting/study-variable-classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudyVariableClassController {
	@EJB
	private StudyVariableClassManager studyVariableClassManager;

	@GET
	@Path(value = "")
	public List<HashMap<String, Object>> findStudyVariableClasses() {
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		List<StudyVariableClass> studyVariableClasses = studyVariableClassManager.findAllStudyVariableClasses();
		for (StudyVariableClass studyVariableClass : studyVariableClasses) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", studyVariableClass.getId());
			hashMap.put("name", studyVariableClass.getName());

			hashMaps.add(hashMap);
		}
		return hashMaps;
	}
}
