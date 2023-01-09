package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.Question;

/**
 * Session Bean implementation class AnswerManager
 */
@Stateless
@LocalBean
public class ExpectedAnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private QuestionManager questionManager;

	/**
	 * Default constructor.
	 */
	public ExpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public ExpectedAnswer createOneExpectedAnswer(String answer, Question question) throws Exception {
		if (answer.isBlank())
			throw new Exception("Debe ingresar una respuesta.");
		if (question == null)
			throw new Exception("Debe proveer una pregunta.");
		if (answer.length() > 256)
			throw new Exception("Debe ingresar 256 caracteres o menos como respuesta.");

		ExpectedAnswer answer2 = new ExpectedAnswer();

		answer2.setExpectedAnswer(answer);
		answer2.setQuestion(question);
		;
		answer2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		answer2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (ExpectedAnswer) daoManager.createOne(answer2);
	}

	public ExpectedAnswer createOneExpectedAnswer(String answer, String studyVariableId) throws Exception {
		Question question = questionManager.findOneQuestionByStudyVariableId(answer);
		return createOneExpectedAnswer(answer, question);
	}

	@SuppressWarnings("unchecked")
	public List<ExpectedAnswer> findManyExpectedAnswersByStudyVariableId(String studyVariableId) {
		return daoManager.findManyWhere(ExpectedAnswer.class, "o.question.studyVariable.id = '" + studyVariableId + "'",
				null);
	}

	public ExpectedAnswer findOneExpectedAnswerById(int expectedAnswerId) throws Exception {
		return (ExpectedAnswer) daoManager.findOneById(ExpectedAnswer.class, expectedAnswerId);
	}
}
