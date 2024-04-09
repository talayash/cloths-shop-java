package system;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import database.ShopDB;

public class Order implements Serializable{


	// Attributes
	private int _id;
	private Customer _customer;
	private ArrayList<Product> _products;
	private Invoice _invoice;
	private static final long serialVersionUID = 1L;
	
	// Constructor
	public Order(Customer customer, ArrayList<Product> products, Invoice invoice) throws SQLException {
		
		_id = ShopDB.getInstance().getOrderTableCountByCustomer();
		setCustomer(customer);
		setProductArrayList(products);
		setInvoice(invoice);
		
	}
	
	// Setters
	public void setCustomer(Customer customer) {
		_customer = customer;
	}
	
	public void setProductArrayList(ArrayList<Product> products) {
		_products = products;
	}
	
	public void setInvoice(Invoice invoice) {
		_invoice = invoice;
	}
	
	// Getters
	public int getID() {
		return _id;
	}
	
	public Customer getCustomer() {
		return _customer;
	}
	
	public ArrayList<Product> getProducts(){
		return _products;
	}
	
	public Invoice getInvoice() {
		return _invoice;
	}
	
	// toString
	public String toString() {
		return "";
	}

}
