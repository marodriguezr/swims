package swimsEJB.model;

public class CompoundTestRegister {
	String thesisTitle;
	String studyVariableLongName;
	String limesurveyQuestionTitle;
	String limesurveyAnswerCode;

	public CompoundTestRegister(String thesisTitle, String studyVariableLongName, String limesurveyQuestionTitle,
			String limesurveyAnswerCode) {
		super();
		this.thesisTitle = thesisTitle;
		this.studyVariableLongName = studyVariableLongName;
		this.limesurveyQuestionTitle = limesurveyQuestionTitle;
		this.limesurveyAnswerCode = limesurveyAnswerCode;
	}

	public String getThesisTitle() {
		return thesisTitle;
	}

	public void setThesisTitle(String thesisTitle) {
		this.thesisTitle = thesisTitle;
	}

	public String getStudyVariableLongName() {
		return studyVariableLongName;
	}

	public void setStudyVariableLongName(String studyVariableLongName) {
		this.studyVariableLongName = studyVariableLongName;
	}

	public String getLimesurveyQuestionTitle() {
		return limesurveyQuestionTitle;
	}

	public void setLimesurveyQuestionTitle(String limesurveyQuestionTitle) {
		this.limesurveyQuestionTitle = limesurveyQuestionTitle;
	}

	public String getLimesurveyAnswerCode() {
		return limesurveyAnswerCode;
	}

	public void setLimesurveyAnswerCode(String limesurveyAnswerCode) {
		this.limesurveyAnswerCode = limesurveyAnswerCode;
	}

}
