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

import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;

@RequestScoped
@Path("harvesting/thesis-records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThesisRecordController {
	@EJB
	private ThesisRecordManager thesisRecordManager;

	@GET
	@Path(value = "")
	public List<HashMap<String, Object>> findThesisRecords() {
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		List<ThesisRecord> oaiRecords = thesisRecordManager.findAllThesisRecords();
		for (ThesisRecord oaiRecord : oaiRecords) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", oaiRecord.getId());
			hashMap.put("url", oaiRecord.getUrl());
			hashMap.put("title", oaiRecord.getTitle());
			hashMap.put("author", oaiRecord.getCreator());
			hashMap.put("subject", oaiRecord.getSubject());
			hashMap.put("description", oaiRecord.getDescription());
			hashMap.put("contributor", oaiRecord.getContributor());
			hashMap.put("issue_date", oaiRecord.getInferredIssueDate());
			hashMap.put("creation_date", oaiRecord.getInferredCreationDate());
			hashMap.put("thesis_set_id", oaiRecord.getThesisSet().getId());

			hashMaps.add(hashMap);
		}
		return hashMaps;
	}
}
