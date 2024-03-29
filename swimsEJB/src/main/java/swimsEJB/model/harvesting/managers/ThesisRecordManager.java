package swimsEJB.model.harvesting.managers;

import java.io.IOException;
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
 * Session Bean implementation class ThesisRecordManager
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
	public List<String> findAllUnassignedThesisRecordIds() {
		return daoManager.findAll(UnassignedThesisAssignmentId.class);
	}

	public ThesisRecord findOneThesisRecordById(String thesisRecordId) throws Exception {
		return (ThesisRecord) daoManager.findOneById(ThesisRecord.class, thesisRecordId);
	}

	@SuppressWarnings("unchecked")
	public List<ThesisRecord> findAllThesisRecords() {
		return daoManager.findAll(ThesisRecord.class, "updatedAt", false);
	}

	public ThesisRecord createOneThesisRecord(ThesisRecord thesisRecord, ThesisSet thesisSet) throws Exception {
		thesisRecord.setThesisSet(thesisSet);
		thesisRecord.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		thesisRecord.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		thesisRecord.setIsActive(true);

		return (ThesisRecord) daoManager.createOne(thesisRecord);
	}

	public ThesisRecord createOneThesisRecord(ThesisRecord thesisRecord, String thesisSetId) throws Exception {
		ThesisSet thesisSet = thesisSetManager.findOneThesisSetById(thesisSetId);
		if (thesisSet == null)
			throw new Exception("El set de tesis no se encuentra registrado.");
		return createOneThesisRecord(thesisRecord, thesisSet);
	}

	public List<ThesisRecord> createManyThesisRecords(List<ThesisRecord> thesisRecords, ThesisSet thesisSet)
			throws Exception {
		List<ThesisRecord> thesisRecords2 = new ArrayList<>();
		for (ThesisRecord thesisRecord : thesisRecords) {
			thesisRecords2.add(createOneThesisRecord(thesisRecord, thesisSet));
		}
		return thesisRecords2;
	}

	public ThesisRecord thesisRecordDtoToThesisRecord(ThesisRecordDto thesisRecordDto) throws IOException {
		ThesisRecord thesisRecord = new ThesisRecord();
		thesisRecord.setId(thesisRecordDto.getId());
		thesisRecord.setUrl(thesisRecordDto.getUrl());
		thesisRecord.setTitle(
				thesisRecordDto.getTitles().isEmpty() ? "Registro sin título" : thesisRecordDto.getTitles().get(0));
		thesisRecord.setCreator(thesisRecordDto.getCreators().isEmpty() ? "Registro sin creador"
				: thesisRecordDto.getCreators().get(0));
		thesisRecord.setSubject(
				thesisRecordDto.getSubjects().isEmpty() ? "Registro sin tema" : thesisRecordDto.getSubjects().get(0));
		thesisRecord.setDescription(thesisRecordDto.getDescriptions().isEmpty() ? "Registro sin descripción"
				: thesisRecordDto.getDescriptions().get(0));
		thesisRecord.setContributor(thesisRecordDto.getContributors().isEmpty() ? "Registro sin director"
				: thesisRecordDto.getContributors().get(0));
		thesisRecord.setInferredIssueDate(thesisRecordDto.getInferredIssueDate() == null
				? thesisRecordDto.getDates().isEmpty() ? null : thesisRecordDto.getDates().get(0)
				: thesisRecordDto.getInferredIssueDate());
		thesisRecord.setInferredCreationDate(thesisRecordDto.getInferredCreationDate() == null
				? thesisRecordDto.getDates().isEmpty() ? null
						: thesisRecordDto.getDates().get(thesisRecordDto.getDates().size() - 1)
				: thesisRecordDto.getInferredCreationDate());
		return thesisRecord;
	}

	public List<ThesisRecord> thesisRecordDtosToThesisRecords(List<ThesisRecordDto> thesisRecordDtos)
			throws IOException {
		List<ThesisRecord> thesisRecords = new ArrayList<ThesisRecord>();
		for (ThesisRecordDto thesisRecordDto : thesisRecordDtos) {
			thesisRecords.add(this.thesisRecordDtoToThesisRecord(thesisRecordDto));
		}
		return thesisRecords;
	}

	public ThesisRecordDto thesisRecordToThesisRecordDto(ThesisRecord thesisRecord) {
		ThesisRecordDto thesisRecordDto;
		thesisRecordDto = new ThesisRecordDto();
		thesisRecordDto.setId(thesisRecord.getId());
		thesisRecordDto.getTitles().add(thesisRecord.getTitle());
		thesisRecordDto.getCreators().add(thesisRecord.getCreator());
		thesisRecordDto.getSubjects().add(thesisRecord.getSubject());
		thesisRecordDto.getDescriptions().add(thesisRecord.getDescription());
		thesisRecordDto.getContributors().add(thesisRecord.getContributor());
		thesisRecordDto.getDates().add(thesisRecord.getInferredIssueDate());
		thesisRecordDto.setInferredIssueDate(thesisRecord.getInferredIssueDate());
		thesisRecordDto.setThesisSetId(thesisRecord.getThesisSet().getId());
		thesisRecordDto.setCreatedAt(thesisRecord.getCreatedAt());
		thesisRecordDto.setUpdateAt(thesisRecord.getUpdatedAt());
		thesisRecordDto.setActive(thesisRecord.getIsActive());
		return thesisRecordDto;
	}

	public List<ThesisRecordDto> thesisRecordsToThesisRecordDtos(List<ThesisRecord> thesisRecords) {
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();
		for (ThesisRecord thesisRecord : thesisRecords) {
			thesisRecordDtos.add(thesisRecordToThesisRecordDto(thesisRecord));
		}
		return thesisRecordDtos;
	}

	public List<ThesisRecordDto> removeBlankThesisRecordDtos(List<ThesisRecordDto> thesisRecordDtos) {
		List<ThesisRecordDto> nonBlankThesisRecordDtos = new ArrayList<>(thesisRecordDtos);
		nonBlankThesisRecordDtos.removeIf(arg0 -> arg0.getTitles().size() == 0);
		return nonBlankThesisRecordDtos;
	}

	public List<ThesisRecordDto> removeDuplicateThesisRecordDtos(List<ThesisRecordDto> existentThesisRecordDtos,
			List<ThesisRecordDto> newThesisRecordDtos) {
		HashMap<String, ThesisRecordDto> existentThesisRecordDtosMap = new HashMap<>();
		for (ThesisRecordDto thesisRecordDto : existentThesisRecordDtos) {
			existentThesisRecordDtosMap.put(thesisRecordDto.getId(), thesisRecordDto);
		}

		HashMap<String, ThesisRecordDto> uniqueThesisRecordDtosMap = new HashMap<>();
		for (ThesisRecordDto thesisRecordDto : newThesisRecordDtos) {
			if (existentThesisRecordDtosMap.containsKey(thesisRecordDto.getId()))
				continue;
			if (uniqueThesisRecordDtosMap.containsKey(thesisRecordDto.getId()))
				continue;
			if (existentThesisRecordDtos.stream().anyMatch(t -> thesisRecordDto.getTitles().containsAll(t.getTitles())))
				continue;
			uniqueThesisRecordDtosMap.put(thesisRecordDto.getId(), thesisRecordDto);
		}

		return new ArrayList<ThesisRecordDto>(uniqueThesisRecordDtosMap.values());
	}

	public String fetchThesisStringByThesisRecordId(String thesisRecordId) throws Exception {
		String OAI_URI = "http://repositorio.utn.edu.ec/oai/request?verb=GetRecord&identifier=" + thesisRecordId
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

	public List<String> fetchThesisStrings(String thesisSetIdentifier, LocalDate from, LocalDate until)
			throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String OAI_URI = MessageFormat.format(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from={1}&until={2}&set={0}",
				thesisSetIdentifier,
				dateFormat.format(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant())),
				dateFormat.format(Date.from(until.atStartOfDay(ZoneId.systemDefault()).toInstant())));
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAI_URI)).build();

		List<String> thesisRecords = new ArrayList<String>();

		HttpResponse<String> response;
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
			thesisRecords.add(response.body());
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
				thesisRecords.add(response.body());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Ha ocurrido un error en la adquisición de recursos externos.");
			}
		}
		return thesisRecords;
	}

	public String getURLFromThesisRecordDto(ThesisRecordDto thesisRecordDto) {
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

	public List<ThesisRecordDto> filterThesisRecordDtosByKeyWords(String[] keywords,
			List<ThesisRecordDto> thesisRecordDtos) {
		LinkedList<ThesisRecordDto> filteredThesisRecordDtos = new LinkedList<>();

		Boolean hastThesisRecordBeenAdded;

		String[] normalizedKeywords = new String[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			normalizedKeywords[i] = StringHelpers.stripAccents(keywords[i]).toLowerCase();
		}

		for (String keyword : normalizedKeywords) {
			for (ThesisRecordDto thesisRecordDto : thesisRecordDtos) {
				hastThesisRecordBeenAdded = false;
				for (String title : thesisRecordDto.getTitles()) {
					if (StringHelpers.stripAccents(title).toLowerCase().contains(keyword)) {
						filteredThesisRecordDtos.add(thesisRecordDto);
						hastThesisRecordBeenAdded = true;
						break;
					}
				}

				if (hastThesisRecordBeenAdded)
					continue;
				for (String subject : thesisRecordDto.getSubjects()) {
					if (StringHelpers.stripAccents(subject).toLowerCase().contains(keyword)) {
						filteredThesisRecordDtos.add(thesisRecordDto);
						hastThesisRecordBeenAdded = true;
						break;
					}
				}

				if (hastThesisRecordBeenAdded)
					continue;
				for (String description : thesisRecordDto.getDescriptions()) {
					if (StringHelpers.stripAccents(description).toLowerCase().contains(keyword)) {
						filteredThesisRecordDtos.add(thesisRecordDto);
						hastThesisRecordBeenAdded = true;
						break;
					}
				}
			}
		}

		return filteredThesisRecordDtos;
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
	public ThesisRecordDto parseStringToThesisRecordDto(String string) {
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
		thesisRecordDto.setUrl(getURLFromThesisRecordDto(thesisRecordDto));

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
	public List<ThesisRecordDto> parseStringToThesisRecordDtos(String string) {
		String RECORD_OT = "<record>";
		String RECORD_CT = "</record>";

		String record;
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();

		while (true) {
			record = extractStringBetweenXMLTags(string, RECORD_OT, RECORD_CT);
			if (record == null)
				break;
			thesisRecordDtos.add(parseStringToThesisRecordDto(record));
			string = removeCharactersBetweenXMLTag(string, RECORD_OT, RECORD_CT);
		}
		return thesisRecordDtos;
	}

	public List<ThesisRecordDto> parseStringsToThesisRecordDtos(List<String> strings) {
		List<ThesisRecordDto> thesisRecordDtos = new ArrayList<ThesisRecordDto>();

		for (String string : strings) {
			Stream.of(thesisRecordDtos, parseStringToThesisRecordDtos(string)).forEach(thesisRecordDtos::addAll);
		}

		return thesisRecordDtos;
	}

	public ThesisRecordDto findOneExternalThesisRecordDtoById(String id) throws Exception {
		return parseStringToThesisRecordDto(fetchThesisStringByThesisRecordId(id));
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
		List<ThesisRecord> thesisRecords = new ArrayList<>();
		for (Object object : objects) {
			thesisRecords.add(this.findOneThesisRecordById(object.toString()));
		}
		return thesisRecords;
	}

}
