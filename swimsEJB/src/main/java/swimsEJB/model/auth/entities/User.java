package swimsEJB.model.auth.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users",schema = "auth")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(nullable=false, length=2147483647)
	private String email;

	@Column(name="first_name", nullable=false, length=2147483647)
	private String firstName;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="is_super_user", nullable=false)
	private Boolean isSuperUser;

	@Column(name="last_name", nullable=false, length=2147483647)
	private String lastName;

	@Column(nullable=false, length=2147483647)
	private String password;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to UserGroup
	@OneToMany(mappedBy="user")
	private List<UserGroup> usersGroups;

	public User() {
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsSuperUser() {
		return this.isSuperUser;
	}

	public void setIsSuperUser(Boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<UserGroup> getUsersGroups() {
		return this.usersGroups;
	}

	public void setUsersGroups(List<UserGroup> usersGroups) {
		this.usersGroups = usersGroups;
	}

	public UserGroup addUsersGroup(UserGroup usersGroup) {
		getUsersGroups().add(usersGroup);
		usersGroup.setUser(this);

		return usersGroup;
	}

	public UserGroup removeUsersGroup(UserGroup usersGroup) {
		getUsersGroups().remove(usersGroup);
		usersGroup.setUser(null);

		return usersGroup;
	}

}