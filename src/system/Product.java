package system;

import java.io.Serializable;
import java.util.Date;
import enums.ProductType;

public class Product implements Serializable{
	
	// Attributes
	private int _id;
	private String _name;
	private double _price;
	private Date _joinDate;
	private ProductType _type;
	private String _imageURL;
	private static final long serialVersionUID = 1L;
		
	// Constructor
	public Product(int id, String name, double price, ProductType type, String imageURL) {
		_id = id;
		setProductName(name);
		setProductPrice(price);
		setProductType(type);
		setImageURL(imageURL);
		_joinDate = new Date();
	}
	
	
	// Setters
	public void setID(int id) {
		_id = id;
	}
	
	public void setProductName(String name) {
		_name = name;	
	}
	
	public void setProductPrice(double price) {
		_price = price;
	}
	
	public void setProductType(ProductType type) {
		_type = type;
	}
	public void setImageURL(String imageURL) {
		_imageURL = imageURL;
	}
	
	
	// Getters
	public int getProductID() {
		return _id;
	}
	public String getProductName() {
		return _name;
	}
	public double getProductPrice() {
		return _price;
	}
	public Date getJoinDate() {
		return _joinDate;
	}
	public ProductType getProductType() {
		return _type;
	}
	public String getImageURL() {
		return _imageURL;
	}
		
	// toString
	public String toString() {
		return "[Product ID: " + _id + ", Name: " + _name + ", Price: " + _price + ", Join Date: " + _joinDate + ", Type: " + _type + "]";
	}
}
