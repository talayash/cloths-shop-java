package system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Cart implements Serializable{
	
	// Attributes
	private int _id;
	private ArrayList<Product> _products;
	private int _numberOfProducts;
	private double _totalPrice;
	private Date _date;
	static private int counter = 1;
	private static final long serialVersionUID = 1L;
	
	// Constructor
	public Cart() {
		_id = counter++;
		_products = new ArrayList<Product>();
		_numberOfProducts = 0;
		_totalPrice = 0;
		_date = new Date();
	}
	
	// Constructor for reset cases 
	public Cart(Cart cart) {
		_id = cart.getCartID();
		_products = new ArrayList<Product>(cart.getProductList());
		_numberOfProducts = cart.getNumberOfProductsInCart();
		_totalPrice = cart.getTotalPriceInCart();
		_date = cart.getDateOfCart();
	}
	
	// Getters
	public int getCartID() {
		return _id;
	}
	public ArrayList<Product> getProductList(){
		return _products;
	}
	public int getNumberOfProductsInCart() {
		return _numberOfProducts;
	}
	public double getTotalPriceInCart() {
		return _totalPrice;
	}
	public Date getDateOfCart() {
		return _date;
	}
	
	
	// Add Product
	public void addProductToCart(Product product) {
		// Add product to products cart list
		_products.add(product);
		
		// Update the amount of product
		updateNumberOfProducts();
		
		// Update the total price of the cart
		updateTotalPriceOnAdd(product.getProductPrice());
	}
	
	// Remove Product
	public void removeProductFromCart(Product product) {
		if(_products.contains(product)) {
			_products.remove(product);
			updateNumberOfProducts();
			updateTotalPriceOnRemove(product.getProductPrice());
		}
	}
	
	// Update amount of product in the cart
	public void updateNumberOfProducts() {
		_numberOfProducts = _products.size();
	}
	
	// Update cart total price while adding product
	public void updateTotalPriceOnAdd(double price) {
		_totalPrice += price;
	}
	
	// Update cart total price while removing product
	public void updateTotalPriceOnRemove(double price) {
		_totalPrice -= price;
	}
	
	// Clear the cart from product and reset counters
	public void clear() {
		_products = new ArrayList<Product>();
		_numberOfProducts = 0;
		_totalPrice = 0;
				
	}
	
	// Print the products on the cart
	// Used for testing
	public void printCartProducts() {
		
		// Run a forEach over all the products in the cart and print them
		_products.forEach((product) -> {
			System.out.println(product);
			});
	}
	
	// Get String with amount and total price of current cart
	public String cartDetail() {
		return "Products Amount: " + getNumberOfProductsInCart() 
		+ "\nTotal Price: " + String.format("%.2f", getTotalPriceInCart());
	}

	public String toString() {
		return "";
	}
}
