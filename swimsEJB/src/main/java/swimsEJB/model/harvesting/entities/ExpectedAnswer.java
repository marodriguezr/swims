package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the expected_answers database table.
 * 
 */
@Entity
@Table(name="expected_answers", schema="harvesting")
@NamedQuery(name="ExpectedAnswer.findAll", query="SELECT e FROM ExpectedAnswer e")
public class ExpectedAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="expected_answer")
	private String expectedAnswer;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="expectedAnswer")
	private List<Answer> answers;

	//bi-directional many-to-one association to Question
	@ManyToOne
	private Question question;

	public ExpectedAnswer() {
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

	public String getExpectedAnswer() {
		return this.expectedAnswer;
	}

	public void setExpectedAnswer(String expectedAnswer) {
		this.expectedAnswer = expectedAnswer;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setExpectedAnswer(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setExpectedAnswer(null);

		return answer;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}