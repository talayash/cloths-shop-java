package system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import database.ShopDB;
import enums.ProductType;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ShopSystem {

	// Singelton variable
	private static ShopSystem instance = new ShopSystem();
	
	// Instance of Database class
	private static ShopDB shopDB = ShopDB.getInstance();
	
	// Attributes (Shop System data structure)
	private static HashMap<Integer, Product> _products = new HashMap<Integer, Product>();
	private static HashMap<Integer, Customer> _customers = new HashMap<Integer, Customer>();
	private static HashMap<Integer, Order> _orders = new HashMap<Integer, Order>();
	private static HashMap<Customer, Cart> _carts = new HashMap<Customer, Cart>();	
	private static HashMap<Product,Node> _cartNodes = new HashMap<Product,Node>();
	private  int counter;
	private  String orderPrint;

	// Constructor
	public ShopSystem() {}
	
	
	// Get Instance
	public static ShopSystem getInstance() {
		return instance;
	}
	
	
	// Current Customer on Shop
	private Customer _currentCustomer;
	public void setCurrentCustomer(Customer customer) {
		_currentCustomer = customer;
	}
	public Customer getCurrentCustomer() {
		return _currentCustomer;
	}
	
	// Current Cart
	private Cart _currentCart;
	public void setCurrentCart(Cart cart) {
		_currentCart = cart;
	}
	public Cart getCurrentCart() {
		return _currentCart;
	}
	
	
	// Counter for Order
	private int orderCounter;
	public void setOrderCounter(int id) {orderCounter = id+1;}
	public int getOrderCounter() {return orderCounter++;}
	
	// Counter for Invoice (Base on Order counter)
	public int getInvoiceCounter() {return orderCounter;}
	
	// Counter for Product
	private int productCounter;
	public void setProductCounter(int id) {productCounter = id+1;}
	public int getProductCounter() {return productCounter++;}

	// Setters
	public void setProductsHashMap(HashMap<Integer, Product> products) {
		_products = products;
	}
	public void setCustomersHashMap(HashMap<Integer, Customer> customers) {
		_customers = customers;
	}
	public void setOrdersHashMap(HashMap<Integer, Order> orders) {
		_orders = orders;
	}
	public void setCartsHashMap(HashMap<Customer, Cart> carts) {
		_carts = carts;
	}
		
	// Getters
	public HashMap<Integer, Product> getProducts(){return _products;}
	public HashMap<Integer, Customer> getCustomers(){return _customers;}
	public HashMap<Integer, Order> getOrders(){return _orders;}
	public HashMap<Customer, Cart> getCarts(){return _carts;}
	public HashMap<Product,Node> getCartNodes(){return _cartNodes;}
	
	// Add Customer
	public void addCustomer(Customer customer) throws SQLException{
		if(!_customers.containsKey(customer.getUserID())) {
			_customers.put(customer.getUserID(), customer);
			addCart(new Cart(), customer);

		}		
	}
	
	// Remove Customer
	public void removeCustomer(Customer customer) {
		if(_customers.containsKey(customer.getUserID())) {
			_customers.remove(customer.getUserID());
			Cart cart = _carts.get(customer);
			removeCart(cart, customer);
		}
	}
	
	// Add Cart
	public void addCart(Cart cart, Customer customer) {
		if(!_carts.containsKey(customer))
			_carts.put(customer, cart);
	}
	
	// Remove Cart
	public void removeCart(Cart cart, Customer customer) {
		if(_carts.containsKey(customer))
			_carts.remove(customer);
	}
	
	// Add Product
	public void addProduct(Product product) {
		if(!_products.containsKey(product.getProductID()))
			_products.put(product.getProductID(), product);
	}
	
	// Remove Product
	public void removeProduct(Product product) {
		if(_products.containsKey(product.getProductID()))
			_products.remove(product.getProductID());
	}
		
	// From Cart to Orders
	// After purchase made the cart get clear and all products move to orders
	public void fromCartToOrders() throws SQLException {
		
		// Fill orders HashMap with the products and customers
		ArrayList<Product> products = new ArrayList<Product>(_currentCart.getProductList());
			
		// Create new Invoice for the customer and add to DB
		Invoice invoice = new Invoice(_currentCustomer, _currentCart);
		
		String quary = "insert into Invoices values ("+invoice.getCustomer().getUserID()+","
												 + "'"+invoice.getDate().toString()+"',"
												 + "'"+invoice.getInvoicePrint()+"');";
		
		shopDB.executeQuary(quary);
		
		// Create new Order after payment
		Order order = new Order(_currentCustomer, products, invoice);		
		_orders.put(order.getID(), order);
		
		// Insert order detail into Orders table in database
		order.getProducts().forEach((product) -> {
			try {
				shopDB.executeQuary("INSERT INTO Orders VALUES ('" + order.getID() + "',"
															 + "'" + _currentCustomer.getUserID()  +"',"
															 +  "" + product.getProductID() + ");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		// Clear cart
		_currentCart.clear();
	}
	
	// Return amount of products in the shop sorted by product type
	public int getSizeByType(ProductType type) {
		counter = 0;
		_products.forEach((id, product) -> {
			if(product.getProductType().equals(type))
				counter++;
		});
		return counter;
	}
	
	// Get List object of Products
	public List<Product> getListOfProducts(){
		
		Product[] productArray = new Product[_products.size()];
		_products.forEach((id, product) -> {
			productArray[id] = product;
		});
		
		List<Product> list = Arrays.asList(productArray);
		
		return list;	
	}
	
	// Return amount of products in the shop sorted by product type using Streams
		public int getSizeByTypeWithStreamUsing(ProductType type) {
			
			List<Product> filteredByType = 
					getListOfProducts()
					.stream()
					.filter(product -> product.getProductType().equals(type))
					.collect(Collectors.toList());
			return filteredByType.size();
		}
		
	// Get String with prints of all of the orders
	public String getOrdersPrint() {
		orderPrint = "";
		
		_orders.forEach((id, order)->{
			orderPrint += "Customer ID: " + order.getCustomer().getUserName() +
					"\nOrder ID: " + order.getID() + 
					"\n" + order.getInvoice().getInvoicePrint();
		});
		
		return orderPrint;
	}
	
	
	
	// Check if String input is a Integer
	public boolean checkInputInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}		
	}
	
	// Check if a String input is a Double
	public boolean checkInputDouble(String input) {
		if(!input.isEmpty()) {
			try {
				Double.parseDouble(input);
				return true;
			}
			catch(NumberFormatException nfe) {
				return false;
			}	
		}
		return false;
	}
	
	// Alert dialog before deleting any object
	public boolean deleteDialog(String name) {
		
		// Create Alert dialog to confirm if user want to delete current object
		// Also add title, icon and message
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Are you sure?");
    	Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(new Image("ui/img/logo.png"));   	
    	String alertMessage = "Are you sure that you want to delete " + name + " ?";
    	alert.setContentText(alertMessage);    	 
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	// If yes return true
    	if ((result.isPresent()) && (result.get() == ButtonType.OK)) 
    		return true;
    	
    	return false;   	
	}
	
	public void errorDialog(String type ,String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(type);
		alert.setHeaderText("Error Alert");
		alert.setContentText(msg);
		alert.show();
	}


}
