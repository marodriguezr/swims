package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.SurveyAssignment;
import swimsEJB.model.harvesting.entities.UncodedExpectedAnswer;

/**
 * Session Bean implementation class ResponseManager
 */
@Stateless
@LocalBean
public class UncodedExpectedAnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private QuestionManager questionManager;

	/**
	 * Default constructor.
	 */
	public UncodedExpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public UncodedExpectedAnswer createOneUncodedExpectedAnswer(Question question, String answer,
			SurveyAssignment surveyAssignment) throws Exception {
		UncodedExpectedAnswer uncodedExpectedAnswer = new UncodedExpectedAnswer();
		uncodedExpectedAnswer.setQuestion(question);
		uncodedExpectedAnswer.setAnswer(answer);
		uncodedExpectedAnswer.setSurveyAssignment(surveyAssignment);
		uncodedExpectedAnswer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		uncodedExpectedAnswer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (UncodedExpectedAnswer) daoManager.createOne(uncodedExpectedAnswer);
	}

	public UncodedExpectedAnswer createOneUncodedExpectedAnswer(int limesurveyQuestionId, String answer,
			SurveyAssignment surveyAssignment) throws Exception {
		Question question = questionManager.findOneQuestionById(limesurveyQuestionId);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneUncodedExpectedAnswer(question, answer, surveyAssignment);
	}
}
