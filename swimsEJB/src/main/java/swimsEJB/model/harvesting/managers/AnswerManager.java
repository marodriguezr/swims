package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Answer;
import swimsEJB.model.harvesting.entities.StudyVariable;

/**
 * Session Bean implementation class AnswerManager
 */
@Stateless
@LocalBean
public class AnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private StudyVariableManager studyVariableManager;

	/**
	 * Default constructor.
	 */
	public AnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public Answer createOneAnswer(String answer, StudyVariable studyVariable) throws Exception {
		if (answer.isBlank())
			throw new Exception("Debe ingresar una respuesta.");
		if (studyVariable == null)
			throw new Exception("Debe proveer una variable de estudio.");
		if (answer.length() > 256)
			throw new Exception("Debe ingresar 256 caracteres o menos como respuesta.");

		Answer answer2 = new Answer();

		answer2.setAnswer(answer);
		answer2.setStudyVariable(studyVariable);
		answer2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		answer2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (Answer) daoManager.createOne(answer2);
	}

	public Answer createOneAnswer(String answer, String studyVariableId) throws Exception {
		StudyVariable studyVariable = studyVariableManager.findOneStudyVariableById(studyVariableId);
		return createOneAnswer(answer, studyVariable);
	}

	@SuppressWarnings("unchecked")
	public List<Answer> findManyAnswersByStudyVariableId(String studyVariableId) {
		return daoManager.findManyWhere(Answer.class, "o.studyVariable.id = '" + studyVariableId + "'", null);
	}
}
