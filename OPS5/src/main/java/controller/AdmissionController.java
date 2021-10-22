package controller;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AdmissionController {
	
	private Parent root;

	@FXML
    private ComboBox<String> selectPatient;   
    
	/**
	 * For KIS System
	 */
    @FXML
    private CaseController caseController;
    
    /**
	 * For OPS System
	 */
    @FXML
    private OPController opController;
    

	@FXML
	public void initialize() {
    	System.out.println("Initialize Admission-Tab!");        
    }	
	
    @FXML
	public void create() {
    	System.out.println("Creating Case/OP!");
    }
    
    @FXML
	public void createAndShowNewPatientWindow() {
    	System.out.println("New Patient Window!");
    	try {
    		FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PanePatient.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root));
            stage.show();
    	}catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    
}
