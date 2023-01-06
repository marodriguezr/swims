package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.StudyVariable;

/**
 * Session Bean implementation class QuestionManager
 */
@Stateless
@LocalBean
public class LimesurveyQuestionManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public LimesurveyQuestionManager() {
		// TODO Auto-generated constructor stub
	}

	public LimesurveyQuestion createOneQuestion(String limesurveyQuestionTitle, int limesurveySurveyId, int limesurveyQuestionId,
			StudyVariable studyVariable) throws Exception {
		LimesurveyQuestion question = new LimesurveyQuestion();
		question.setLimesurveyQuestionTitle(limesurveyQuestionTitle);
		question.setLimesurveySurveyId(limesurveySurveyId);
		question.setLimesurveyQuestionId(limesurveyQuestionId);
		question.setStudyVariable(studyVariable);

		question.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		question.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveyQuestion) daoManager.createOne(question);
	}

	public LimesurveyQuestion findOneQuestionById(int id) throws Exception {
		return (LimesurveyQuestion) daoManager.findOneById(LimesurveyQuestion.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveyQuestion> findAllQuestionsByLimesurveySurveyId(int limesurveySurveyId) {
		return daoManager.findManyWhere(LimesurveyQuestion.class, "o.limesurveySurveyId = " + limesurveySurveyId, null);
	}
}
