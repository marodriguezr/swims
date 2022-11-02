package swimsEJB.model.auth.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="groups", schema = "auth")
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="is_root", nullable=false)
	private Boolean isRoot;

	@Column(nullable=false, length=2147483647)
	private String name;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	//bi-directional many-to-one association to GroupPermission
	@OneToMany(mappedBy="group")
	private List<GroupPermission> groupsPermissions;

	//bi-directional many-to-one association to UserGroup
	@OneToMany(mappedBy="group")
	private List<UserGroup> usersGroups;

	public Group() {
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

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsRoot() {
		return this.isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<GroupPermission> getGroupsPermissions() {
		return this.groupsPermissions;
	}

	public void setGroupsPermissions(List<GroupPermission> groupsPermissions) {
		this.groupsPermissions = groupsPermissions;
	}

	public GroupPermission addGroupsPermission(GroupPermission groupsPermission) {
		getGroupsPermissions().add(groupsPermission);
		groupsPermission.setGroup(this);

		return groupsPermission;
	}

	public GroupPermission removeGroupsPermission(GroupPermission groupsPermission) {
		getGroupsPermissions().remove(groupsPermission);
		groupsPermission.setGroup(null);

		return groupsPermission;
	}

	public List<UserGroup> getUsersGroups() {
		return this.usersGroups;
	}

	public void setUsersGroups(List<UserGroup> usersGroups) {
		this.usersGroups = usersGroups;
	}

	public UserGroup addUsersGroup(UserGroup usersGroup) {
		getUsersGroups().add(usersGroup);
		usersGroup.setGroup(this);

		return usersGroup;
	}

	public UserGroup removeUsersGroup(UserGroup usersGroup) {
		getUsersGroups().remove(usersGroup);
		usersGroup.setGroup(null);

		return usersGroup;
	}

}