package swimsEJB.model.harvest.managers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvest.dtos.OaiRecordDto;
import swimsEJB.model.harvest.entities.OaiRecord;
import swimsEJB.utilities.StringHelpers;

/**
 * Session Bean implementation class OaiRecordManager
 */
@Stateless
@LocalBean
public class OaiRecordManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public OaiRecordManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<OaiRecord> findAllOaiRecords() {
		return daoManager.findAll(OaiRecord.class);
	}

	public OaiRecordDto oaiRecordToOaiRecordDto(OaiRecord oaiRecord) {
		OaiRecordDto oaiRecordDto;
		oaiRecordDto = new OaiRecordDto();
		oaiRecordDto.setId(oaiRecord.getId());
		oaiRecordDto
				.setTitles(oaiRecord.getOaiRecordTitles().stream().map(t -> t.getTitle()).collect(Collectors.toList()));
		oaiRecordDto.setCreators(
				oaiRecord.getOaiRecordCreators().stream().map(t -> t.getCreator()).collect(Collectors.toList()));
		oaiRecordDto.setSubjects(
				oaiRecord.getOaiRecordSubjects().stream().map(t -> t.getSubject()).collect(Collectors.toList()));
		oaiRecordDto.setDescriptions(oaiRecord.getOaiRecordDescriptions().stream().map(t -> t.getDescription())
				.collect(Collectors.toList()));
		oaiRecordDto.setPublishers(
				oaiRecord.getOaiRecordPublishers().stream().map(t -> t.getPublisher()).collect(Collectors.toList()));
		oaiRecordDto.setContributors(oaiRecord.getOaiRecordContributors().stream().map(t -> t.getContributor())
				.collect(Collectors.toList()));
		oaiRecordDto
				.setDates(oaiRecord.getOaiRecordDates().stream().map(t -> t.getDate()).collect(Collectors.toList()));
		oaiRecordDto.setIdentifiers(
				oaiRecord.getOaiRecordIdentifiers().stream().map(t -> t.getIdentifier()).collect(Collectors.toList()));
		return oaiRecordDto;
	}

	public List<OaiRecordDto> oaiRecordsToOaiRecordDtos(List<OaiRecord> oaiRecords) {
		List<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();
		for (OaiRecord oaiRecord : oaiRecords) {
			oaiRecordDtos.add(oaiRecordToOaiRecordDto(oaiRecord));
		}
		return oaiRecordDtos;
	}
	
	public List<OaiRecordDto> removeDuplicateOaiRecordDtos(List<OaiRecordDto> oaiRecordDtos,List<OaiRecordDto> oaiRecordDtos2) {
		HashMap<String, OaiRecordDto> oaiRecordDtoHashMap = new HashMap<>();
		for (OaiRecordDto oaiRecordDto : oaiRecordDtos) {
			oaiRecordDtoHashMap.put(oaiRecordDto.getId(), oaiRecordDto);
		}
		
		List<OaiRecordDto> oaiRecordDtos3 = new ArrayList<OaiRecordDto>();
		for (OaiRecordDto oaiRecordDto : oaiRecordDtos2) {
			if (oaiRecordDtoHashMap.containsKey(oaiRecordDto.getId())) continue;
			oaiRecordDtos3.add(oaiRecordDto);
		}
		
		return oaiRecordDtos3;
	}

	/**
	 * Method that fetches all the OAI records within the given parameters.
	 * 
	 * @param oaiSet Set of OAI Records that is meant to be harvested.
	 * @param from
	 * @param until
	 * @return A XML string with many OAI Records
	 * @throws Exception In case of being unable to perform the request.
	 */
	public String findManyOaiRecordsSingleRequest(String oaiSetIdentifier, LocalDate from, LocalDate until)
			throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String OAI_URI = MessageFormat.format(
				"http://repositorio.utn.edu.ec/oai/request?verb=ListRecords&metadataPrefix=oai_dc&from={1}&until={2}&set={0}",
				oaiSetIdentifier, dateFormat.format(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant())),
				dateFormat.format(Date.from(until.atStartOfDay(ZoneId.systemDefault()).toInstant())));
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAI_URI)).build();
		try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			return response.body();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la adquisición de recursos externos.");
		}
	}

	public List<String> findManyOaiRecords2(String oaiSetIdentifier, LocalDate from, LocalDate until) throws Exception {
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

	public ArrayList<OaiRecordDto> parseStringToOaiRecordDtos2(List<String> oaiRecords) throws Exception {
		String LIST_RECORDS_OT = "<ListRecords>";
		String LIST_RECORDS_CT = "</ListRecords>";
		int indexOfLIST_RECORDS_OT;
		int indexOfLIST_RECORDS_CT;

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

		ArrayList<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();
		String oaiDc;
		OaiRecordDto oaiRecordDto;
		String listRecords;

		for (String oaiRecord : oaiRecords) {
			indexOfLIST_RECORDS_OT = oaiRecord.indexOf(LIST_RECORDS_OT);
			indexOfLIST_RECORDS_CT = oaiRecord.indexOf(LIST_RECORDS_CT);
			if (indexOfLIST_RECORDS_OT == -1 || indexOfLIST_RECORDS_CT == -1)
				break;
			listRecords = oaiRecord.substring(indexOfLIST_RECORDS_OT + LIST_RECORDS_OT.length(),
					indexOfLIST_RECORDS_CT);
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
				oaiRecordDto
						.setDescriptions(extractStringBetweenManyXMLTags(oaiDc, DC_DESCRIPTION_OT, DC_DESCRIPTION_CT));
				oaiRecordDto.setPublishers(extractStringBetweenManyXMLTags(oaiDc, DC_PUBLISHER_OT, DC_PUBLISHER_CT));
				oaiRecordDto
						.setContributors(extractStringBetweenManyXMLTags(oaiDc, DC_CONTRIBUTOR_OT, DC_CONTRIBUTOR_CT));
				try {
					oaiRecordDto.setDates(extractDateBetweenManyXMLTags(oaiDc, DC_DATE_OT, DC_DATE_CT));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new Exception("A ocurrido un error en la conversión de registros OAI.");
				}
				oaiRecordDto.setTypes(extractStringBetweenManyXMLTags(oaiDc, DC_TYPE_OT, DC_TYPE_CT));
				oaiRecordDto.setFormats(extractStringBetweenManyXMLTags(oaiDc, DC_FORMAT_OT, DC_FORMAT_CT));
				oaiRecordDto.setIdentifiers(extractStringBetweenManyXMLTags(oaiDc, DC_IDENTIFIER_OT, DC_IDENTIFIER_CT));
				oaiRecordDto.setSources(extractStringBetweenManyXMLTags(oaiDc, DC_SOURCE_OT, DC_SOURCE_CT));
				oaiRecordDto.setLanguages(extractStringBetweenManyXMLTags(oaiDc, DC_LANGUAGE_OT, DC_LANGUAGE_CT));
				oaiRecordDto.setRelations(extractStringBetweenManyXMLTags(oaiDc, DC_RELATION_OT, DC_RELATION_CT));
				oaiRecordDto.setCoverages(extractStringBetweenManyXMLTags(oaiDc, DC_COVERAGE_OT, DC_COVERAGE_CT));
				oaiRecordDto.setRights(extractStringBetweenManyXMLTags(oaiDc, DC_RIGHTS_OT, DC_RIGHTS_CT));
				for (String identifier : oaiRecordDto.getIdentifiers()) {
					oaiRecordDto.setId(oaiRecordDto.getId() + identifier);
				}
				oaiRecordDto.setId(UUID.nameUUIDFromBytes(oaiRecordDto.getId().getBytes()).toString());
				oaiRecordDtos.add(oaiRecordDto);
			}
		}
		return oaiRecordDtos;
	}

	public ArrayList<OaiRecordDto> parseStringToOaiRecordDtosSingleRequest(String oaiRecord) throws Exception {
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

		String listRecords = oaiRecord.substring(oaiRecord.indexOf(LIST_RECORDS_OT) + LIST_RECORDS_OT.length(),
				oaiRecord.indexOf(LIST_RECORDS_CT));
		String oaiDc;
		OaiRecordDto oaiRecordDto;
		ArrayList<OaiRecordDto> oaiRecordDtos = new ArrayList<OaiRecordDto>();

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
			try {
				oaiRecordDto.setDates(extractDateBetweenManyXMLTags(oaiDc, DC_DATE_OT, DC_DATE_CT));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("A ocurrido un error en la conversión de registros OAI.");
			}
			oaiRecordDto.setTypes(extractStringBetweenManyXMLTags(oaiDc, DC_TYPE_OT, DC_TYPE_CT));
			oaiRecordDto.setFormats(extractStringBetweenManyXMLTags(oaiDc, DC_FORMAT_OT, DC_FORMAT_CT));
			oaiRecordDto.setIdentifiers(extractStringBetweenManyXMLTags(oaiDc, DC_IDENTIFIER_OT, DC_IDENTIFIER_CT));
			oaiRecordDto.setSources(extractStringBetweenManyXMLTags(oaiDc, DC_SOURCE_OT, DC_SOURCE_CT));
			oaiRecordDto.setLanguages(extractStringBetweenManyXMLTags(oaiDc, DC_LANGUAGE_OT, DC_LANGUAGE_CT));
			oaiRecordDto.setRelations(extractStringBetweenManyXMLTags(oaiDc, DC_RELATION_OT, DC_RELATION_CT));
			oaiRecordDto.setCoverages(extractStringBetweenManyXMLTags(oaiDc, DC_COVERAGE_OT, DC_COVERAGE_CT));
			oaiRecordDto.setRights(extractStringBetweenManyXMLTags(oaiDc, DC_RIGHTS_OT, DC_RIGHTS_CT));
			for (String identifier : oaiRecordDto.getIdentifiers()) {
				oaiRecordDto.setId(oaiRecordDto.getId() + identifier);
			}
			oaiRecordDto.setId(UUID.nameUUIDFromBytes(oaiRecordDto.getId().getBytes()).toString());
			oaiRecordDtos.add(oaiRecordDto);
		}
		return oaiRecordDtos;
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

	public ArrayList<String> extractStringBetweenManyXMLTags(String oaiDc, String openingTag, String closingTag) {
		int indexOfOT;
		int indexOfCT;

		ArrayList<String> list = new ArrayList<String>();

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

	public ArrayList<Date> extractDateBetweenManyXMLTags(String oaiDc, String openingTag, String closingTag)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

		int indexOfOT;
		int indexOfCT;

		ArrayList<Date> list = new ArrayList<Date>();

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

}
