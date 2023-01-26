package swimsEJB.model.harvesting.dtos;

import java.util.List;

public class LinkableLimesurveyQuestionDto {
	private LimesurveyQuestionDto linkableParentLimesurveyQuestionDto;
	private boolean isParentQuestionAlreadyRegistered;
	private List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos;

	public LinkableLimesurveyQuestionDto() {
		super();
	}

	public LinkableLimesurveyQuestionDto(LimesurveyQuestionDto linkableParentLimesurveyQuestionDto,
			List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos) {
		super();
		this.linkableParentLimesurveyQuestionDto = linkableParentLimesurveyQuestionDto;
		this.linkableChildLimesurveyQuestionDtos = linkableChildLimesurveyQuestionDtos;
	}

	public LinkableLimesurveyQuestionDto(LimesurveyQuestionDto linkableParentLimesurveyQuestionDto,
			List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos,
			boolean isParentQuestionAlreadyRegistered) {
		super();
		this.linkableParentLimesurveyQuestionDto = linkableParentLimesurveyQuestionDto;
		this.linkableChildLimesurveyQuestionDtos = linkableChildLimesurveyQuestionDtos;
		this.isParentQuestionAlreadyRegistered = isParentQuestionAlreadyRegistered;
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

	public boolean isParentQuestionAlreadyRegistered() {
		return isParentQuestionAlreadyRegistered;
	}

	public void setParentQuestionAlreadyRegistered(boolean isParentQuestionAlreadyRegistered) {
		this.isParentQuestionAlreadyRegistered = isParentQuestionAlreadyRegistered;
	}

}
