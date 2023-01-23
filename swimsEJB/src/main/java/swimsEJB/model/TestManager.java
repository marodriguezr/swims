package swimsEJB.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Base64.Encoder;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.ThesisRecordDto;
import swimsEJB.model.harvesting.managers.ThesisRecordManager;
import swimsEJB.model.harvesting.services.LimesurveyService;
import swimsEJB.utilities.ResourceUtilities;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Session Bean implementation class TestManager
 */
@Stateless
@LocalBean
public class TestManager {

	/**
	 * Default constructor.
	 */
	public TestManager() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	private DaoManager daoManager;
	@EJB
	private ThesisRecordManager thesisRecordManager;

	public static String parse(String jsonLine) {
		JsonObject jobject = JsonParser.parseString(jsonLine).getAsJsonObject();
		String result = jobject.get("result").getAsString();
		return result;
	}

	public void test() throws Exception {
//		InputStream inputStream;
//		inputStream = ResourceUtilities.getResourceInputStream("registros-tesis.csv");
//
//		Path path = java.nio.file.Files.createTempFile("file", ".csv");
//		Files.write(path, inputStream.readAllBytes());
//		List<List<String>> records = new ArrayList<List<String>>();
//		try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"))) {
//			String[] values = null;
//			while ((values = csvReader.readNext()) != null) {
//				records.add(Arrays.asList(values));
//			}
//		}
//
//		records.remove(0);
//		System.out.println(records.size());
//
//		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
//		Date d1 = sdformat.parse("2011-01-01");
//
//		for (List<String> list : records) {
//			ThesisRecordDto thesisRecordDto = thesisRecordManager.findOneExternalThesisRecordDtoById(list.get(0));
//			if (thesisRecordDto.getInferredCreationDate().compareTo(d1) < 0) {
//				continue;
//			}
//			thesisRecordManager.createOneThesisRecord(
//					thesisRecordManager.thesisRecordDtoToThesisRecord(thesisRecordDto), list.get(1));
//		}

	}

	public void test4(byte[] bytes) {

	}

}
