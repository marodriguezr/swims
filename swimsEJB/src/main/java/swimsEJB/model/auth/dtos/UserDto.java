package swimsEJB.model.auth.dtos;

public class UserDto {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private boolean isSuperUser;
	private boolean isActive;

	public UserDto() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSuperUser() {
		return isSuperUser;
	}

	public void setSuperUser(boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
