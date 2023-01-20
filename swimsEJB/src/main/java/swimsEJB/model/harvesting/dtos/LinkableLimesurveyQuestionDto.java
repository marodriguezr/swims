package swimsEJB.model.harvesting.dtos;

import java.util.List;

public class LinkableLimesurveyQuestionDto {
	private LimesurveyQuestionDto linkableParentLimesurveyQuestionDto;
	private List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos;

	public LinkableLimesurveyQuestionDto() {
		super();
	}

	public List<LimesurveyQuestionDto> getLinkableChildLimesurveyQuestionDtos() {
		return linkableChildLimesurveyQuestionDtos;
	}

	public void setLinkableChildLimesurveyQuestionDtos(
			List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos) {
		this.linkableChildLimesurveyQuestionDtos = linkableChildLimesurveyQuestionDtos;
	}

	public LimesurveyQuestionDto getLinkableParentLimesurveyQuestionDto() {
		return linkableParentLimesurveyQuestionDto;
	}

	public void setLinkableParentLimesurveyQuestionDto(LimesurveyQuestionDto linkableParentLimesurveyQuestionDto) {
		this.linkableParentLimesurveyQuestionDto = linkableParentLimesurveyQuestionDto;
	}

}
