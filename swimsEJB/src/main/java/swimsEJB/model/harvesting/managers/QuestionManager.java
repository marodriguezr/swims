package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.StudyVariable;

/**
 * Session Bean implementation class QuestionManager
 */
@Stateless
@LocalBean
public class QuestionManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public QuestionManager() {
		// TODO Auto-generated constructor stub
	}

	public Question createOneQuestion(String question, StudyVariable studyVariable) throws Exception {
		if (question.isBlank())
			throw new Exception("Debe proveer una pregunta.");
		if (studyVariable == null)
			throw new Exception("Debe proveer una variable de estudio.");

		Question question2 = new Question();
		question2.setQuestion(question);
		question2.setStudyVariable(studyVariable);
		question2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		question2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (Question) daoManager.createOne(question2);
	}

	public Question findOneQuestionByStudyVariableId(String studyVarableId) {
		return (Question) daoManager.findOneWhere(Question.class, "o.studyVariable.id = '" + studyVarableId + "'");
	}
}
