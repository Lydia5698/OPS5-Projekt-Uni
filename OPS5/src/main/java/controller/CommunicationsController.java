package controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CommunicationsController {
	
    @FXML
    private TextField communicationsIpAddress;
    @FXML
    private TextField communicationsPort;    
    @FXML
    private ComboBox<?> communicationsObject;
    @FXML
    private TableView<?> ts;
    
	@FXML
	public void initialize() {
    	System.out.println("Initialize Communications-Tab!");        
    }	
	
    @FXML
	public void send() {
    	System.out.println("Sending something!");
    }
    

}

