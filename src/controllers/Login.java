package controllers;
import java.io.IOException;
import java.sql.SQLException;

import database.ShopDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.*;

/*
 * Login Controller
 * FXML: ui/Login.fxml
 * Login pannel for connecting to a the system
 * Admin login with 'Admin' input as Username and Password
 * Customer login with 'Name' as Username and ID as Password
 * */

public class Login {

	// Attributes
	private static ShopSystem instance = ShopSystem.getInstance();
	private boolean userExists;
	private double x;
	private double y;
	
	// JavaFX Components
    @FXML
    private TextField userNameTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button loginButton;

    @FXML
    private Text wrongText;
    
    @FXML
    public void initialize() {
    	// Exists for initialize data or do some testing
    	// If evrything is work fine - should be empty
    }
    
    // OnClick login button
    @FXML
    void onClickLogin(ActionEvent event) throws IOException, SQLException {
    	   	
    	// Check if 'Admin' try to login
    	if(userNameTF.getText().toString().equals("Admin") && passwordTF.getText().toString().equals("Admin")) {
    		openNextStage("/ui/Admin.fxml");   		  		
		}
    
    	// Else - Check fields for Customer login
    	else {
    		if(checkFields()) {
        		
        		// Init variable from input and check if exists in the database
        		String username = userNameTF.getText().toString();
        		int password = Integer.parseInt(passwordTF.getText().toString());
        		
        		// Check if the inputs are exists
        		if(ShopDB.getInstance().isCustomerExists(username, password)) {
        			
        			// Create new 'Customer' and 'Cart' to define the current user 
        			Customer customer = instance.getCustomers().get(password);
        			Cart cart = instance.getCarts().get(customer);
        			
        			// Documentation of user log
        			ShopDB.getInstance().executeQuary("INSERT INTO Logs VALUES ('" + customer.getUserID() + "', CURRENT_TIMESTAMP);");
        			
        			// Set the currents for the current user that logging
        			instance.setCurrentCustomer(customer);
        			instance.setCurrentCart(cart);

        			// Open the shop
        			openNextStage("/ui/Shop.fxml");
        		}
        		else {
        			wrongText.setText("Username or Password wrong!");
        			wrongText.setVisible(true);
        			}
        	}
    		else {
    			wrongText.setText("Wrong input!");
    			wrongText.setVisible(true);
    		}
    	}
    }
    
    // Check if user is exists in the database
    // In our case user 'Name' is an Username and 'id' is a password
    public boolean checkIfExists(String name, int id) {
    	userExists = false;
    	
    	// Run a lambda over all Customers and check if exists
    	instance.getCustomers().forEach((customerID, customer) -> {
    		// If the user exists return true
    		if(customer.getUserName().equals(name) && customer.getUserID() == id)
    			userExists = true;
    	});
    	return userExists;
    }
    
    // Check input TextField and PasswordField
    public boolean checkFields() {
    	// if the fields not empty return true
    	if(!userNameTF.getText().toString().isEmpty() && !passwordTF.getText().toString().isEmpty()) {
    		// Check if the ID(password) is only numbers
    		return instance.checkInputInteger(passwordTF.getText().toString());
    	}
    	return false;
    }
        
    // Open next window (Admin/Customer)
    public void openNextStage(String fxmlUrl) throws IOException {
    	
    	// Create new Stage and root
    	Stage stage = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource(fxmlUrl));
    	stage.setScene(new Scene(root));
    	
    	// Set logo
    	stage.getIcons().add(new Image("/ui/img/logo.png"));
       
    	// Initialize window style without border and upline panel
    	stage.initStyle(StageStyle.UNDECORATED);
        
    	// Make window values available
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

        	stage.setX(event.getScreenX() - x);
        	stage.setY(event.getScreenY() - y);

        });
        
        // Run the new stage
        stage.show();

        // Close the old one (The 'Login' stage)
        Stage old = (Stage)loginButton.getScene().getWindow();
        old.close();
    }   
}
