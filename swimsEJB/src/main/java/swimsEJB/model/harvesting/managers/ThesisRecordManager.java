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
import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.model.harvesting.dtos.ThesisRecordDto;
import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.entities.ThesisSet;
import swimsEJB.model.harvesting.entities.views.UnassignedThesisAssignmentId;
import swimsEJB.utilities.DateUtilities;
import swimsEJB.utilities.StringHelpers;

/**
 * Session Bean implementation class OaiRecordManager
 */
@Stateless
@LocalBean
public class ThesisRecordManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private ThesisSetManager thesisSetManager;

	/**
	 * Default constructor.
	 */
	public ThesisRecordManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllUnassignedOaiRecordIds() {
		return daoManager.findAll(UnassignedThesisAssignmentId.class);
	}

	public ThesisRecord findOneOaiRecordById(String oaiRecordId) throws Exception {
		return (ThesisRecord) daoManager.findOneById(ThesisRecord.class, oaiRecordId);
	}

	@SuppressWarnings("unchecked")
	public List<ThesisRecord> findAllThesisRecords() {
		return daoManager.findAll(ThesisRecord.class);
	}

	public ThesisRecord createOneOaiRecord(ThesisRecord oaiRecord, ThesisSet thesisSet) throws Exception {
		oaiRecord.setThesisSet(thesisSet);
		oaiRecord.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		oaiRecord.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		oaiRecord.setIsActive(true);

		return (ThesisRecord) daoManager.createOne(oaiRecord);
	}

	public List<ThesisRecord> createManyThesisRecords(List<ThesisRecord> oaiRecords, ThesisSet oaiSet)
			throws Exception {
		List<ThesisRecord> oaiRecords2 = new ArrayList<>();
		for (ThesisRecord oaiRecord : oaiRecords) {
			oaiRecords2.add(createOneOaiRecord(oaiRecord, oaiSet));
		}
		return oaiRecords2;
	}

	public ThesisRecord oaiRecordDtoToOaiRecord(ThesisRecordDto thesisRecordDto) {
		ThesisRecord oaiRecord = new ThesisRecord();
		oaiRecord.setId(thesisRecordDto.getId());
		oaiRecord.setUrl(thesisRecordDto.getUrl());
		oaiRecord.setTitle(
				thesisRecordDto.getTitles().isEmpty() ? "Registro sin título" : thesisRecordDto.getTitles().get(0));
		oaiRecord.setCreator(thesisRecordDto.getCreators().isEmpty() ? "Registro sin creador"
				: thesisRecordDto.getCreators().get(0));
		oaiRecord.setSubject(
				thesisRecordDto.getSubjects().isEmpty() ? "Registro sin tema" : thesisRecordDto.getSubjects().get(0));
		oaiRecord.setDescription(thesisRecordDto.getDescriptions().isEmpty() ? "Registro sin descripción"
				: thesisRecordDto.getDescriptions().get(0));
		oaiRecord.setPublisher(thesisRecordDto.getPublishers().isEmpty() ? "Publisher non registered"
				: thesisRecordDto.getPublishers().get(0));
		oaiRecord.setContributor(thesisRecordDto.getContributors().isEmpty() ? "Registro sin director"
				: thesisRecordDto.getContributors().get(0));
		oaiRecord.setInferredIssueDate(thesisRecordDto.getInferredIssueDate() == null
				? thesisRecordDto.getDates().isEmpty() ? null : thesisRecordDto.getDates().get(0)
				: thesisRecordDto.getInferredIssueDate());

		return oaiRecord;
	}

	public List<ThesisRecord> thesisRecordDtosToThesisRecords(List<ThesisRecordDto> thesisRecordDtos) {
		List<ThesisRecord> oaiRecords = new ArrayList<ThesisRecord>();
		for (ThesisRecordDto thesisRecordDto : thesisRecordDtos) {
			oaiRecords.add(this.oaiRecordDtoToOaiRecord(thesisRecordDto));
		}
		return oaiRecords;
	}

	public ThesisRecordDto oaiRecordToOaiRecordDto(ThesisRecord oaiRecord) {
		ThesisRecordDto thesisRecordDto;
		thesisRecordDto = new ThesisRecordDto();
		thesisRecordDto.setId(oaiRecord.getId());
		thesisRecordDto.getTitles().add(oaiRecord.getTitle());
		thesisRecordDto.getCreators().add(oaiRecord.getCreator());
		thesisRecordDto.getSubjects().add(oaiRecord.getSubject());
		thesisRecordDto.getDescriptions().add(oaiRecord.getDescription());
		thesisRecordDto.getPublishers().add(oaiRecord.getPublisher());
		thesisRecordDto.getContributors().add(oaiRecord.getContributor());
		thesisRecordDto.getDates().add(oaiRecord.getInferredIssueDate());
		thesisRecordDto.setInferredIssueDate(oaiRecord.getInferredIssueDate());
		thesisRecordDto.setOaiSetId(oaiRecord.getThesisSet().getId());
		thesisRecordDto.setCreatedAt(oaiRecord.getCreatedAt());
		thesisRecordDto.setUpdateAt(oaiRecord.getUpdatedAt());
		thesisRecordDto.setActive(oaiRecord.getIsActive());
		return thesisRecordDto;
	}

	public List<ThesisRecordDto> oaiRecordsToOaiRecordDtos(List<ThesisRecord> oaiRecords) {
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();
		for (ThesisRecord oaiRecord : oaiRecords) {
			thesisRecordDtos.add(oaiRecordToOaiRecordDto(oaiRecord));
		}
		return thesisRecordDtos;
	}

	public List<ThesisRecordDto> removeDuplicateOaiRecordDtos(List<ThesisRecordDto> existentOaiRecordDtos,
			List<ThesisRecordDto> newOaiRecordDtos) {
		HashMap<String, ThesisRecordDto> existentOaiRecordDtosMap = new HashMap<>();
		for (ThesisRecordDto thesisRecordDto : existentOaiRecordDtos) {
			existentOaiRecordDtosMap.put(thesisRecordDto.getId(), thesisRecordDto);
		}

		HashMap<String, ThesisRecordDto> uniqueOaiRecordDtosMap = new HashMap<>();
		for (ThesisRecordDto thesisRecordDto : newOaiRecordDtos) {
			if (existentOaiRecordDtosMap.containsKey(thesisRecordDto.getId()))
				continue;
			if (uniqueOaiRecordDtosMap.containsKey(thesisRecordDto.getId()))
				continue;
			uniqueOaiRecordDtosMap.put(thesisRecordDto.getId(), thesisRecordDto);

		}

		return new ArrayList<ThesisRecordDto>(uniqueOaiRecordDtosMap.values());
	}

	public String fetchOaiStringByOaiRecordId(String oaiRecordId) throws Exception {
		String OAI_URI = "http://repositorio.utn.edu.ec/oai/request?verb=GetRecord&identifier=" + oaiRecordId
				+ "&metadataPrefix=oai_dc";
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

	public String getURLFromOaiRecordDto(ThesisRecordDto thesisRecordDto) {
		if (thesisRecordDto.getIdentifiers().size() == 0)
			return "";
		for (String identifier : thesisRecordDto.getIdentifiers()) {
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

	public List<ThesisRecordDto> filterOaiRecordDtosByKeyWords(String[] keywords,
			List<ThesisRecordDto> thesisRecordDtos) {
		LinkedList<ThesisRecordDto> filteredOaiRecordDtos = new LinkedList<>();

		Boolean hastOaiRecordBeenAdded;

		String[] normalizedKeywords = new String[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			normalizedKeywords[i] = StringHelpers.stripAccents(keywords[i]).toLowerCase();
		}

		for (String keyword : normalizedKeywords) {
			for (ThesisRecordDto thesisRecordDto : thesisRecordDtos) {
				hastOaiRecordBeenAdded = false;
				for (String title : thesisRecordDto.getTitles()) {
					if (StringHelpers.stripAccents(title).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(thesisRecordDto);
						hastOaiRecordBeenAdded = true;
						break;
					}
				}

				if (hastOaiRecordBeenAdded)
					continue;
				for (String subject : thesisRecordDto.getSubjects()) {
					if (StringHelpers.stripAccents(subject).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(thesisRecordDto);
						hastOaiRecordBeenAdded = true;
						break;
					}
				}

				if (hastOaiRecordBeenAdded)
					continue;
				for (String description : thesisRecordDto.getDescriptions()) {
					if (StringHelpers.stripAccents(description).toLowerCase().contains(keyword)) {
						filteredOaiRecordDtos.add(thesisRecordDto);
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
	public ThesisRecordDto parseStringToOaiRecordDto(String string) {
		ThesisRecordDto thesisRecordDto = new ThesisRecordDto();

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

		thesisRecordDto.setId(extractStringBetweenXMLTags(string, IDENTIFIER_OT, IDENTIFIER_CT));
		thesisRecordDto.setTitles(extractStringsBetweenXMLTags(string, DC_TITLE_OT, DC_TITLE_CT));
		thesisRecordDto.setCreators(extractStringsBetweenXMLTags(string, DC_CREATOR_OT, DC_CREATOR_CT));
		thesisRecordDto.setSubjects(extractStringsBetweenXMLTags(string, DC_SUBJECT_OT, DC_SUBJECT_CT));
		thesisRecordDto.setDescriptions(extractStringsBetweenXMLTags(string, DC_DESCRIPTION_OT, DC_DESCRIPTION_CT));
		thesisRecordDto.setPublishers(extractStringsBetweenXMLTags(string, DC_PUBLISHER_OT, DC_PUBLISHER_CT));
		thesisRecordDto.setContributors(extractStringsBetweenXMLTags(string, DC_CONTRIBUTOR_OT, DC_CONTRIBUTOR_CT));
		thesisRecordDto.setDates(extractDatesBetweenXMLTags(string, DC_DATE_OT, DC_DATE_CT));
		/**
		 * Inferred issue date can be null
		 */
		thesisRecordDto.setInferredIssueDate(DateUtilities.findMaxDateOfList(thesisRecordDto.getDates()));
		/**
		 * Inferred creation date can be null
		 */
		thesisRecordDto.setInferredCreationDate(DateUtilities.findMinDateOfList(thesisRecordDto.getDates()));
		thesisRecordDto.setTypes(extractStringsBetweenXMLTags(string, DC_TYPE_OT, DC_TYPE_CT));
		thesisRecordDto.setFormats(extractStringsBetweenXMLTags(string, DC_FORMAT_OT, DC_FORMAT_CT));
		thesisRecordDto.setIdentifiers(extractStringsBetweenXMLTags(string, DC_IDENTIFIER_OT, DC_IDENTIFIER_CT));
		thesisRecordDto.setSources(extractStringsBetweenXMLTags(string, DC_SOURCE_OT, DC_SOURCE_CT));
		thesisRecordDto.setLanguages(extractStringsBetweenXMLTags(string, DC_LANGUAGE_OT, DC_LANGUAGE_CT));
		thesisRecordDto.setRelations(extractStringsBetweenXMLTags(string, DC_RELATION_OT, DC_RELATION_CT));
		thesisRecordDto.setCoverages(extractStringsBetweenXMLTags(string, DC_COVERAGE_OT, DC_COVERAGE_CT));
		thesisRecordDto.setRights(extractStringsBetweenXMLTags(string, DC_RIGHTS_OT, DC_RIGHTS_CT));
		thesisRecordDto.setUrl(getURLFromOaiRecordDto(thesisRecordDto));

		return thesisRecordDto;
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
	public List<ThesisRecordDto> parseStringToOaiRecordDtos(String string) {
		String RECORD_OT = "<record>";
		String RECORD_CT = "</record>";

		String record;
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();

		while (true) {
			record = extractStringBetweenXMLTags(string, RECORD_OT, RECORD_CT);
			if (record == null)
				break;
			thesisRecordDtos.add(parseStringToOaiRecordDto(record));
			string = removeCharactersBetweenXMLTag(string, RECORD_OT, RECORD_CT);
		}
		return thesisRecordDtos;
	}

	public List<ThesisRecordDto> parseStringsToOaiRecordDtos(List<String> strings) {
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();

		for (String string : strings) {
			Stream.of(thesisRecordDtos, parseStringToOaiRecordDtos(string)).forEach(thesisRecordDtos::addAll);
		}

		return thesisRecordDtos;
	}

	public ThesisRecordDto findOneExternalOaiRecordDtoById(String id) throws Exception {
		return parseStringToOaiRecordDto(fetchOaiStringByOaiRecordId(id));
	}

	@SuppressWarnings("unchecked")
	public List<ThesisRecord> findAllUnassignedThesisRecordsByLimesurveySurveyDtos(
			List<LimesurveySurveyDto> availableLimesurveySurveyDtos) throws Exception {
		EntityManager entityManager = daoManager.getEntityManager();
		String queryString = "select or2.id from harvesting.thesis_records or2 except ";
		for (int i = 0; i < availableLimesurveySurveyDtos.size(); i++) {
			queryString += "select ta.thesis_record_id from harvesting.thesis_assignments ta, harvesting.limesurvey_survey_assignments lsa "
					+ "where ta.id = lsa.thesis_assignment_id and lsa.limesurvey_survey_id = "
					+ availableLimesurveySurveyDtos.get(i).getSid();
			if (i != (availableLimesurveySurveyDtos.size() - 1))
				queryString += " intersect ";
		}
		Query query = entityManager.createNativeQuery(queryString);
		List<Object> objects = query.getResultList();
		List<ThesisRecord> oaiRecords = new ArrayList<>();
		for (Object object : objects) {
			oaiRecords.add(this.findOneOaiRecordById(object.toString()));
		}
		return oaiRecords;
	}

}
