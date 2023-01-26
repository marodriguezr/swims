package swimsEJB.model.harvesting.dtos;

import java.util.HashMap;

public class LimesurveyQuestionPropertiesDto extends LimesurveyQuestionDto {

	private HashMap<String, String> answerOptions;

	public LimesurveyQuestionPropertiesDto(String id, int limesurveyQuestionId, String question, int sid, int gid,
			String title, int parentQid, HashMap<String, String> answerOptions) {
		super(id, limesurveyQuestionId, question, sid, gid, title, parentQid);
		this.answerOptions = answerOptions;
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, String> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(HashMap<String, String> answerOptions) {
		this.answerOptions = answerOptions;
	}

}
