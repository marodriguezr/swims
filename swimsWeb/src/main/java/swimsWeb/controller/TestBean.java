package swimsWeb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import swimsEJB.model.CompoundTestRegister;
import swimsEJB.model.TestManager;
import swimsEJB.model.auth.managers.GroupManager;
import swimsEJB.model.core.managers.SeedManager;
import swimsEJB.model.harvesting.dtos.LimesurveySurveyDto;
import swimsEJB.model.harvesting.services.LimesurveyService;

@Named
@RequestScoped
public class TestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private GroupManager groupManager;
	@EJB
	private TestManager testManager;
	@EJB
	private SeedManager seedManager;
	List<CompoundTestRegister> compoundTestRegisters;

	public TestBean() {
		// TODO Auto-generated constructor stub
	}

	public void test() {
//		List<Integer> found = groupManager.findRootGroupUserIds();
//		for (Integer integer : found) {
//			System.out.println(integer);
//		}
//		try {
//			testManager.test();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		System.out.println(System.getenv().getOrDefault("LIMESURVEY_ADMIN_PASSWORD", "didnt work D:"));
		System.out.println(System.getenv("LIMESURVEY_BASE_URL"));
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext.redirect(
					System.getenv("LIMESURVEY_BASE_URL").toString() + "/index.php/admin/authentication/sa/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test2() {
		try {
			List<LimesurveySurveyDto> limeSurveySurveyDtos = LimesurveyService.listAllSurveys();
			for (LimesurveySurveyDto limeSurveySurveyDto : limeSurveySurveyDtos) {
				System.out.println(limeSurveySurveyDto.getSid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test3() {
		try {
			seedManager.seedStudyVariablesAndLimesurvey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void findAllCompoundRegisters() {
		this.compoundTestRegisters = testManager.findAllCompoundTestRegisters();
	}

	public List<CompoundTestRegister> getCompoundTestRegisters() {
		return compoundTestRegisters;
	}

	public void setCompoundTestRegisters(List<CompoundTestRegister> compoundTestRegisters) {
		this.compoundTestRegisters = compoundTestRegisters;
	}

}
