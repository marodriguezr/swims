package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.ThesisAssignment;

import static swimsEJB.constants.StudyVariables.BENEFICIARY_NAME_STUDY_VARIABLE_NAME;

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

	public ExpectedAnswer createOneAnswer(Question question, String answer, ThesisAssignment thesisAssignment)
			throws Exception {
		if (question == null)
			throw new Exception("Debe proveer una pregunta.");
		if (answer.isBlank())
			throw new Exception("Debe proveer una respuesta.");
		if (thesisAssignment == null)
			throw new Exception("Debe proveer una asignación de Tesis");

		ExpectedAnswer answer2 = new ExpectedAnswer();
		answer2.setQuestion(question);
		answer2.setAnswer(answer);
		answer2.setThesisAssignment(thesisAssignment);
		answer2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		answer2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (ExpectedAnswer) daoManager.createOne(answer2);
	}

	public ExpectedAnswer dispatchBeneficiaryAnswer(String answer, ThesisAssignment thesisAssignment) throws Exception {
		Question question = questionManager.findOneQuestionByStudyVariableId(BENEFICIARY_NAME_STUDY_VARIABLE_NAME);
		return createOneAnswer(question, answer, thesisAssignment);
	}

	public boolean isBeneficiaryStudyVariableAnsweredForThesisAssignent(ThesisAssignment thesisAssignment) {
		ExpectedAnswer foundExpectedAnswer = (ExpectedAnswer) daoManager.findOneWhere(ExpectedAnswer.class,
				"o.thesisAssignment.id = " + thesisAssignment.getId() + " AND o.question.studyVariable.id = '"
						+ BENEFICIARY_NAME_STUDY_VARIABLE_NAME + "'");
		return foundExpectedAnswer != null;
	}
}
