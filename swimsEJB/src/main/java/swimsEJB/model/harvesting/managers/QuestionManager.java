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

	public Question createOneQuestion(String limesurveyQuestionTitle, int limesurveySurveyId, int limesurveyQuestionId,
			StudyVariable studyVariable, Integer limesurveyParentQuestionId, String limesurveyParentQuestionTitle)
			throws Exception {
		Question question = new Question();
		question.setLimesurveyQuestionTitle(limesurveyQuestionTitle);
		question.setLimesurveySurveyId(limesurveySurveyId);
		question.setLimesurveyQuestionId(limesurveyQuestionId);
		question.setStudyVariable(studyVariable);
		question.setLimesurveyParentQuestionId(limesurveyParentQuestionId == null ? 0 : limesurveyParentQuestionId);
		question.setLimesurveyParentQuestionTitle(
				limesurveyParentQuestionTitle == null ? "" : limesurveyParentQuestionTitle);
		question.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		question.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (Question) daoManager.createOne(question);
	}

	public Question createOneQuestion(String limesurveyQuestionTitle, int limesurveySurveyId, int limesurveyQuestionId,
			StudyVariable studyVariable) throws Exception {
		return createOneQuestion(limesurveyQuestionTitle, limesurveySurveyId, limesurveyQuestionId, studyVariable, null,
				null);
	}

}
