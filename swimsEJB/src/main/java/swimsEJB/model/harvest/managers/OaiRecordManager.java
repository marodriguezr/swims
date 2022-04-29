package swimsEJB.model.harvest.managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import swimsEJB.model.harvest.dtos.OaiRecordDto;
import swimsEJB.utilities.StringHelpers;

/**
 * Session Bean implementation class OaiRecordManager
 */
@Stateless
@LocalBean
public class OaiRecordManager {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Default constructor.
	 */
	public OaiRecordManager() {
		// TODO Auto-generated constructor stub
	}
	
	public LinkedList<OaiRecordDto> xmlStringToOaiRecordDtos() {
		return null;
	}
	
	public String findAllOaiRecords() {
		String OAI_URI = "http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from=2011-01-01&until=2021-01-01&set=col_123456789_40";
		return "";
	}

	public LinkedList<OaiRecordDto> findAllCISICOaiRecords() throws ParseException, IOException, InterruptedException {

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from=2011-01-01&until=2021-01-01&set=col_123456789_40"))
				.build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		String LIST_RECORDS_OT = "<ListRecords>";
		String LIST_RECORDS_CT = "</ListRecords>";

		String OAI_DC_OT = "<oai_dc:dc";
		String OAI_DC_CT = "</oai_dc:dc>";
		int indexOfOAI_DC_OT;
		int indexOfOAI_DC_CT;

		String DC_TITLE_OT = "<dc:title>";
		String DC_TITLE_CT = "</dc:title>";

		String DC_CREATOR_OT = "<dc:creator>";
		String DC_CREATOR_CT = "</dc:creator>";

		String DC_SUBJECT_OT = "<dc:subject>";
		String DC_SUBJECT_CT = "</dc:subject>";

		String DC_DESCRIPTION_OT = "<dc:description>";
		String DC_DESCRIPTION_CT = "</dc:description>";

		String DC_PUBLISHER_OT = "<dc:publisher>";
		String DC_PUBLISHER_CT = "</dc:publisher>";

		String DC_CONTRIBUTOR_OT = "<dc:contributor>";
		String DC_CONTRIBUTOR_CT = "</dc:contributor>";

		String DC_DATE_OT = "<dc:date>";
		String DC_DATE_CT = "</dc:date>";

		String DC_TYPE_OT = "<dc:type>";
		String DC_TYPE_CT = "</dc:type>";

		String DC_FORMAT_OT = "<dc:format>";
		String DC_FORMAT_CT = "</dc:format>";

		String DC_IDENTIFIER_OT = "<dc:identifier>";
		String DC_IDENTIFIER_CT = "</dc:identifier>";

		String DC_SOURCE_OT = "<dc:source>";
		String DC_SOURCE_CT = "</dc:source>";

		String DC_LANGUAGE_OT = "<dc:language>";
		String DC_LANGUAGE_CT = "</dc:language>";

		String DC_RELATION_OT = "<dc:relation>";
		String DC_RELATION_CT = "</dc:relation>";

		String DC_COVERAGE_OT = "<dc:coverage>";
		String DC_COVERAGE_CT = "</dc:coverage>";

		String DC_RIGHTS_OT = "<dc:rights>";
		String DC_RIGHTS_CT = "</dc:rights>";

		String listRecords = response.body().substring(
				response.body().indexOf(LIST_RECORDS_OT) + LIST_RECORDS_OT.length(),
				response.body().indexOf(LIST_RECORDS_CT));
		String oaiDc;
		OaiRecordDto oaiRecordDto;
		LinkedList<OaiRecordDto> oaiRecordDtos = new LinkedList<OaiRecordDto>();

		while (true) {
			indexOfOAI_DC_OT = listRecords.indexOf(OAI_DC_OT);
			indexOfOAI_DC_CT = listRecords.indexOf(OAI_DC_CT);

			if (indexOfOAI_DC_OT == -1 || indexOfOAI_DC_CT == -1)
				break;
			oaiDc = listRecords.substring(indexOfOAI_DC_OT + OAI_DC_OT.length(), indexOfOAI_DC_CT);
			listRecords = StringHelpers.removeSubstring(listRecords, 0, indexOfOAI_DC_CT + OAI_DC_CT.length());
			oaiRecordDto = new OaiRecordDto();

			oaiRecordDto.setTitles(extractStringBetweenManyXMLTags(oaiDc, DC_TITLE_OT, DC_TITLE_CT));
			oaiRecordDto.setCreators(extractStringBetweenManyXMLTags(oaiDc, DC_CREATOR_OT, DC_CREATOR_CT));
			oaiRecordDto.setSubjects(extractStringBetweenManyXMLTags(oaiDc, DC_SUBJECT_OT, DC_SUBJECT_CT));
			oaiRecordDto.setDescriptions(extractStringBetweenManyXMLTags(oaiDc, DC_DESCRIPTION_OT, DC_DESCRIPTION_CT));
			oaiRecordDto.setPublishers(extractStringBetweenManyXMLTags(oaiDc, DC_PUBLISHER_OT, DC_PUBLISHER_CT));
			oaiRecordDto.setContributors(extractStringBetweenManyXMLTags(oaiDc, DC_CONTRIBUTOR_OT, DC_CONTRIBUTOR_CT));
			oaiRecordDto.setDates(extractDateBetweenManyXMLTags(oaiDc, DC_DATE_OT, DC_DATE_CT));
			oaiRecordDto.setTypes(extractStringBetweenManyXMLTags(oaiDc, DC_TYPE_OT, DC_TYPE_CT));
			oaiRecordDto.setFormats(extractStringBetweenManyXMLTags(oaiDc, DC_FORMAT_OT, DC_FORMAT_CT));
			oaiRecordDto.setIdentifiers(extractStringBetweenManyXMLTags(oaiDc, DC_IDENTIFIER_OT, DC_IDENTIFIER_CT));
			oaiRecordDto.setSources(extractStringBetweenManyXMLTags(oaiDc, DC_SOURCE_OT, DC_SOURCE_CT));
			oaiRecordDto.setLanguages(extractStringBetweenManyXMLTags(oaiDc, DC_LANGUAGE_OT, DC_LANGUAGE_CT));
			oaiRecordDto.setRelations(extractStringBetweenManyXMLTags(oaiDc, DC_RELATION_OT, DC_RELATION_CT));
			oaiRecordDto.setCoverages(extractStringBetweenManyXMLTags(oaiDc, DC_COVERAGE_OT, DC_COVERAGE_CT));
			oaiRecordDto.setRights(extractStringBetweenManyXMLTags(oaiDc, DC_RIGHTS_OT, DC_RIGHTS_CT));

			oaiRecordDtos.add(oaiRecordDto);
		}

		for (OaiRecordDto oaiRecordDto2 : oaiRecordDtos) {
			if (oaiRecordDto2.getTitles().isEmpty())
				break;
			System.out.println(oaiRecordDto2.getTitles().get(0));
		}

		System.out.println("done");

		return oaiRecordDtos;
	}

	public LinkedList<String> extractStringBetweenManyXMLTags(String oaiDc, String openingTag, String closingTag) {
		int indexOfOT;
		int indexOfCT;

		LinkedList<String> list = new LinkedList<String>();

		while (true) {
			indexOfOT = oaiDc.indexOf(openingTag);
			indexOfCT = oaiDc.indexOf(closingTag);

			if (indexOfOT == -1 || indexOfCT == -1)
				break;
			list.add(oaiDc.substring(indexOfOT + openingTag.length(), indexOfCT));
			oaiDc = StringHelpers.removeSubstring(oaiDc, 0, oaiDc.indexOf(closingTag) + closingTag.length());
		}

		return list;
	}

	public LinkedList<Date> extractDateBetweenManyXMLTags(String oaiDc, String openingTag, String closingTag)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

		int indexOfOT;
		int indexOfCT;

		LinkedList<Date> list = new LinkedList<Date>();

		while (true) {
			indexOfOT = oaiDc.indexOf(openingTag);
			indexOfCT = oaiDc.indexOf(closingTag);

			if (indexOfOT == -1 || indexOfCT == -1)
				break;

			try {
				list.add(df.parse(oaiDc.substring(indexOfOT + openingTag.length(), indexOfCT)));
			} catch (Exception e) {
				try {
					list.add(df2.parse(oaiDc.substring(indexOfOT + openingTag.length(), indexOfCT)));
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			oaiDc = StringHelpers.removeSubstring(oaiDc, 0, oaiDc.indexOf(closingTag) + closingTag.length());
		}

		return list;
	}

	/*
	 * public List<Test> findAllTest() { return
	 * entityManager.createNamedQuery("Test.findAll", Test.class).getResultList(); }
	 */
	
	/*
	 * public void bd(String oaiDc, String openingTag, String closingTag) { int
	 * indexOfOT; int indexOfCT; while (true) { indexOfOT =
	 * oaiDc.indexOf(openingTag); indexOfCT = oaiDc.indexOf(closingTag);
	 * 
	 * if (indexOfOT == -1 || indexOfCT == -1) break; oaiDc =
	 * listRecords.substring(indexOfOAI_DC_OT + OAI_DC_OT.length(),
	 * indexOfOAI_DC_CT); listRecords = StringHelpers.removeSubstring(listRecords,
	 * 0, indexOfOAI_DC_CT + OAI_DC_CT.length());
	 * 
	 * oaiRecordDto = new OaiRecordDto();
	 * 
	 * ad(oaiDc, DC_TITLE_OT, DC_TITLE_CT, oaiRecordDto);
	 * 
	 * oaiRecordDtos.add(oaiRecordDto); } }
	 */
}
