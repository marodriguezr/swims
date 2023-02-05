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

import swimsEJB.model.analytics.managers.CompoundUnexpectedAnswerManager;

@RequestScoped
@Path("analytics/compound-unexpected-answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompoundUnexpectedAnswersController {
	@EJB
	private CompoundUnexpectedAnswerManager compoundUnexpectedAnswerManager;

	@GET
	@Path(value = "")
	public List<HashMap<String, Object>> findAllCompoundAnswers() {
		try {
			return compoundUnexpectedAnswerManager.findAllCompoundUnexpectedAnswers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
