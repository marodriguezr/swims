package swimsEJB.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sysparams database table.
 * 
 */
@Entity
@Table(name="sysparams", schema = "core")
@NamedQuery(name="Sysparam.findAll", query="SELECT s FROM Sysparam s")
public class Sysparam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=2147483647)
	private String key;

	@Column(nullable=false, length=2147483647)
	private String value;

	public Sysparam() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}