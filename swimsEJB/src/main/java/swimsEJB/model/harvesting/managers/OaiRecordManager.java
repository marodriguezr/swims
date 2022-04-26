package swimsEJB.model.harvesting.managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class OaiRecordManager
 */
@Stateless
@LocalBean
public class OaiRecordManager {

	/**
	 * Default constructor.
	 */
	public OaiRecordManager() {
		// TODO Auto-generated constructor stub
	}

	public void findAllCISICOaiRecords() throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from=2011-01-01&until=2021-01-01&set=col_123456789_40"))
				.build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		System.out.println(response.body());
	}
}
















