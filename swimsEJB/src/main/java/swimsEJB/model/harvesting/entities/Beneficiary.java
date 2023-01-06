package swimsEJB.model.harvesting.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the beneficiaries database table.
 * 
 */
@Entity
@Table(name="beneficiaries", schema="harvesting")
@NamedQuery(name="Beneficiary.findAll", query="SELECT b FROM Beneficiary b")
public class Beneficiary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=64)
	private String name;

	public Beneficiary() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}