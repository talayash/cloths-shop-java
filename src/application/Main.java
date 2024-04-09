package application;
	
import java.io.IOException;

import controllers.Admin;
import database.ShopDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import system.Customer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	// Database instance
	static private ShopDB instanceDB = ShopDB.getInstance();
	
	// Attributes
	private double x, y;

	// Start method for running the Login stage
	@Override
    public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
        
    	Parent root = FXMLLoader.load(getClass().getResource("/ui/Login.fxml"));
        primaryStage.setScene(new Scene(root));
       
        // Set stage icon
        primaryStage.getIcons().add(new Image("/ui/img/logo.png"));
        // Set stage borderless and make it dragable
        primaryStage.initStyle(StageStyle.UNDECORATED);
        
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        
        primaryStage.show();
        
    }
	
	// Main methods 
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		// Load Shop "Database" before launching the system
		instanceDB.loadDatabase();
		
		// Launch the system
		launch(args);
	}
	
	/*
	@Override
	// When all scene closed - save the database
	public void stop(){
		
		// Reset current user cart data
		if(instanceShop.getCurrentCustomer() != null)
			instanceShop.getCurrentCart().clear();		
		// Save date
		localSerDB.getInstance().saveData();
	}
	*/	
}
