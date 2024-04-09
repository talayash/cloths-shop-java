package controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import database.ShopDB;
import enums.ProductType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import system.*;


public class Shop implements Initializable{
	
	// Instance
	private static ShopSystem instance = ShopSystem.getInstance();
	private static ShopDB shopDB = ShopDB.getInstance();
	
	// Current Customer and Cart
	private static Customer _currentCustomer = instance.getCurrentCustomer();
	private static Cart _currentCart = instance.getCurrentCart();

		
	// Shop attributes
	private ArrayList<Pane> _panes;
	private static HashMap<Product,Node> _nodes = new HashMap<Product, Node>();
	private static HashMap<Product,Node> _currentCartNodes = new HashMap<Product, Node>();
	
	
	// JavaFX Components
	@FXML
    private VBox vboxPnlSoccer;
	
	@FXML
    private VBox vboxPnlNBA;
    
	@FXML
    private VBox vboxPnlPoloShirts;
	
    @FXML
    private VBox vboxPnlSportShoes;
    
    @FXML
    private VBox vboxPnlKids;
    
	@FXML
    private ImageView imgUser;

    @FXML
    private Label userName;

    @FXML
    private Button btnSoccer;

    @FXML
    private Button btnNBA;

    @FXML
    private Button btnPoloShirts;

    @FXML
    private Button btnSportShoes;

    @FXML
    private Button btnKids;

    @FXML
    private Pane pnlSoccer;

    @FXML
    private Pane pnlNBA;

    @FXML
    private Pane pnlPoloShirts;

    @FXML
    private Pane pnlSportShoes;

    @FXML
    private Pane pnlKids;
    
    @FXML
    private Pane pnlCart;
    
    @FXML
    private Pane pnlOrders;

    @FXML
    private VBox vboxPnlCart;

    @FXML
    private TextArea cartDetails;

    @FXML
    private Button payButton;
    
    @FXML
    private Text numberOfProducts;
    
    @FXML
    private Button btnCart;
    
    @FXML
    private Button btnClose;

    @FXML
    private Button btnMinimize;
    
    @FXML
    private Button btnOrders;
    
    @FXML
    private TextArea ordersTextArea;

    // Initialize Customer side - Shop
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	// Update current user details
    	_currentCart  = instance.getCarts().get(_currentCustomer);
    	numberOfProducts.setText(_currentCart.getNumberOfProductsInCart() + "");
    	ordersTextArea.setText(shopDB.getInvoicesPrintByCustomer(_currentCustomer.getUserID()));
    	
    	// Load panes arraylist
    	_panes = new ArrayList<Pane>();
    	_panes.add(pnlSoccer);
    	_panes.add(pnlNBA);
    	_panes.add(pnlPoloShirts);
    	_panes.add(pnlSportShoes);
    	_panes.add(pnlKids);
    	_panes.add(pnlCart);
    	_panes.add(pnlOrders);
    	
    	// Set "Hello" message for current user
    	userName.setText("Hello " + _currentCustomer.getUserName());

    	// Initialize products from database to panes by categories
    	initProducts();
    }
    
    
    // Handle Button Click for change panes and minimize/close window
    @FXML
    void handleClicks(ActionEvent event) {
    	
    	if(event.getSource() == btnSoccer) 
    		paneChange(pnlSoccer);   		   		
    	
    	if(event.getSource() == btnNBA) 
    		paneChange(pnlNBA);
    	
    	if(event.getSource() == btnPoloShirts) 
    		paneChange(pnlPoloShirts);
    	
    	if(event.getSource() == btnSportShoes) 
    		paneChange(pnlSportShoes);    		        	
    	
    	if(event.getSource() == btnKids) 
    		paneChange(pnlKids);		
    	
    	if(event.getSource() == btnCart) 
    		paneChange(pnlCart);
    	
    	if(event.getSource() == btnOrders)
    		paneChange(pnlOrders);
    	
    	if(event.getSource() == btnClose) {
    		Stage stage = (Stage)btnClose.getScene().getWindow();
    		stage.close();
    	}
    	if(event.getSource() == btnMinimize) 
    		((Stage)(btnMinimize.getScene().getWindow())).setIconified(true);
    	
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

    
    // Initialize Products from database to view point
    // Product database -> Node -> VBox
    public void initProducts() {
    	
        instance.getProducts().forEach((productID, product) -> {			
    		try {

                // Create new Product Controller object and set the current product in
                ProductController pc = new ProductController(product, false);                    	
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Product.fxml"));
                
                // Load the controller with the FXMLLoader
                loader.setController(pc);
                
                // Add the new node to the list
                _nodes.put(product, loader.load());

                // Some fx graphics on the nodes
                _nodes.get(product).setOnMouseEntered(event -> {
                	_nodes.get(product).setStyle("-fx-background-color : #f2f4f6");
                });
                _nodes.get(product).setOnMouseExited(event -> {
                	_nodes.get(product).setStyle("-fx-background-color : #ffffff");
                });
                
                // Add the Node to the VBOX pannel to show the data
                // Sort by Product Category (type)
                if(product.getProductType().equals(ProductType.Soccer))                    
                	vboxPnlSoccer.getChildren().add(_nodes.get(product));
                
                else if(product.getProductType().equals(ProductType.NBA))
                	vboxPnlNBA.getChildren().add(_nodes.get(product));
                
                else if(product.getProductType().equals(ProductType.PoloShirts))
                	vboxPnlPoloShirts.getChildren().add(_nodes.get(product));
                
                else if(product.getProductType().equals(ProductType.SportShoes))
                	vboxPnlSportShoes.getChildren().add(_nodes.get(product));
                
                else if(product.getProductType().equals(ProductType.Kids))
                	vboxPnlKids.getChildren().add(_nodes.get(product));
            } 
			catch (IOException e) {
                e.printStackTrace();
            }
    		
    	});       
    }
    
    
    // Cart Pane
    @FXML
    void onClickPayButton(ActionEvent event) throws SQLException {

    	// First - Check that the cart not empty
    	if(_currentCart.getNumberOfProductsInCart() > 0) {
    		// Add the Cart to the Orders
        	instance.fromCartToOrders();
        	
        	// Reset Cart Pane (RESET: VBOX, Nodes, Cart details Text and amount of products)
        	vboxPnlCart.getChildren().clear();
        	_currentCartNodes.clear();
        	cartDetails.clear();
        	numberOfProducts.setText("0");
        	        	
        	// Update Customer 'Order' pane with details
        	ordersTextArea.setText(shopDB.getInvoicesPrintByCustomer(_currentCustomer.getUserID())); 
        	
        	// Delete from Cart Table all of the rows of this current user
        	shopDB.executeQuary("DELETE FROM Carts WHERE CustomerID = "+ _currentCustomer.getUserID()+"");
    	}  
    }
    
    
        
    ///////////////////////////////
    // Product Controller Class //
    ///////////////////////////////
    public class ProductController {
  	
        @FXML
        private HBox itemC;

        @FXML
        private Label lblProductName;

        @FXML
        private Label lblProductPrice;

        @FXML
        private Button btnAdd;
        @FXML
        private Button btnRemove;
        
        @FXML
        private ImageView productImage;

        // Attributes
        private Product _product;
        private boolean _inCart;
        
        @FXML
        void onActionAddToCart(ActionEvent event) throws SQLException {
        	// Add product to the cart
        	_currentCart.addProductToCart(_product);
        	shopDB.executeQuary("INSERT INTO Carts VALUES("+_currentCustomer.getUserID()+","+_product.getProductID()+");");
        	// Update cart details (Amount of product and cart details)
        	updateCartPane();
        	
        	// Set the VBOX with the Node items
        	setCartVbox();
        	        	
        }
        
        @FXML
        void onActionRemove(ActionEvent event) throws SQLException {
        	if(instance.deleteDialog(_product.getProductName())) {
        		// First remove the current product from the cart
            	_currentCart.removeProductFromCart(_product);
            	shopDB.executeQuary("DELETE FROM Carts WHERE CustomerID = "+ _currentCustomer.getUserID() 
            											+" AND ProductID = "+ _product.getProductID() +"");
            	// Update cart details (
            	updateCartPane();
            	
            	// Remove from VBOX and Nodes 
            	vboxPnlCart.getChildren().remove(_currentCartNodes.get(_product));
            	_currentCartNodes.remove(_product);
        	}
        }
        
        // Initialize
        @FXML
        public void initialize() {
        	lblProductName.setText(_product.getProductName());
        	lblProductPrice.setText(_product.getProductPrice()+"");
        	Image image = new Image(_product.getImageURL());
        	productImage.setImage(image);
        	setButton(_inCart);
        }
        
        // Constructor
        public ProductController(Product product, boolean inCart) {
        	
        	_product = product;
        	_inCart = inCart;
        }
        
        // Set button: 'Add' or 'Remove' visible
        public void setButton(boolean mode) {
        	if(mode) {
        		btnAdd.setVisible(false);
        		btnRemove.setVisible(true);
        		}
        	else {
        		btnRemove.setVisible(false);
        		btnAdd.setVisible(true);
        	}
        }
               
        // Update Cart pane details
        private void updateCartPane() {
        	numberOfProducts.setText(_currentCart.getNumberOfProductsInCart() +"");
        	cartDetails.setText(_currentCart.cartDetail());
        }
        
        // Set Cart pane VBOX with Nodes from the Cart list
        public void setCartVbox() {
        	// Run forEach over all of the product and check
        	instance.getProducts().forEach((productID, product) -> {			
        		if(product.equals(_product)) {
        			try {
            			
                        // Create new Product Controller object and set the current product in
                        ProductController pc = new ProductController(product, true);                    	
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Product.fxml"));
                        
                        // Load the controller with the FXMLLoader
                        loader.setController(pc);
                        
                        // Add the new node to the list
                        _currentCartNodes.put(product, loader.load());
                                               
                        // Add the Node to the VBOX pannel to show the data
                        vboxPnlCart.getChildren().add(_currentCartNodes.get(product));
                    } 
        			catch (IOException e) {
                        e.printStackTrace();
                    }
        		}    		
        	});
        }         
    }
}
