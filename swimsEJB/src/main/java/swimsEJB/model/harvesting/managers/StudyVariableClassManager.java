package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.StudyVariableClass;

/**
 * Session Bean implementation class StudyVariableClassManager
 */
@Stateless
@LocalBean
public class StudyVariableClassManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public StudyVariableClassManager() {
		// TODO Auto-generated constructor stub
	}

	public StudyVariableClass createOneStudyVariableClass(String longName, String shortName) throws Exception {
		StudyVariableClass studyVariableClass = new StudyVariableClass();
		studyVariableClass.setLongName(longName);
		studyVariableClass.setShortName(shortName);
		studyVariableClass.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		studyVariableClass.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (StudyVariableClass) daoManager.createOne(studyVariableClass);
	}
	
	
}