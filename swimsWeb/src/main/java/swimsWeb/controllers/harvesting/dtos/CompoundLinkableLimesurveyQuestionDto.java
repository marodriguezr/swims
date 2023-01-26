package swimsWeb.controllers.harvesting.dtos;

import java.util.List;

import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;

public class CompoundLinkableLimesurveyQuestionDto extends LinkableLimesurveyQuestionDto {
	private List<Integer> selectedLimesurveyQuestionIds;

	public CompoundLinkableLimesurveyQuestionDto() {
		super();
	}

	public CompoundLinkableLimesurveyQuestionDto(LimesurveyQuestionDto linkableParentLimesurveyQuestionDto,
			List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos,
			List<Integer> selectedLimesurveyQuestionIds) {
		super(linkableParentLimesurveyQuestionDto, linkableChildLimesurveyQuestionDtos);
		this.selectedLimesurveyQuestionIds = selectedLimesurveyQuestionIds;
	}

	public List<Integer> getSelectedLimesurveyQuestionIds() {
		return selectedLimesurveyQuestionIds;
	}

	public void setSelectedLimesurveyQuestionIds(List<Integer> selectedLimesurveyQuestionIds) {
		this.selectedLimesurveyQuestionIds = selectedLimesurveyQuestionIds;
	}

}
