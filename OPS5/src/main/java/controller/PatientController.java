package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class PatientController{

    @FXML
    private TextField patientFirstname;
    @FXML
    private TextField patientLastname;
    @FXML
    private DatePicker patientBirthdate;
    @FXML
    private TextField patientBirthplace;
    @FXML
    private TextField patientStreet;
    @FXML
    private TextField patientPostcode;
    @FXML
    private TextField patientCellphone;
    @FXML
    private ToggleGroup sexGroup;
    @FXML
    private RadioButton patientFemale;
    @FXML
    private RadioButton patientMale;
    @FXML
    private RadioButton patientDiv;
       
	@FXML
	public void initialize() {
    	System.out.println("Initialize Patient-Tab!");
	}
	
    @FXML
    void createPatient(ActionEvent event) {
    	System.out.println("Creating patient!");
    }
    
}

