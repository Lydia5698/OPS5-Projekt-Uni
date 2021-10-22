package controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class ProcedureController {
	
	@FXML
	private TableView<?> procedureTable;
	@FXML
	private ComboBox<?> procedureOp;
	@FXML
	private ComboBox<?> procedureOpsCode;
	
	
    @FXML
	public void initialize() {
    	System.out.println("Initialize Procedure-Tab!");
    }
	
	@FXML
	public void createProcedure() {
		System.out.println("Create procedure!");
	}
}
