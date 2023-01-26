package swimsWeb.controllers.harvesting.dtos;

import java.util.List;

import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;

public class CompoundLinkableLimesurveyQuestionDto extends LinkableLimesurveyQuestionDto {
	private List<String> selectedLimesurveyQuestionIds;

	public CompoundLinkableLimesurveyQuestionDto() {
		super();
	}

	public CompoundLinkableLimesurveyQuestionDto(LimesurveyQuestionDto linkableParentLimesurveyQuestionDto,
			List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos, boolean isParentQuestionAlreadyRegistered,
			List<String> selectedLimesurveyQuestionIds) {
		super(linkableParentLimesurveyQuestionDto, linkableChildLimesurveyQuestionDtos,
				isParentQuestionAlreadyRegistered);
		this.selectedLimesurveyQuestionIds = selectedLimesurveyQuestionIds;
	}

	public List<String> getSelectedLimesurveyQuestionIds() {
		return selectedLimesurveyQuestionIds;
	}

	public void setSelectedLimesurveyQuestionIds(List<String> selectedLimesurveyQuestionIds) {
		this.selectedLimesurveyQuestionIds = selectedLimesurveyQuestionIds;
	}

}
