package swimsEJB.model.harvesting.dtos;

public class LimesurveyQuestionDto {
	private int id;
	private String question;
	private int sid;
	private int gid;
	private String title;
	private int parentQid;

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

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getParentQid() {
		return parentQid;
	}

	public void setParentQid(int parentQid) {
		this.parentQid = parentQid;
	}

	public LimesurveyQuestionDto(int id, String question, int sid, int gid, String title, int parentQid) {
		super();
		this.id = id;
		this.question = question;
		this.sid = sid;
		this.gid = gid;
		this.title = title;
		this.parentQid = parentQid;
	}

}
