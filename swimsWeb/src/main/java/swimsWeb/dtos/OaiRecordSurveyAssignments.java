package swimsWeb.dtos;

import java.util.List;

import swimsEJB.model.harvesting.entities.OaiRecord;

public class OaiRecordSurveyAssignments {
	private OaiRecord oaiRecord;
	private List<Integer> assignedLimesurveySurveyIds;
	public OaiRecord getOaiRecord() {
		return oaiRecord;
	}
	public void setOaiRecord(OaiRecord oaiRecord) {
		this.oaiRecord = oaiRecord;
	}
	public List<Integer> getAssignedLimesurveySurveyIds() {
		return assignedLimesurveySurveyIds;
	}
	public void setAssignedLimesurveySurveyIds(List<Integer> assignedLimesurveySurveyIds) {
		this.assignedLimesurveySurveyIds = assignedLimesurveySurveyIds;
	}
	
	public OaiRecordSurveyAssignments(OaiRecord oaiRecord, List<Integer> assignedLimesurveySurveyIds) {
		super();
		this.oaiRecord = oaiRecord;
		this.assignedLimesurveySurveyIds = assignedLimesurveySurveyIds;
	}
}
