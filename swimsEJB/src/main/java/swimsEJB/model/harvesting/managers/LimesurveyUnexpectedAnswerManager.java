package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;
import swimsEJB.model.harvesting.entities.LimesurveyUnexpectedAnswer;

/**
 * Session Bean implementation class ResponseManager
 */
@Stateless
@LocalBean
public class LimesurveyUnexpectedAnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;

	/**
	 * Default constructor.
	 */
	public LimesurveyUnexpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public LimesurveyUnexpectedAnswer createOneLimesurveyUnexpectedAnswer(LimesurveyQuestion question, String answer,
			LimesurveySurveyAssignment surveyAssignment) throws Exception {
		LimesurveyUnexpectedAnswer unexpectedAnswer = new LimesurveyUnexpectedAnswer();
		unexpectedAnswer.setLimesurveyQuestion(question);
		unexpectedAnswer.setAnswer(answer);
		unexpectedAnswer.setLimesurveySurveyAssignment(surveyAssignment);
		unexpectedAnswer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		unexpectedAnswer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveyUnexpectedAnswer) daoManager.createOne(unexpectedAnswer);
	}

	public LimesurveyUnexpectedAnswer createOneLimesurveyUnexpectedAnswer(int limesurveyLimesurveyQuestionId, String answer,
			LimesurveySurveyAssignment surveyAssignment) throws Exception {
		LimesurveyQuestion question = limesurveyQuestionManager.findOneQuestionById(limesurveyLimesurveyQuestionId);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneLimesurveyUnexpectedAnswer(question, answer, surveyAssignment);
	}
}
