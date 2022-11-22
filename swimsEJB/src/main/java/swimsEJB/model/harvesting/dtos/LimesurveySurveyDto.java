package swimsEJB.model.harvesting.dtos;

public class LimesurveySurveyDto {
	private Integer sid;
	private String surveylsTitle;
	private boolean active;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getSurveylsTitle() {
		return surveylsTitle;
	}

	public void setSurveylsTitle(String surveylsTitle) {
		this.surveylsTitle = surveylsTitle;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LimesurveySurveyDto(Integer sid, String surveylsTitle, boolean active) {
		super();
		this.sid = sid;
		this.surveylsTitle = surveylsTitle;
		this.active = active;
	}

}
