package swimsEJB.model.harvesting.managers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.OaiRecordDto;
import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.entities.OaiSet;
import swimsEJB.utilities.DateUtilities;
import swimsEJB.utilities.StringHelpers;

/**
 * Session Bean implementation class OaiRecordManager
 */
@Stateless
@LocalBean
public class OaiRecordManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private OaiSetManager oaiSetManager;

	/**
	 * Default constructor.
	 */
	public OaiRecordManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllUnassignedOaiRecordIds() {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery("select" + " or2.id " + " from" + "	harvesting.oai_records or2"
				+ " where" + "	not exists (" + "	select" + "	from" + "	harvesting.thesis_assignments ta"
				+ "	where" + " or2.id = ta.thesis_record_id)");

		List<String> unassignedOaiRecordIds = query.getResultList();
		return unassignedOaiRecordIds;
	}

	public OaiRecord findOneOaiRecordById(String oaiRecordId) throws Exception {
		return (OaiRecord) daoManager.findOneById(OaiRecord.class, oaiRecordId);
	}

	public List<OaiRecord> findAllUnassignedOaiRecords() throws Exception {
		List<String> foundAllUnassignedOaiRecordIds = findAllUnassignedOaiRecordIds();
		List<OaiRecord> oaiRecords = new ArrayList<>();
		for (String id : foundAllUnassignedOaiRecordIds) {
			oaiRecords.add(findOneOaiRecordById(id));
		}
		return oaiRecords;
	}

	@SuppressWarnings("unchecked")
	public List<OaiRecord> findAllOaiRecords() {
		return daoManager.findAll(OaiRecord.class);
	}

	public OaiRecord createOneOaiRecord(OaiRecord oaiRecord, OaiSet oaiSet) throws Exception {
		oaiRecord.setOaiSet(oaiSet);
		oaiRecord.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		oaiRecord.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		oaiRecord.setIsActive(true);

		return (OaiRecord) daoManager.createOne(oaiRecord);
	}

	public List<OaiRecord> createManyOaiRecords(List<OaiRecord> oaiRecords, OaiSet oaiSet) throws Exception {
		List<OaiRecord> oaiRecords2 = new ArrayList<>();
		for (OaiRecord oaiRecord : oaiRecords) {
			oaiRecords2.add(createOneOaiRecord(oaiRecord, oaiSet));
		}
		return oaiRecords2;
	}

	public OaiRecord oaiRecordDtoToOaiRecord(OaiRecordDto oaiRecordDto) {
		OaiRecord oaiRecord = new OaiRecord();
		oaiRecord.setId(oaiRecordDto.getId());
		oaiRecord.setUrl(oaiRecordDto.getUrl());
		oaiRecord
				.setTitle(oaiRecordDto.getTitles().isEmpty() ? "Registro sin título" : oaiRecordDto.getTitles().get(0));
		oaiRecord.setCreator(
				oaiRecordDto.getCreators().isEmpty() ? "Registro sin creador" : oaiRecordDto.getCreators().get(0));
		oaiRecord.setSubject(
				oaiRecordDto.getSubjects().isEmpty() ? "Registro sin tema" : oaiRecordDto.getSubjects().get(0));
		oaiRecord.setDescription(oaiRecordDto.getDescriptions().isEmpty() ? "Registro sin descripción"
				: oaiRecordDto.getDescriptions().get(0));
		oaiRecord.setPublisher(oaiRecordDto.getPublishers().isEmpty() ? "Publisher non registered"
				: oaiRecordDto.getPublishers().get(0));
		oaiRecord.setContributor(oaiRecordDto.getContributors().isEmpty() ? "Registro sin director"
				: oaiRecordDto.getContributors().get(0));
		oaiRecord.setInferredIssueDate(oaiRecordDto.getInferredIssueDate() == null
				? oaiRecordDto.getDates().isEmpty() ? null : oaiRecordDto.getDates().get(0)
				: oaiRecordDto.getInferredIssueDate());

		return oaiRecord;
	}

	public List<OaiRecord> oaiRecordDtosToOaiRecords(List<OaiRecordDto> oaiRecordDtos) {
		List<OaiRecord> oaiRecords = new ArrayList<OaiRecord>();
		for (OaiRecordDto oaiRecordDto : oaiRecordDtos) {
			oaiRecords.add(this.oaiRecordDtoToOaiRecord(oaiRecordDto));
		}
		return oaiRecords;
	}

	public OaiRecordDto oaiRecordToOaiRecordDto(OaiRecord oaiRecord) {
		OaiRecordDto oaiRecordDto;
		oaiRecordDto = new OaiRecordDto();
		oaiRecordDto.setId(oaiRecord.getId());
		oaiRecordDto.getTitles().add(oaiRecord.getTitle());
		oaiRecordDto.getCreators().add(oaiRecord.getCreator());
		oaiRecordDto.getSubjects().add(oaiRecord.getSubject());
		oaiRecordDto.getDescriptions().add(oaiRecord.getDescription());
		oaiRecordDto.getPublishers().add(oaiRecord.getPublisher());
		oaiRecordDto.getContributors().add(oaiRecord.getContributor());
		oaiRecordDto.getDates().add(oaiRecord.getInferredIssueDate());
		oaiRecordDto.setInferredIssueDate(oaiRecord.getInferredIssueDate());
		oaiRecordDto.setOaiSetId(oaiRecord.getOaiSet().getId());
		oaiRecordDto.setCreatedAt(oaiRecord.getCreatedAt());
		oaiRecordDto.setUpdateAt(oaiRecord.getUpdatedAt());
		oaiRecordDto.setActive(oaiRecord.getIsActive());
		return oaiRecordDto;
	}

	public List<OaiRecordDto> oaiRecordsToOaiRecordDtos(List<OaiRecord> oaiRecords) {
		List<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();
		for (OaiRecord oaiRecord : oaiRecords) {
			oaiRecordDtos.add(oaiRecordToOaiRecordDto(oaiRecord));
		}
		return oaiRecordDtos;
	}

	public List<OaiRecordDto> removeDuplicateOaiRecordDtos(List<OaiRecordDto> oaiRecordDtos,
			List<OaiRecordDto> oaiRecordDtos2) {
		HashMap<String, OaiRecordDto> oaiRecordDtoHashMap = new HashMap<>();
		for (OaiRecordDto oaiRecordDto : oaiRecordDtos) {
			oaiRecordDtoHashMap.put(oaiRecordDto.getId(), oaiRecordDto);
		}

		List<OaiRecordDto> oaiRecordDtos3 = new ArrayList<OaiRecordDto>();
		for (OaiRecordDto oaiRecordDto : oaiRecordDtos2) {
			if (oaiRecordDtoHashMap.containsKey(oaiRecordDto.getId()))
				continue;
			oaiRecordDtos3.add(oaiRecordDto);
		}

		return oaiRecordDtos3;
	}

	public String fetchOaiStringByOaiRecordId(String oaiRecordId) throws Exception {
		String OAI_URI = "http://repositorio.utn.edu.ec/oai/request?verb=GetRecord&identifier="
				+ oaiRecordId + "&metadataPrefix=oai_dc";
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAI_URI)).build();
		HttpResponse<String> response;
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
			return response.body();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la adquisicón de recursos externos.");
		}
	}

	public List<String> fetchOaiStrings(String oaiSetIdentifier, LocalDate from, LocalDate until) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String OAI_URI = MessageFormat.format(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from={1}&until={2}&set={0}",
				oaiSetIdentifier, dateFormat.format(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant())),
				dateFormat.format(Date.from(until.atStartOfDay(ZoneId.systemDefault()).toInstant())));
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAI_URI)).build();

		List<String> oaiRecords = new ArrayList<String>();

		HttpResponse<String> response;
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
			oaiRecords.add(response.body());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la adquisición de recursos externos.");
		}
		while (response.body().indexOf("</resumptionToken>") != -1) {
			request = HttpRequest.newBuilder()
					.uri(URI.create(
							"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&resumptionToken="
									+ StringUtils.substringBetween(
											StringHelpers.removeSubstring(response.body(), 0,
													response.body().indexOf("<resumptionToken")),
											">", "</resumptionToken>")))
					.build();
			try {
				response = httpClient.send(request, BodyHandlers.ofString());
				oaiRecords.add(response.body());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Ha ocurrido un error en la adquisición de recursos externos.");
			}
		}
		return oaiRecords;
	}

	public String getURLFromOaiRecordDto(OaiRecordDto oaiRecordDto) {
		if (oaiRecordDto.getIdentifiers().size() == 0)
			return "";
		for (String identifier : oaiRecordDto.getIdentifiers()) {
			if (identifier.contains("http://") || identifier.contains("https://"))
				return identifier;
		}
		return "";
	}

	public String extractStringBetweenXMLTags(String string, String openingTag, String closingTag) {
		int indexOfOT;
		int indexOfCT;

		indexOfOT = string.indexOf(openingTag);
		indexOfCT = string.indexOf(closingTag);

		if (indexOfOT == -1 || indexOfCT == -1)
			return null;
		return string.substring(indexOfOT + openingTag.length(), indexOfCT);
	}

	public ArrayList<String> extractStringsBetweenXMLTags(String oaiDc, String openingTag, String closingTag) {
		ArrayList<String> list = new ArrayList<String>();

		String string;
		while (true) {
			string = extractStringBetweenXMLTags(oaiDc, openingTag, closingTag);
			if (string == null)
				break;
			list.add(string);
			oaiDc = removeCharactersBetweenXMLTag(oaiDc, openingTag, closingTag);
		}
		return list;
	}

	public Date extractDateBetweenXMLTags(String string, String openingTag, String closingTag) {
		int indexOfOT = string.indexOf(openingTag);
		int indexOfCT = string.indexOf(closingTag);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // issue date
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd"); // creation date

		if (indexOfOT == -1 || indexOfCT == -1)
			return null;
		try {
			return df.parse(string.substring(indexOfOT + openingTag.length(), indexOfCT));
		} catch (Exception e) {
			try {
				return df2.parse(string.substring(indexOfOT + openingTag.length(), indexOfCT));
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
	}

	public ArrayList<Date> extractDatesBetweenXMLTags(String oaiDc, String openingTag, String closingTag) {
		ArrayList<Date> list = new ArrayList<Date>();

		Date date;

		while (true) {
			date = extractDateBetweenXMLTags(oaiDc, openingTag, closingTag);
			if (date == null)
				break;
			list.add(date);
			oaiDc = removeCharactersBetweenXMLTag(oaiDc, openingTag, closingTag);
		}
		return list;
	}

	public List<OaiRecordDto> filterOaiRecordDtosByKeyWords(String[] keywords, List<OaiRecordDto> oaiRecordDtos) {
		LinkedList<OaiRecordDto> filteredOaiRecordDtos = new LinkedList<>();

		Boolean hastOaiRecordBeenAdded;

		String[] normalizedKeywords = new String[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			normalizedKeywords[i] = StringHelpers.stripAccents(keywords[i]).toLowerCase();
		}

		for (String keyword : normalizedKeywords) {
			for (OaiRecordDto oaiRecordDto : oaiRecordDtos) {
				hastOaiRecordBeenAdded = false;
				for (String title : oaiRecordDto.getTitles()) {
					if (StringHelpers.stripAccents(title).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(oaiRecordDto);
						hastOaiRecordBeenAdded = true;
						break;
					}
				}

				if (hastOaiRecordBeenAdded)
					continue;
				for (String subject : oaiRecordDto.getSubjects()) {
					if (StringHelpers.stripAccents(subject).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(oaiRecordDto);
						hastOaiRecordBeenAdded = true;
						break;
					}
				}

				if (hastOaiRecordBeenAdded)
					continue;
				for (String description : oaiRecordDto.getDescriptions()) {
					if (StringHelpers.stripAccents(description).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(oaiRecordDto);
						hastOaiRecordBeenAdded = true;
						break;
					}
				}
			}
		}

		return filteredOaiRecordDtos;
	}

	public String removeCharactersBetweenXMLTag(String string, String openingTag, String closingTag) {
		return StringHelpers.removeSubstring(string, 0, string.indexOf(closingTag) + closingTag.length());
	}

	/**
	 * According to OAI-PMH specification, this method expects a string containing
	 * all the fields between the tags <record> and </record> </br>
	 * The attributes oaiSetId, createdAt, updatedAt, isActive are not setted.
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public OaiRecordDto parseStringToOaiRecordDto(String string) {
		OaiRecordDto oaiRecordDto = new OaiRecordDto();

		String IDENTIFIER_OT = "<identifier>";
		String IDENTIFIER_CT = "</identifier>";

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

		oaiRecordDto.setId(extractStringBetweenXMLTags(string, IDENTIFIER_OT, IDENTIFIER_CT));
		oaiRecordDto.setTitles(extractStringsBetweenXMLTags(string, DC_TITLE_OT, DC_TITLE_CT));
		oaiRecordDto.setCreators(extractStringsBetweenXMLTags(string, DC_CREATOR_OT, DC_CREATOR_CT));
		oaiRecordDto.setSubjects(extractStringsBetweenXMLTags(string, DC_SUBJECT_OT, DC_SUBJECT_CT));
		oaiRecordDto.setDescriptions(extractStringsBetweenXMLTags(string, DC_DESCRIPTION_OT, DC_DESCRIPTION_CT));
		oaiRecordDto.setPublishers(extractStringsBetweenXMLTags(string, DC_PUBLISHER_OT, DC_PUBLISHER_CT));
		oaiRecordDto.setContributors(extractStringsBetweenXMLTags(string, DC_CONTRIBUTOR_OT, DC_CONTRIBUTOR_CT));
		oaiRecordDto.setDates(extractDatesBetweenXMLTags(string, DC_DATE_OT, DC_DATE_CT));
		/**
		 * Inferred issue date can be null
		 */
		oaiRecordDto.setInferredIssueDate(DateUtilities.findMaxDateOfList(oaiRecordDto.getDates()));
		/**
		 * Inferred creation date can be null
		 */
		oaiRecordDto.setInferredCreationDate(DateUtilities.findMinDateOfList(oaiRecordDto.getDates()));
		oaiRecordDto.setTypes(extractStringsBetweenXMLTags(string, DC_TYPE_OT, DC_TYPE_CT));
		oaiRecordDto.setFormats(extractStringsBetweenXMLTags(string, DC_FORMAT_OT, DC_FORMAT_CT));
		oaiRecordDto.setIdentifiers(extractStringsBetweenXMLTags(string, DC_IDENTIFIER_OT, DC_IDENTIFIER_CT));
		oaiRecordDto.setSources(extractStringsBetweenXMLTags(string, DC_SOURCE_OT, DC_SOURCE_CT));
		oaiRecordDto.setLanguages(extractStringsBetweenXMLTags(string, DC_LANGUAGE_OT, DC_LANGUAGE_CT));
		oaiRecordDto.setRelations(extractStringsBetweenXMLTags(string, DC_RELATION_OT, DC_RELATION_CT));
		oaiRecordDto.setCoverages(extractStringsBetweenXMLTags(string, DC_COVERAGE_OT, DC_COVERAGE_CT));
		oaiRecordDto.setRights(extractStringsBetweenXMLTags(string, DC_RIGHTS_OT, DC_RIGHTS_CT));
		oaiRecordDto.setUrl(getURLFromOaiRecordDto(oaiRecordDto));

		return oaiRecordDto;
	}

	/**
	 * According to OAI-PMH specification, this method expects a string containing
	 * all the fields between the tags <ListRecords> and </ListRecords> </br>
	 * The attributes oaiSetId, createdAt, updatedAt, isActive are not setted.
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public List<OaiRecordDto> parseStringToOaiRecordDtos(String string) {
		String RECORD_OT = "<record>";
		String RECORD_CT = "</record>";

		String record;
		List<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();

		while (true) {
			record = extractStringBetweenXMLTags(string, RECORD_OT, RECORD_CT);
			if (record == null)
				break;
			oaiRecordDtos.add(parseStringToOaiRecordDto(record));
			string = removeCharactersBetweenXMLTag(string, RECORD_OT, RECORD_CT);
		}
		return oaiRecordDtos;
	}

	public List<OaiRecordDto> parseStringsToOaiRecordDtos(List<String> strings) {
		List<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();

		for (String string : strings) {
			Stream.of(oaiRecordDtos, parseStringToOaiRecordDtos(string)).forEach(oaiRecordDtos::addAll);
		}

		return oaiRecordDtos;
	}

	public OaiRecordDto findOneExternalOaiRecordDtoById(String id) throws Exception {
		return parseStringToOaiRecordDto(fetchOaiStringByOaiRecordId(id));
	}
}
