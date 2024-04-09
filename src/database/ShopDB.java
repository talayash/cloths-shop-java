package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import enums.ProductType;
import system.*;


public class ShopDB {
	
	// Create instance
	private static ShopDB instance = new ShopDB();
	
	// Database Attributes
	private static ShopSystem shopSystem = ShopSystem.getInstance();
	static String connectionUrl = "jdbc:sqlserver://localhost;databaseName=Shop;integratedSecurity=true;" ;  
	static Connection con = null;  
	static Statement stmt = null;  
	static ResultSet rs = null;
	
	// Constructor
	public ShopDB() {
		
	}
	
	// Get Instance
	public static ShopDB getInstance() {
		return instance;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	// Connect to Database
	public void loadDatabase() {

		try {    
			System.out.println("Trying to set a connection to 'Shop' Database ...");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
			con = DriverManager.getConnection(connectionUrl);
			initDB();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Connection to 'Shop' data base succeed!\n");
	}
	
	// Initialize data structures from DB
	public void initDB() {
		readCustomerFromDatabase();
		readProductsFromDatabase();
	}
	
	// Execute Quary for Table working
	public void setStatementAndResultSet(String quary) throws SQLException {
		// Check if user exists in database
		try{		
			// Run the statement
			stmt = con.createStatement();
			rs = stmt.executeQuery(quary);			
		}		
		catch(Exception ex){
			ex.printStackTrace();
		}				
	}
	
	
	// Execute Quary for outside method
	public void executeQuary(String quary) throws SQLException {
		// Check if user exists in database
		try{		
			// Run the statement
			Statement stmt = con.createStatement();
			stmt.execute(quary);			
		}		
		catch(Exception ex){
			ex.printStackTrace();
		}				
	}
		
	// Read customers from databae (for load)
	public void readCustomerFromDatabase() {		
		// SQL Quary
		String quary = "SELECT * FROM Customers";				
		
		try {  			
			// Create and execute an SQL statement that returns some data.  
			setStatementAndResultSet(quary);
			
			// Run a while loop over all of the table
			while (rs.next()) {
			    
				// Load Customer Attributes
			    int id = Integer.parseInt(rs.getObject(1).toString());
			    String name = rs.getObject(2).toString();;
			    String email = rs.getObject(3).toString();;
			    String birthDate = rs.getObject(4).toString();;
			    
			    // Create new customer and add to local data structor
			    Customer customer = new Customer(id, name, email, birthDate);
			    shopSystem.addCustomer(customer);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {  
			if (rs != null) try { rs.close(); } catch(Exception e) {}  
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}			
		}
	}
	
	// Read products from databae (for load)
	public void readProductsFromDatabase() {
		
		// SQL Quary
		String quary = "SELECT * FROM Products order by ProductType";
				
		try {  			
			// Create and execute an SQL statement that returns some data.  
			setStatementAndResultSet(quary); 
			
			// Run a while loop over all of the table
			while (rs.next()) {
			
				// Load Product values
				int id = Integer.parseInt(rs.getObject(1).toString());
			    String name = rs.getObject(2).toString();;
			    double price = Double.parseDouble(rs.getObject(3).toString());
			    ProductType type = getProductType(rs.getObject(5).toString());
			    String imageURL = rs.getObject(6).toString();;
    			
			    // Create product and add to local data structure
			    Product product = new Product(id, name, price, type, imageURL);
			    shopSystem.getProducts().put(product.getProductID(), product);
			}
		} 
		// Catch SQLException
		catch (SQLException e) {
			e.printStackTrace();
		} 
		// Finally close ResultSet and Statment
		finally {  
			if (rs != null) try { rs.close(); } catch(Exception e) {}  
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}			
		}
	}
	
	// Read customers from databae (for load)
	public void readCartFromDatabase() {		
		// SQL Quary
		String quary = "SELECT * FROM Carts WHERE CustomerID = "+shopSystem.getCurrentCustomer().getUserID()+"";				
		
		try {  			
			// Create and execute an SQL statement that returns some data.  
			setStatementAndResultSet(quary);
			
			// Run a while loop over all of the table
			while (rs.next()) {
			    
				// Load Customer Attributes
			    int id = Integer.parseInt(rs.getObject(1).toString());
			    String name = rs.getObject(2).toString();;
			    String email = rs.getObject(3).toString();;
			    String birthDate = rs.getObject(4).toString();;
			    
			    // Create new customer and add to local data structor
			    Customer customer = new Customer(id, name, email, birthDate);
			    shopSystem.addCustomer(customer);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {  
			if (rs != null) try { rs.close(); } catch(Exception e) {}  
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}			
		}
	}
	
	
	// Get product type from String to ProductType
	public ProductType getProductType(String input) {
		
		if(input.equals("Soccer"))
			return ProductType.Soccer;
		
		else if(input.equals("NBA"))
			return ProductType.NBA;
		
		else if(input.equals("PoloShirts"))
			return ProductType.PoloShirts;
		
		else if(input.equals("SportShoes"))
			return ProductType.SportShoes;
		
		else if(input.equals("Kids"))
			return ProductType.Kids;
		
		else
			return null;
	}
	
	// Check if customer exist in database
	public boolean isCustomerExists(String name, int id) throws SQLException {
		
		String quary = "select * from Customers where FullName = '"+ name +"' and ID = "+ id +" ";
		setStatementAndResultSet(quary);
		
		if(rs.next() == false) {
			stmt.close();
			rs.close();			
			return false;
		}
		else {
			stmt.close();
			rs.close();
			return true;
		}
	}
	
	// Get invoices size
	public int getTableCount(String tableName) throws SQLException {
		stmt = con.createStatement();  
		rs = stmt.executeQuery("SELECT count(*) FROM "+tableName+"");
		if(rs.next() == false) {
			stmt.close();
			rs.close();			
			return 0;
		}
		else {
			int size = Integer.parseInt(rs.getObject(1).toString()) + 1;
			stmt.close();
			rs.close();
			return size;
		}
	}
	
	// Get Order count from Orders table by customer
	public int getOrderTableCountByCustomer() throws SQLException {
		stmt = con.createStatement();  
		rs = stmt.executeQuery("select count(*) from Orders group by ID");
		int counter = 1;
		while(rs.next())
			counter++;
		return counter;
	}
	
	// Get last product ID value from DB for add new one
	public int getProductLastIndex() throws SQLException {
		stmt = con.createStatement();  
		rs = stmt.executeQuery("SELECT TOP 1 ID FROM Products ORDER BY ID DESC");
		if(rs.next())
			return Integer.parseInt(rs.getObject(1).toString());
		return 0;
	}
	
	// Get all invoices Print
	public String getInvoicesPrint() {
		// Results String
		String result = "";
		
		// SQL Quary
		String quary = "SELECT cu.FullName, PrintMessage FROM Invoices i join Customers cu on cu.ID = i.CustomerID";
				
		try {  			
			// Create and execute an SQL statement that returns some data.  
			stmt = con.createStatement();  
			rs = stmt.executeQuery(quary);  
			
			// Run a while loop over all of the table
			while (rs.next()) {
			    result += "Customer: " + rs.getObject(1).toString() +"\n"
			    		+rs.getObject(2).toString() + "\n";
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {  
			if (rs != null) try { rs.close(); } catch(Exception e) {}  
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}			
		}
		return result;
	}
	
	// Get all invoices Print
	public String getInvoicesPrintByCustomer(int id) {
		
		// Create String variable for result
		String result = "";
		// SQL Quary
		String quary = "select PrintMessage from Invoices where CustomerID = '"+id+"'";
				
		try {  			
			// Create and execute an SQL statement that returns some data.  
			setStatementAndResultSet(quary); 
			
			// Run a while loop over all of the table
			while (rs.next()) {
			    result += rs.getObject(1).toString() + "\n";
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {  
			if (rs != null) try { rs.close(); } catch(Exception e) {}  
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}			
		}
		return result;
	}
	
}
