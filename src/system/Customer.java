package system;

public class Customer extends User{

	
	// Attributes
	private String _email;
	private String _dateOfBirth;
	private static final long serialVersionUID = 1L;
	
	
	// Constructor
	public Customer(int id, String name, String email, String dateOfBirth) {
		super(id, name);
		setEmail(email);
		setDateOfBirth(dateOfBirth);
	}
	
	
	// Setters
	public void setEmail(String email) {
		_email = email;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		_dateOfBirth = dateOfBirth;
	}
	
	
	// Getters
	public String getEmail() {
		return _email;
	}
	
	public String getDateOfBirth() {
		return _dateOfBirth;
	}

	
	// toString
	public String toString() {
		return "[Customer ID: " + super.getUserID() + ", Name: " + super.getUserName() + ", Email: " + _email + ", Date of birth:" + _dateOfBirth + "]";
	}
}
