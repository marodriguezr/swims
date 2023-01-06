package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the questions database table.
 * 
 */
@Entity
@Table(name="questions", schema="harvesting")
@NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(nullable=false, length=512)
	private String question;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to ExpectedAnswer
	@OneToMany(mappedBy="question")
	private List<ExpectedAnswer> expectedAnswers;

	//bi-directional many-to-one association to StudyVariable
	@ManyToOne
	@JoinColumn(name="study_variable_id", nullable=false)
	private StudyVariable studyVariable;

	public Question() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<ExpectedAnswer> getExpectedAnswers() {
		return this.expectedAnswers;
	}

	public void setExpectedAnswers(List<ExpectedAnswer> expectedAnswers) {
		this.expectedAnswers = expectedAnswers;
	}

	public ExpectedAnswer addExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().add(expectedAnswer);
		expectedAnswer.setQuestion(this);

		return expectedAnswer;
	}

	public ExpectedAnswer removeExpectedAnswer(ExpectedAnswer expectedAnswer) {
		getExpectedAnswers().remove(expectedAnswer);
		expectedAnswer.setQuestion(null);

		return expectedAnswer;
	}

	public StudyVariable getStudyVariable() {
		return this.studyVariable;
	}

	public void setStudyVariable(StudyVariable studyVariable) {
		this.studyVariable = studyVariable;
	}

}