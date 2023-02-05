package swimsApi.controllers.analytics;

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
import javax.ws.rs.QueryParam;

import swimsEJB.model.analytics.managers.CompoundAnswerManager;

@RequestScoped
@Path("analytics/compound-answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompoundAnswersController {
	@EJB
	private CompoundAnswerManager compoundAnswerManager;

	@GET
	@Path(value = "")
	public List<HashMap<String, Object>> findCompoundAnswers(
			@QueryParam("study-variable-class-id") String studyVariableClassId) {
		try {
			return compoundAnswerManager.findAllCompoundAnswersByStudyVariableClassId(studyVariableClassId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
