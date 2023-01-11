package swimsApi.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;

@RequestScoped
@Path("test")
@Produces("application/json")
@Consumes("application/json")
public class TestController {

	@EJB
	private ThesisRecordManager thesisRecordManager;

	@GET
	@Path(value = "test2")
	public List<HashMap<String, Object>> test2() {
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		List<ThesisRecord> oaiRecords = thesisRecordManager.findAllThesisRecords();
		for (ThesisRecord oaiRecord : oaiRecords) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", oaiRecord.getId());
			hashMap.put("contributor", oaiRecord.getContributor());
			hashMap.put("author", oaiRecord.getCreator());
			hashMap.put("subject", oaiRecord.getSubject());
			hashMap.put("issue_date", oaiRecord.getInferredIssueDate());
			hashMap.put("description", oaiRecord.getDescription());

			hashMaps.add(hashMap);
		}
		return hashMaps;
	}
}
