package system;

import java.io.Serializable;

public abstract class User implements Serializable{


	// Attributes
	protected int _id;
	protected String _name;
	private static final long serialVersionUID = 1L;
	
	
	//Constructor
	protected User(int id, String name) {
		setID(id);
		setName(name);
	}
	
	
	// Setters
	public void setID(int id) {
		_id = id;
	}
	
	public void setName(String name) {
		_name = name;
	}
	

	// Getters
	public int getUserID() {
		return _id;
	}
	
	public String getUserName() {
		return _name;
	}	
	
	public String toString() {
		return "";
	}
}

