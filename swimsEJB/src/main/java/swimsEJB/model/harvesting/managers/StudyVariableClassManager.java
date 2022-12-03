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

	public StudyVariableClass createOneStudyVariableClass(String id, String name) throws Exception {
		if (id.isBlank()) {
			throw new Exception("Debe ingresar un valor apropiado como identificador.");
		}
		if (id.length() > 32) {
			throw new Exception("Debe ingresar un valor con 32 caracteres o menos como identificador.");
		}
		if (name.isBlank()) {
			throw new Exception("Debe ingresar un valor apropiado como nombre.");
		}
		if (name.length() > 256) {
			throw new Exception("Debe ingresar un valor con 256 caracteres o menos como nombre.");
		}
		StudyVariableClass studyVariableClass = new StudyVariableClass();
		studyVariableClass.setId(id);
		studyVariableClass.setName(name);
		studyVariableClass.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		studyVariableClass.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (StudyVariableClass) daoManager.createOne(studyVariableClass);
	}
	
	
}