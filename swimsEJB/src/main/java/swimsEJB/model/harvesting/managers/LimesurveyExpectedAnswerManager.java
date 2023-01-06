package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.LimesurveyExpectedAnswer;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;

/**
 * Session Bean implementation class ResponseManager
 */
@Stateless
@LocalBean
public class LimesurveyExpectedAnswerManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private LimesurveyQuestionManager limesurveyQuestionManager;

	/**
	 * Default constructor.
	 */
	public LimesurveyExpectedAnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public LimesurveyExpectedAnswer createOneLimesurveyExpectedAnswer(LimesurveyQuestion question, String answer,
			LimesurveySurveyAssignment surveyAssignment) throws Exception {
		LimesurveyExpectedAnswer expectedAnswer = new LimesurveyExpectedAnswer();
		expectedAnswer.setLimesurveyQuestion(question);
		expectedAnswer.setAnswer(answer);
		expectedAnswer.setLimesurveySurveyAssignment(surveyAssignment);
		expectedAnswer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		expectedAnswer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveyExpectedAnswer) daoManager.createOne(expectedAnswer);
	}

	public LimesurveyExpectedAnswer createOneLimesurveyExpectedAnswer(int limesurveyLimesurveyQuestionId, String limesurveyAnswerCode,
			LimesurveySurveyAssignment surveyAssignment) throws Exception {
		LimesurveyQuestion question = limesurveyQuestionManager.findOneQuestionById(limesurveyLimesurveyQuestionId);
		if (question == null)
			throw new Exception("La Pregunta especificada no se encuentra registrada.");
		return createOneLimesurveyExpectedAnswer(question, limesurveyAnswerCode, surveyAssignment);
	}
}
