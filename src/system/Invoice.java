package system;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import database.ShopDB;

public class Invoice implements Serializable{
	
	
	// Attributes
	private int _id;
	private Customer _customer;
	private Cart _cart;
	private String _invoicePrint;
	private Date _date;
	private static final long serialVersionUID = 1L;
	
	
	// Constructor
	public Invoice(Customer customer, Cart cart) throws SQLException {
		_id = ShopDB.getInstance().getTableCount("Invoices");
		setCustomer(customer);
		setCart(cart);
		_date = new Date();
		
	}
	
	// Setters
	public void setCustomer(Customer customer) {
		_customer = customer;
	}
	
	public void setCart(Cart cart) {
		_cart = new Cart(cart);
	}
	
	public void setInvoicePrint(String input) {
		_invoicePrint = input;
	}
	
	// Getters
	public int getInvoiceID() {
		return _id;
	}
	public Customer getCustomer() {
		return _customer;
	}
	public Cart getCart() {
		return _cart;
	}
	public Date getDate() {
		return _date;
	}
	
	// Get String of all of the invoice details (ID, Products, Products amount and total price)
	public String getInvoicePrint() {
		_invoicePrint = "Invoice ID: " + _id
				+ "\nDate: " + _date.toString()
				+"\nProducts: "; 
		
		_cart.getProductList().forEach((product) -> {
			_invoicePrint +="\n    - " +  product.getProductName() + " || " + product.getProductPrice();
		});
		_invoicePrint += "\n\nProduct Amount: " + _cart.getNumberOfProductsInCart() +
				"\nTotal: " + String.format("%.2f", _cart.getTotalPriceInCart()) + "\n\n";
				
		return _invoicePrint;		
	}
	
	public String toString() {
		return "";
	}
}
