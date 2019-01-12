package arquillian.tutorial.entity;

public class Users {

	private int id;

	private String firstname;

	private String lastname;

	private String address;

	private String email;

	private String username;

	private String password;

	public Users(String firstname, String lastname, String address, String email, String username, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Users() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}
	
	

}
