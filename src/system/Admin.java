package system;
import java.util.Date;

public class Admin extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Attributes
	private String _city;
	private Date _joinDate;
	
	
	// Constructor
	public Admin(int id,String name, String city) {
		super(id, name);
		setAdminCity(city);
		_joinDate = new Date();
		
	}
	
	// Setters
	public void setAdminCity(String city) {
		_city = city;
	}
	
	
	// Getters
	public String getAdminCity() {
		return _city;
	}
	public Date getAdminJoinDate() {
		return _joinDate;
	}
	
	// toString
	public String toString() {
		return "[Admin ID: " + super.getUserID() + ", Name: " +super.getUserName() + ", City: " + _city + "Join Date: " + _joinDate + "]";
	}

}
