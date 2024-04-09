package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import system.*;

////////////////////////////////////////////////////////////////

// ** Singelton Class for Save and Load method to SER file **
// ** Using Local DB - A File in the Project folder **

//////////////////////////////////////////////////////////////

public class LocalSerDB {
	
	// Local Ser file class Attribuets
	private static LocalSerDB instance = new LocalSerDB();
	public LocalSerDB() {}	
	public static LocalSerDB getInstance() {
		return instance;
	}
	
	// ShopSystem instance
	private static ShopSystem shopSystem = ShopSystem.getInstance();
	
	// Save Data method
		public void saveData() {
			try {
				FileOutputStream fileOut = new FileOutputStream("data.ser");
	            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
	            
	            // Write objects to file            
	            objectOut.writeObject(shopSystem.getProducts());
	            objectOut.writeObject(shopSystem.getCustomers());
	            objectOut.writeObject(shopSystem.getOrders());
	            objectOut.writeObject(shopSystem.getCarts()); 
	            
	            objectOut.close();
	            fileOut.close();
	            System.out.println("Save data successful!");
			 } 
			catch (IOException e) {
		            System.out.println("Error initializing stream");
		    }
		}
		
		// Load Data method
		@SuppressWarnings("unchecked")
		public void loadData() throws IOException, ClassNotFoundException {
			
			try {
	         
	            FileInputStream fileIn = new FileInputStream("data.ser");
	            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				
	            // Read objects
	            HashMap<Integer, Product> products = (HashMap<Integer, Product>) objectIn.readObject(); 
	            shopSystem.setProductCounter(products.size());
	            shopSystem.setProductsHashMap(products);
				
	            HashMap<Integer, Customer> customers = (HashMap<Integer, Customer>) objectIn.readObject(); 
	            shopSystem.setCustomersHashMap(customers);
	            
	            HashMap<Integer, Order> orders = (HashMap<Integer, Order>) objectIn.readObject(); 
	            shopSystem.setOrderCounter(orders.size());
	            shopSystem.setOrdersHashMap(orders);
				
	            HashMap<Customer, Cart> carts = (HashMap<Customer, Cart>) objectIn.readObject(); 
	            shopSystem.setCartsHashMap(carts);
	            			 
	            objectIn.close();
	            fileIn.close();
	            System.out.println("Load data successful!");

	        } 
			catch (FileNotFoundException e) {
	            System.out.println("File not found, new one will be create after close the app.");
	        } 
			catch (IOException e) {
	            System.out.println("Error initializing stream.");
	        } 
			catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }       
		}

}
