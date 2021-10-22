package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DiagnosisController {
	
	@FXML
	private ComboBox<Integer> diagnosisOpId;
	@FXML
	private ComboBox<String> diagnosisIcdCode;
	@FXML
	private ComboBox<String> diagnosisType;
	@FXML
	private TextField diagnosisFreetext;	
	@FXML
	private TableView<?> diagnosisTable;	

	@FXML
	public void initialize() {
    	System.out.println("Initialize Diagnosis-Tab!");
	}
	
	@FXML
	public void createDiagnosis(ActionEvent event){
		System.out.println("Create diagnosis!");
	}

}
