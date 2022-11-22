package swimsWeb.controller.harvesting.thesis_record_assignment;

import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH;
import static swimsWeb.constants.WebappPaths.HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.harvesting.managers.ThesisAssignmentManager;

@Named
@SessionScoped
public class UserSelectionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private UserManager userManager;
	@EJB
	private ThesisAssignmentManager thesisAssignmentManager;

	private UserDto selectedUserDto;
	private List<UserDto> userDtos;

	public UserSelectionBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.setDefaultValues();
	}

	public String loadPage() {
		return HARVESTING_THESIS_RECORD_ASSIGNMENT_USER_SELECTION_WEBAPP_PATH + "?faces-redirect=true";
	}

	public String onPageLoad() {
		this.setDefaultValues();
		return null;
	}

	public void setDefaultValues() {
		try {
			this.userDtos = userManager
					.findAllUserDtosByWebappRelatedPath(HARVESTING_THESIS_RECORD_DATA_EXTRACTION_WEBAPP_PATH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.userDtos = new ArrayList<>();
		}
	}

	public int findIndexOfUserDto(List<UserDto> userDtos, UserDto userDto) {
		int index = -1;
		try {
			for (int i = 0; i < userDtos.size(); i++) {
				if (userDtos.get(i).getId() == userDto.getId())
					index = i;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return index;
	}

	public int findUndispatchedThesisAssignmentsCountByUserId(int userId) {
		return thesisAssignmentManager.countUndispatchedThesisAssignmentsByUserId(userId);
	}

	public int findUndispatchedSurveysCountByUserId(int userId) {
		return thesisAssignmentManager.countUndispatchedSurveysByUserId(userId);
	}
	
	public UserDto getSelectedUserDto() {
		return selectedUserDto;
	}

	public void setSelectedUserDto(UserDto selectedUserDto) {
		this.selectedUserDto = selectedUserDto;
	}

	public List<UserDto> getUserDtos() {
		return userDtos;
	}

	public void setUserDtos(List<UserDto> userDtos) {
		this.userDtos = userDtos;
	}

}
