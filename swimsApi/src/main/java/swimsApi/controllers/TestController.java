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

import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.managers.OaiRecordManager;

@RequestScoped
@Path("test")
@Produces("application/json")
@Consumes("application/json")
public class TestController {

	@EJB
	private OaiRecordManager oaiRecordManager;

	@GET
	@Path(value = "test2")
	public List<HashMap<String, Object>> test2() {
		List<HashMap<String, Object>> hashMaps = new ArrayList<>();
		List<OaiRecord> oaiRecords = oaiRecordManager.findAllOaiRecords();
		for (OaiRecord oaiRecord : oaiRecords) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", oaiRecord.getId());
			hashMap.put("contributor", oaiRecord.getContributor());
			hashMap.put("author", oaiRecord.getCreator());

			hashMaps.add(hashMap);
		}
		return hashMaps;
	}
}
