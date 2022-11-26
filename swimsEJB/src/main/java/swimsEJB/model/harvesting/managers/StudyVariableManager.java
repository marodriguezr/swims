package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.StudyVariableClass;

/**
 * Session Bean implementation class StudyVariableManager
 */
@Stateless
@LocalBean
public class StudyVariableManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public StudyVariableManager() {
		// TODO Auto-generated constructor stub
	}

	public StudyVariable createOneStudyVariable(String longName, String shortName, Boolean isQuantitativeContinuous,
			Boolean isQuantitativeDiscrete, boolean isQualitative, boolean isLikert, boolean isBoolean,
			StudyVariableClass studyVariableClass) throws Exception {
		StudyVariable studyVariable = new StudyVariable();
		studyVariable.setLongName(longName);
		studyVariable.setShortName(shortName);
		studyVariable.setIsQuantitativeContinuous(isQuantitativeContinuous);
		studyVariable.setIsQuantitativeDiscrete(isQuantitativeDiscrete);
		studyVariable.setIsQualitative(isQualitative);
		studyVariable.setIsLikert(isLikert);
		studyVariable.setIsBoolean(isBoolean);
		studyVariable.setStudyVariableClass(studyVariableClass);
		studyVariable.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		studyVariable.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (StudyVariable) daoManager.createOne(studyVariable);
	}

}