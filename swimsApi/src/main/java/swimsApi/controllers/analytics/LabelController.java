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
import javax.ws.rs.PathParam;

import swimsEJB.model.analytics.managers.LabelManager;

@RequestScoped
@Path("analytics/labels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LabelController {
	@EJB
	private LabelManager labelManager;

	@GET
	@Path("{studyVariableId}")
	public List<HashMap<String, String>> findLabels(@PathParam("studyVariableId") String studyVariableId) {
		try {

			return labelManager.findAllLabelsByStudyVariableId(studyVariableId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
