package swimsEJB.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the oai_sets database table.
 * 
 */
@Entity
@Table(name="oai_sets")
@NamedQuery(name="OaiSet.findAll", query="SELECT o FROM OaiSet o")
public class OaiSet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=2147483647)
	private String id;

	@Column(nullable=false, length=2147483647)
	private String name;

	public OaiSet() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}