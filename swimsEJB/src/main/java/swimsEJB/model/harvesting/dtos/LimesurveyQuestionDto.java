package swimsEJB.model.harvesting.dtos;

public class LimesurveyQuestionDto {
	private int id;
	private String question;
	private int sid;
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LimesurveyQuestionDto(int id, String question, int sid, String title) {
		super();
		this.id = id;
		this.question = question;
		this.sid = sid;
		this.title = title;
	}

}
