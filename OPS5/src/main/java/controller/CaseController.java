package controller;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class CaseController{
	
    @FXML
    private ComboBox<String> caseStation;
    @FXML
    private ComboBox<Integer> caseDoctor;   
    @FXML
    private CheckBox caseAmb;   
    @FXML
    private CheckBox caseStat;   
    @FXML
    private DatePicker caseDate;      
   
    @FXML
	public void initialize() {
    	System.out.println("Initialize Case-Tab!");
	}

}
