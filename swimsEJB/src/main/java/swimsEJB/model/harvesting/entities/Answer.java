package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the answers database table.
 * 
 */
@Entity
@Table(name="answers", schema="harvesting")
@NamedQuery(name="Answer.findAll", query="SELECT a FROM Answer a")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to ExpectedAnswer
	@ManyToOne
	@JoinColumn(name="expected_answer_id")
	private ExpectedAnswer expectedAnswer;

	//bi-directional many-to-one association to Question
	@ManyToOne
	private Question question;

	//bi-directional many-to-one association to ThesisAssignment
	@ManyToOne
	@JoinColumn(name="thesis_assignment_id")
	private ThesisAssignment thesisAssignment;

	public Answer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ExpectedAnswer getExpectedAnswer() {
		return this.expectedAnswer;
	}

	public void setExpectedAnswer(ExpectedAnswer expectedAnswer) {
		this.expectedAnswer = expectedAnswer;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public ThesisAssignment getThesisAssignment() {
		return this.thesisAssignment;
	}

	public void setThesisAssignment(ThesisAssignment thesisAssignment) {
		this.thesisAssignment = thesisAssignment;
	}

}