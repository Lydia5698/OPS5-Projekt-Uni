package controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class OPController{	
    
    @FXML
    private ComboBox<Integer> opCaseId;
	@FXML
    private DatePicker opDate;
    @FXML
    private ComboBox<String> opType;
    @FXML
    private ComboBox<String> opRoom;   
    @FXML
    private ComboBox<Integer> opSurgeonId;
	
    @FXML
	public void initialize() {
    	System.out.println("Initialize OP-Tab!");
	}
    
    
}
