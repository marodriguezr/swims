package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.SurveyAssignment;

/**
 * Session Bean implementation class ResponseManager
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

	public ExpectedAnswer createOneExpectedAnswer(Question question, String answer,
			SurveyAssignment surveyAssignment) throws Exception {
		ExpectedAnswer expectedAnswer = new ExpectedAnswer();
		expectedAnswer.setQuestion(question);
		expectedAnswer.setAnswer(answer);
		expectedAnswer.setSurveyAssignment(surveyAssignment);
		expectedAnswer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		expectedAnswer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (ExpectedAnswer) daoManager.createOne(expectedAnswer);
	}

	public ExpectedAnswer createOneExpectedAnswer(int limesurveyQuestionId, String limesurveyAnswerCode,
			SurveyAssignment surveyAssignment) throws Exception {
		Question question = questionManager.findOneQuestionById(limesurveyQuestionId);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneExpectedAnswer(question, limesurveyAnswerCode, surveyAssignment);
	}
}
