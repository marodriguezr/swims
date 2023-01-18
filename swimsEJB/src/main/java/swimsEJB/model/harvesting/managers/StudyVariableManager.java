package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

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
	@EJB
	private StudyVariableClassManager studyVariableClassManager;

	/**
	 * Default constructor.
	 */
	public StudyVariableManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<StudyVariable> findAllStudyVariables() {
		return daoManager.findAll(StudyVariable.class, "updatedAt", false);
	}

	public StudyVariable createOneStudyVariable(String id, String name, Boolean isNumericContinuous,
			Boolean isNumericDiscrete, boolean isCategoricalNominal, boolean isCategoricalOrdinal,
			StudyVariableClass studyVariableClass) throws Exception {
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
		StudyVariable studyVariable = new StudyVariable();
		studyVariable.setId(id);
		studyVariable.setName(name);
		studyVariable.setIsNumericContinuous(isNumericContinuous);
		studyVariable.setIsNumericDiscrete(isNumericDiscrete);
		studyVariable.setIsCategoricalNominal(isCategoricalNominal);
		studyVariable.setIsCategoricalOrdinal(isCategoricalOrdinal);
		studyVariable.setStudyVariableClass(studyVariableClass);
		studyVariable.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		studyVariable.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (StudyVariable) daoManager.createOne(studyVariable);
	}

	public StudyVariable createOneStudyVariable(String id, String name, Boolean isNumericContinuous,
			Boolean isNumericDiscrete, boolean isCategoricalNominal, boolean isCategoricalOrdinal,
			String studyVariableClassId) throws Exception {
		StudyVariableClass studyVariableClass = studyVariableClassManager
				.findOneStudyVariableClassById(studyVariableClassId);
		if (studyVariableClass == null)
			throw new Exception("El Identificador de la Clase de Variable de Estudio no está registrado.");
		return createOneStudyVariable(id, name, isNumericContinuous, isNumericDiscrete, isCategoricalNominal,
				isCategoricalOrdinal, studyVariableClass);
	}

	public StudyVariable updateOneStudyVariableById(String id, String name, Boolean isNumericContinuous,
			Boolean isNumericDiscrete, boolean isCategoricalNominal, boolean isCategoricalOrdinal,
			StudyVariableClass studyVariableClass) throws Exception {
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
		StudyVariable studyVariable = findOneStudyVariableById(id);
		if (studyVariable == null)
			throw new Exception("El Identificador de la Variable de Estudio no está registrado.");

		studyVariable.setName(name);
		studyVariable.setIsNumericContinuous(isNumericContinuous);
		studyVariable.setIsNumericDiscrete(isNumericDiscrete);
		studyVariable.setIsCategoricalNominal(isCategoricalNominal);
		studyVariable.setIsCategoricalOrdinal(isCategoricalOrdinal);
		studyVariable.setStudyVariableClass(studyVariableClass);
		studyVariable.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (StudyVariable) daoManager.updateOne(studyVariable);
	}

	public StudyVariable updateOneStudyVariableById(String id, String name, Boolean isNumericContinuous,
			Boolean isNumericDiscrete, boolean isCategoricalNominal, boolean isCategoricalOrdinal,
			String studyVariableClassId) throws Exception {
		StudyVariableClass studyVariableClass = studyVariableClassManager
				.findOneStudyVariableClassById(studyVariableClassId);
		if (studyVariableClass == null)
			throw new Exception("El Identificador de la Clase de Variable de Estudio no está registrado.");
		return updateOneStudyVariableById(id, name, isNumericContinuous, isNumericDiscrete, isCategoricalNominal,
				isCategoricalOrdinal, studyVariableClass);
	}

	public StudyVariable findOneStudyVariableById(String id) throws Exception {
		return (StudyVariable) daoManager.findOneById(StudyVariable.class, id);
	}
}
