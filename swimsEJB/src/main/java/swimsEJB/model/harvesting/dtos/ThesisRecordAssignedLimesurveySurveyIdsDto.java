package swimsEJB.model.harvesting.dtos;

import java.util.List;

import swimsEJB.model.harvesting.entities.ThesisRecord;

public class ThesisRecordAssignedLimesurveySurveyIdsDto {
	private ThesisRecord thesisRecord;
	private List<Integer> assignedLimesurveySurveyIds;

	public List<Integer> getAssignedLimesurveySurveyIds() {
		return assignedLimesurveySurveyIds;
	}

	public void setAssignedLimesurveySurveyIds(List<Integer> assignedLimesurveySurveyIds) {
		this.assignedLimesurveySurveyIds = assignedLimesurveySurveyIds;
	}

	public ThesisRecordAssignedLimesurveySurveyIdsDto(ThesisRecord oaiRecord, List<Integer> assignedLimesurveySurveyIds) {
		super();
		this.thesisRecord = oaiRecord;
		this.assignedLimesurveySurveyIds = assignedLimesurveySurveyIds;
	}

	public ThesisRecord getThesisRecord() {
		return thesisRecord;
	}

	public void setThesisRecord(ThesisRecord thesisRecord) {
		this.thesisRecord = thesisRecord;
	}

}
