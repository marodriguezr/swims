package swimsEJB.model.auth.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the permissions database table.
 * 
 */
@Entity
@Table(name="permissions", schema = "auth")
@NamedQuery(name="Permission.findAll", query="SELECT p FROM Permission p")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(nullable=false, length=2147483647)
	private String name;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(name="webapp_related_path", nullable=false, length=2147483647)
	private String webappRelatedPath;

	//bi-directional many-to-one association to GroupPermission
	@OneToMany(mappedBy="permission")
	private List<GroupPermission> groupsPermissions;

	public Permission() {
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

	public String getWebappRelatedPath() {
		return this.webappRelatedPath;
	}

	public void setWebappRelatedPath(String webappRelatedPath) {
		this.webappRelatedPath = webappRelatedPath;
	}

	public List<GroupPermission> getGroupsPermissions() {
		return this.groupsPermissions;
	}

	public void setGroupsPermissions(List<GroupPermission> groupsPermissions) {
		this.groupsPermissions = groupsPermissions;
	}

	public GroupPermission addGroupsPermission(GroupPermission groupsPermission) {
		getGroupsPermissions().add(groupsPermission);
		groupsPermission.setPermission(this);

		return groupsPermission;
	}

	public GroupPermission removeGroupsPermission(GroupPermission groupsPermission) {
		getGroupsPermissions().remove(groupsPermission);
		groupsPermission.setPermission(null);

		return groupsPermission;
	}

}