package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Answer;
import swimsEJB.model.harvesting.entities.ExpectedAnswer;
import swimsEJB.model.harvesting.entities.Question;
import swimsEJB.model.harvesting.entities.ThesisAssignment;

import static swimsEJB.constants.StudyVariables.BENEFICIARY_NAME_STUDY_VARIABLE_NAME;

/**
 * Session Bean implementation class AnswerManager
 */
@Stateless
@LocalBean
public class AnswerManager {
	@EJB
	private DaoManager daoManager;
	@EJB
	private QuestionManager questionManager;
	@EJB
	private ExpectedAnswerManager expectedAnswerManager;

	/**
	 * Default constructor.
	 */
	public AnswerManager() {
		// TODO Auto-generated constructor stub
	}

	public Answer createOneAnswer(Question question, ExpectedAnswer expectedAnswer, ThesisAssignment thesisAssignment)
			throws Exception {
		if (question == null)
			throw new Exception("Debe proveer una pregunta.");
		if (expectedAnswer == null)
			throw new Exception("Debe proveer una respuesta.");
		if (thesisAssignment == null)
			throw new Exception("Debe proveer una asignación de Tesis");

		Answer answer2 = new Answer();
		answer2.setQuestion(question);
		answer2.setExpectedAnswer(expectedAnswer);
		answer2.setThesisAssignment(thesisAssignment);
		answer2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		answer2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (Answer) daoManager.createOne(answer2);
	}

	public Answer dispatchBeneficiaryAnswer(int expectedAnswerId, ThesisAssignment thesisAssignment) throws Exception {
		ExpectedAnswer expectedAnswer = expectedAnswerManager.findOneExpectedAnswerById(expectedAnswerId);
		Question question = questionManager.findOneQuestionByStudyVariableId(BENEFICIARY_NAME_STUDY_VARIABLE_NAME);
		return createOneAnswer(question, expectedAnswer, thesisAssignment);
	}

	public Answer dispatchBeneficiaryAnswer(ExpectedAnswer answer, ThesisAssignment thesisAssignment) throws Exception {
		Question question = questionManager.findOneQuestionByStudyVariableId(BENEFICIARY_NAME_STUDY_VARIABLE_NAME);
		return createOneAnswer(question, answer, thesisAssignment);
	}

	public boolean isBeneficiaryStudyVariableAnsweredForThesisAssignent(ThesisAssignment thesisAssignment) {
		Answer foundExpectedAnswer = (Answer) daoManager.findOneWhere(Answer.class,
				"o.thesisAssignment.id = " + thesisAssignment.getId() + " AND o.question.studyVariable.id = '"
						+ BENEFICIARY_NAME_STUDY_VARIABLE_NAME + "'");
		return foundExpectedAnswer != null;
	}
}
