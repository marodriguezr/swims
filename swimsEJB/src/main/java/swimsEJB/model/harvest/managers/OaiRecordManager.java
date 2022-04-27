package swimsEJB.model.harvest.managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import swimsEJB.utilities.StringHelpers;

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

	public void findAllCISICOaiRecords()
			throws IOException, InterruptedException, ParserConfigurationException, SAXException {
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from=2011-01-01&until=2021-01-01&set=col_123456789_40"))
				.build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		String listRecords = StringUtils.substringBetween(response.body(), "<ListRecords>", "</ListRecords>");

		int indexOfRecordOpeningTag = listRecords.indexOf("<oai_dc:dc");
		int indexOfRecordClosingTag = listRecords.indexOf("</oai_dc:dc>");
		String record = listRecords.substring(indexOfRecordOpeningTag + "<oai_dc:dc".length(), indexOfRecordClosingTag);
		listRecords = StringHelpers.removeSubstring(listRecords, indexOfRecordOpeningTag,
				indexOfRecordClosingTag + "</oai_dc:dc>".length());
		System.out.println(record);
		/*
		 * while (listRecords.length() > 0) {
		 * 
		 * indexOfRecordOpeningTag = listRecords.indexOf("<record>");
		 * indexOfRecordClosingTag = listRecords.indexOf("</record>"); record =
		 * listRecords.substring(indexOfRecordOpeningTag + "<record>".length(),
		 * indexOfRecordClosingTag); listRecords =
		 * StringHelpers.removeSubstring(listRecords, indexOfRecordOpeningTag,
		 * indexOfRecordClosingTag + "</record>".length()); }
		 */
	}
}
