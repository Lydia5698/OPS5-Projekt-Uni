package controller;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jooq.Tables;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

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
		Result<Record1<String>> result = Main.dslContext.select(DSL.concat(Tables.PATIENT.NAME, DSL.inline(", "), Tables.PATIENT.VORNAME).as("fullname_patient"))
				.from(Tables.PATIENT)
				.orderBy(Tables.PATIENT.NAME.asc())
				.fetch();
		List<String> patientlist = result.map(record -> record.getValue("fullname_patient").toString());
		selectPatient.getItems().setAll(patientlist);
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
