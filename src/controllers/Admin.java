package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import system.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Optional;

import database.ShopDB;
import enums.*;

/*
 * Admin Controller
 * FXML: ui/Admin.fxml
 * */
public class Admin {
	
	// Shop System instance
	private static ShopSystem instance = ShopSystem.getInstance();
	private static ShopDB shopDB = ShopDB.getInstance();
	
	// Attributes
	private ArrayList<Pane> _panes;
	private static HashMap<Product,Node> _productNodes;
	private static HashMap<Customer,Node> _customerNodes;

	
	// JavaFX Components
    @FXML
    private ImageView imgUser;

    @FXML
    private Label userName;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Pane pnlProducts;

    @FXML
    private TextField tfProductName;

    @FXML
    private TextField tfProductPrice;

    @FXML
    private ComboBox<ProductType> cbProductType;

    @FXML
    private TextField tfImagePath;

    @FXML
    private Button btnAddProduct;

    @FXML
    private VBox vboxProducts;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlCustomers;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnMinimize;
    
    @FXML
    private TextField tfID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfBirthDate;

    @FXML
    private Button btnAddCustomer;

    @FXML
    private VBox vboxCustomers;

    @FXML
    private TextArea taOrders;
    
    
    // FXML Initialize method
    @FXML
    public void initialize() {
    	
    	// Init Panes ArrayList
    	_panes = new ArrayList<Pane>();
    	_panes.add(pnlOrders);
    	_panes.add(pnlProducts);
    	_panes.add(pnlCustomers);
    	
    	// Init combobox
    	EnumSet.allOf(ProductType.class).forEach((type) -> {
    		cbProductType.getItems().add(type);
    	});
    	
    	// Set the first selection and also the folder path
    	cbProductType.getSelectionModel().selectFirst();
    	tfImagePath.setText("ui/img/soccer/");
    	
    	// Set orders 
    	taOrders.setText(shopDB.getInvoicesPrint());
    	
    	// Init Products and Customer VBOX's and Nodes
    	initProducts();
    	initCustomers();
    }

    // Handle Clicls method to control buttons and panes change
    // Incluse 'Close' and 'Minimize' buttons
    @FXML
    void handleClicks(ActionEvent event) {
    	if(event.getSource() == btnProducts) 
    		paneChange(pnlProducts);   		   		
    	
    	if(event.getSource() == btnOrders) 
    		paneChange(pnlOrders);
    	
    	if(event.getSource() == btnCustomers) 
    		paneChange(pnlCustomers);   	
    	
    	if(event.getSource() == btnMinimize) 
    		((Stage)(btnMinimize.getScene().getWindow())).setIconified(true);
    	
    	if(event.getSource() == btnClose) {
    		Stage stage = (Stage)btnClose.getScene().getWindow();
    		stage.close();
    	}
    	
    	// Add Image Path by type to help admin entring the url
    	if(event.getSource() == cbProductType) {
    		if(cbProductType.getValue().equals(ProductType.Soccer))
    			tfImagePath.setText("ui/img/soccer/");
	    	if(cbProductType.getValue().equals(ProductType.NBA))
				tfImagePath.setText("ui/img/NBA/"); 
	    	if(cbProductType.getValue().equals(ProductType.PoloShirts))
				tfImagePath.setText("ui/img/POLO/"); 
	    	if(cbProductType.getValue().equals(ProductType.SportShoes))
				tfImagePath.setText("ui/img/Shoes/"); 
	    	if(cbProductType.getValue().equals(ProductType.Kids))
				tfImagePath.setText("ui/img/KIDS/"); 
    	}
    }

    // Change pane methods
    public void paneChange(Pane pane) {
    	for(Pane x: _panes) {
    		if(pane == x)
    			x.setVisible(true);
    		else
    			x.setVisible(false);
    	}
    }
    
    // On click add new Product
    @FXML
    void onClickAddNewProduct(ActionEvent event){
 	
    	// Check if fields and image path are current
    	if(checkFields(1) && imageCheck()) {
    		   		
    		// Initialize 'Product' values
    		String name = tfProductName.getText().toString();
    		double price = Double.parseDouble(tfProductPrice.getText().toString());
    		ProductType type = cbProductType.getValue();
    		String imagePath = tfImagePath.getText().toString();
    		
    		// Create new product and add to product list
    		Product product = new Product(0 ,name, price, type, imagePath);
    		
    		try {
        		// Create quary to insert new product and execute
        		String quary = "INSERT INTO Products VALUES ('"+name+"', "
        													   + ""+price+","
        													   + " '"+product.getJoinDate().toString()+"',"
        													   + " '"+ type.toString() +"',"
        													   + " '"+imagePath+"');";
        		shopDB.executeQuary(quary);
        		product.setID(shopDB.getProductLastIndex());    		
        		instance.addProduct(product);
        		
        		// Init Prudcuts view (Nodes)
        		initProducts();
       		
        		// Clear Fields
        		clearFields(1);
    		}
    		catch(SQLException e) {
    			e.printStackTrace();
    		}
    	}   	
    }
    
    // OnClick add new customer
    @FXML
    void onClickAddNewCustomer(ActionEvent event) throws SQLException{

    	// Check if field current
    	if(checkFields(2)) {
    		
    		// Initialize 'Customer' values
    		int id = Integer.parseInt(tfID.getText().toString());
    		String name = tfName.getText().toString();
    		String email = tfEmail.getText().toString();
    		String dateOfBirth = tfBirthDate.getText().toString();
    		
    		// Create new Customer and add to customer list
    		Customer customer = new Customer(id, name, email, dateOfBirth);
    		
    		PreparedStatement query = shopDB.getConnection().prepareStatement("INSERT INTO Customers VALUES(?,?,?,?)");
  		
    		query.setInt(1, id);
    		query.setString(2, name);
    		query.setString(3, email);
    		query.setString(4, dateOfBirth);

			// Add customer to local data structure and database
			instance.addCustomer(customer);

			// Execure query
			query.executeUpdate();
			
			// Initialize customer view (Nodes)
			initCustomers();
			
			// Clear Fields
			clearFields(2);
    		
    		
    	}
    }
    
       
    // Initialize Products from database to view point
    // Product database -> Node -> VBox
	public void initProducts() {

		// Create new HashMap with Product as Key and Node as Value
		_productNodes = new HashMap<Product,Node>();
		
		// Clear the VBox for case that isn't clear
		vboxProducts.getChildren().clear();
		
		// Run over all products in Shop database  	
        instance.getProducts().forEach((productID, product) -> {			
    		try {
	            // Create new Product Controller object and set the current product in
				ProductNode productNode = new ProductNode(product);                    	
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/manageProduct.fxml"));
	            
	            // Load the controller with the FXMLLoader
	            loader.setController(productNode);
	            
	            // Add the new node to the list
	            _productNodes.put(product, loader.load());
	
	            // Some fx graphics on the nodes
	            _productNodes.get(product).setOnMouseEntered(event -> {
	            	_productNodes.get(product).setStyle("-fx-background-color : #f2f4f6");
	            });
	            _productNodes.get(product).setOnMouseExited(event -> {
	            	_productNodes.get(product).setStyle("-fx-background-color : #ffffff");
	            });
	            
	            // Add the Node to the VBOX pannel to show the data
	            vboxProducts.getChildren().add(_productNodes.get(product));                	
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}		
        });       
	}
		
    // Initialize Customer from database to view point
    // Customer database -> Node -> VBox
	public void initCustomers() {
		
		// Create new HashMap with Customer as Key and Node as Value
		_customerNodes = new HashMap<Customer,Node>();
		
		// Clear the VBox for case that isn't clear
		vboxCustomers.getChildren().clear();
		 
		// Run over all customer in Shop database  
        instance.getCustomers().forEach((customerID, customer) -> {			
    		try {    			
	            // Create new Customer Controller object and set the current product in
	    		CustomerNode customerNode = new CustomerNode(customer);                    	
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/manageCustomers.fxml"));
	            
	            // Load the controller with the FXMLLoader
	            loader.setController(customerNode);
	            
	            // Add the new node to the list
	            _customerNodes.put(customer, loader.load());
	
	            // Some fx graphics on the nodes
	            _customerNodes.get(customer).setOnMouseEntered(event -> {
	            	_customerNodes.get(customer).setStyle("-fx-background-color : #f2f4f6");
	            });
	            _customerNodes.get(customer).setOnMouseExited(event -> {
	            	_customerNodes.get(customer).setStyle("-fx-background-color : #ffffff");
	            });
	            
	            // Add the Node to the VBOX pannel to show the data
	            vboxCustomers.getChildren().add(_customerNodes.get(customer));                	
	    		} 
	    		catch (IOException e) {
	    			e.printStackTrace();
	    		}		
        });       
	}
	
	// Clear Fields by key 
	// key 1 = Prodcut
	// key 2 = customer
	private void clearFields(int key) {
		
		// If key == 1 == Product
		if(key == 1) {
			tfProductName.clear();
			tfProductPrice.clear();
			tfImagePath.clear();
			cbProductType.getSelectionModel().selectFirst();
		}
		
		// If key == 2 == Customer
		if(key == 2) {
			tfID.clear();
			tfName.clear();
			tfEmail.clear();
			tfBirthDate.clear();
		}
	}
	
	
	// Check fields values (not empty and if numbers are current) by key
	// key 1 = Prodcut
	// key 2 = Customer
	private boolean checkFields(int key) {
		
		// If key == 1 == Product
		if(key == 1) {
			if(!tfProductName.getText().toString().isEmpty() 
					&& !tfImagePath.getText().toString().isEmpty() 
					&& instance.checkInputDouble(tfProductPrice.getText().toString()))
				return true;
		}
		
		// If key == 2 == Customer
		if(key == 2) {
			if(instance.checkInputInteger(tfID.getText().toString())
					&& !tfName.getText().toString().isEmpty()
					&& !tfEmail.getText().toString().isEmpty()
					&& !tfBirthDate.getText().toString().isEmpty())
				return true;
		}
		return false;
	}
 
	// Check if image path is current by trying ro create
	// image object by the url 
	public boolean imageCheck() {
    	try {
    		@SuppressWarnings("unused")
			Image img = new Image(tfImagePath.getText().toString());
    		return true;
    	}
    	catch(Exception e) {
    		return false;
    	}
    }
	
		
	/*
	 * ProductNode Class
	 * Controller class for Product
	 * Use to create a Product View for frontend use
	 * As a Node view in VBox
	 * Store in HashMap of Node
	 * */
	public class ProductNode {
			
	    @FXML
	    private HBox itemC;

	    @FXML
	    private ImageView productImage;

	    @FXML
	    private Label lblProductName;

	    @FXML
	    private Label lblProductPrice;

	    @FXML
	    private Label lblProductDate;
	    
	    @FXML
	    private Label lblProductType;

	    @FXML
	    private Button btnRemove;
	    
	    @FXML
	    private Button btnEdit;

	    // Attributes
	    private Product _product;
	    
	    
	    // Constructor
	    public ProductNode(Product product) {
	    	_product = product;
	    }
	    
	    // Initialize - set all of the data.
	    @FXML
	    public void initialize() {
	    	
	    	lblProductName.setText(_product.getProductName());
	    	lblProductType.setText(_product.getProductType().toString());
	    	lblProductPrice.setText(_product.getProductPrice() + "");
	    	lblProductDate.setText(_product.getJoinDate().toString());
	    	productImage.setImage(new Image(_product.getImageURL()));	

	    }
	    
	    // On Action remove product
	    @FXML
	    void onActionRemove(ActionEvent event) throws SQLException {
	    	
	    	if(instance.deleteDialog(_product.getProductName())) {
	    		// First - remove from the VBox
		    	vboxProducts.getChildren().remove(_productNodes.get(_product));
		    	
		    	// Second - remove from the Node's hashmap
		    	_productNodes.remove(_product);
		    	
		    	// At the end - remove from the database
		    	instance.removeProduct(_product);
		    	
		    	// Remove Product from Database
		    	shopDB.executeQuary("DELETE FROM Products WHERE ID = "+ _product.getProductID() +"");
	    	}    	
	    }		
	 // On Action remove product
	    @FXML
	    void onActionEdit(ActionEvent event) throws SQLException {
	    	
	    	Dialog<Product> dialog = new Dialog<>();
	        dialog.setTitle("Update Prodcut");
	        dialog.setHeaderText("Please edit the values that you want to change");
	        
	        DialogPane dialogPane = dialog.getDialogPane();
	        
	        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	        
	        TextField pName = new TextField(_product.getProductName());
	        TextField pPrice = new TextField(_product.getProductPrice() + "");
	        ComboBox<ProductType> comboBox = new ComboBox<ProductType>();
	        EnumSet.allOf(ProductType.class).forEach((type) -> {
	        	comboBox.getItems().add(type);
	    	});
	        TextField pImgaeURL = new TextField(_product.getImageURL());
	        comboBox.getSelectionModel().select(_product.getProductType());
	       	        	        
	        dialogPane.setContent(new VBox(8, pName, pPrice,comboBox,pImgaeURL));

	       
	        dialog.setResultConverter((ButtonType button) -> {
	            if (button == ButtonType.OK) {
	                
	            	// Update Prodcut object
	            	_product.setProductName(pName.getText().toString());
	            	_product.setProductPrice(Double.parseDouble(pPrice.getText().toString()));
	            	_product.setProductType(comboBox.getSelectionModel().getSelectedItem());
	            	_product.setImageURL(pImgaeURL.getText().toString());
	            	
	            	// Excute the query
	            	try {
						shopDB.executeQuary("Update Products Set "
								+ "ProductName = '"+pName.getText().toString()+"', "
								+ "Price = "+Double.parseDouble(pPrice.getText().toString())+", "
								+ "ProductType = '"+comboBox.getSelectionModel().getSelectedItem()+"', "
								+ "ImageURL = '"+pImgaeURL.getText().toString()+"'\r\n"
								+ "  where ID = "+_product.getProductID()+"");
					} 
	            	catch (NumberFormatException e) {

						e.printStackTrace();
					} 
	            	catch (SQLException e) {
						e.printStackTrace();
					}
	            }
	            return null;
	        });
	        
	        Optional<Product> optionalResult = dialog.showAndWait();
	        initProducts();
	    }	    
	}

	
	/*
	 * CustomerNode Class
	 * Controller class for Customer
	 * Use to create a Customer View for frontend use
	 * As a Node view in VBox
	 * Store in HashMap of Node
	 * */
	public class CustomerNode{
		
		 		 
	    @FXML
	    private HBox itemC;

	    @FXML
	    private Label lblCustomerID;

	    @FXML
	    private Label lblCustomerName;

	    @FXML
	    private Label lblCustomerEmail;

	    @FXML
	    private Label lblCustomerDate;

	    @FXML
	    private Button btnRemove;
	     
	    // Attributes
	    private Customer _customer;
	    
	    public CustomerNode() {}

	    public CustomerNode(Customer customer) {
	    	_customer = customer;
	    }
	    
	    // Initialize - set all of the data.
	    @FXML
	    public void initialize() {
	    	lblCustomerID.setText(_customer.getUserID()+"");
	    	lblCustomerName.setText(_customer.getUserName());
	    	lblCustomerEmail.setText(_customer.getEmail());
	    	lblCustomerDate.setText(_customer.getDateOfBirth());
	    }
	    
	    // OnAction remove customer
	    @FXML
	    void onActionRemove(ActionEvent event) throws SQLException {
	    	
	    	if(instance.deleteDialog(_customer.getUserName())) {
	    		// First - remove from the VBox
		    	vboxCustomers.getChildren().remove(_customerNodes.get(_customer));
		    	
		    	// Second - remove from the Nodes hashmap
		    	_customerNodes.remove(_customer);
		    	
		    	// At the end - remove from database
		    	instance.removeCustomer(_customer);
		    	
		    	// Remove from Database
		    	shopDB.executeQuary("DELETE FROM Customers WHERE ID = "+ _customer.getUserID() +"");
	    	}	    	
	    }	    		         
	}
}
