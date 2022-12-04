package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.SurveyAssignment;
import swimsEJB.model.harvesting.entities.UnexpectedAnswer;

/**
 * Session Bean implementation class ResponseManager
 */
@Stateless
@LocalBean
public class UnexpectedAnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private QuestionManager questionManager;

	/**
	 * Default constructor.
	 */
	public UnexpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public UnexpectedAnswer createOneUnexpectedAnswer(Question question, String answer,
			SurveyAssignment surveyAssignment) throws Exception {
		UnexpectedAnswer unexpectedAnswer = new UnexpectedAnswer();
		unexpectedAnswer.setQuestion(question);
		unexpectedAnswer.setAnswer(answer);
		unexpectedAnswer.setSurveyAssignment(surveyAssignment);
		unexpectedAnswer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		unexpectedAnswer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (UnexpectedAnswer) daoManager.createOne(unexpectedAnswer);
	}

	public UnexpectedAnswer createOneUnexpectedAnswer(String limesurveyQuestionTitle, String answer,
			SurveyAssignment surveyAssignment) throws Exception {
		Question question = questionManager.findOneByQuestionTitle(limesurveyQuestionTitle);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneUnexpectedAnswer(question, answer, surveyAssignment);
	}
}
