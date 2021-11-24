package controller;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import jooq.Tables;
import jooq.tables.pojos.Patient;
import jooq.tables.daos.PatientDao;
import jooq.tables.records.PatientRecord;
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
		selectPatient.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				opController.updateCases(1);//patientDao.getId(selectPatient.GET().k));  //selectPatient.GET() muss pojos.Patient liefern
			}
		});
		// mein etwas kläglicher Versuch in die ComboBox einen Patienten zu schreiben, sodass man für die Fälle die PatId ermitteln kann...
		/*PatientDao patientDao = new PatientDao();
		List<Integer> result = Main.dslContext.selectFrom(Tables.PATIENT).fetch().getValues(Tables.PATIENT.PAT_ID);
		List<Patient> patientlist = patientDao.fetchByPatId(result);*/

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
	public void createRole(){
		System.out.println("Creating Role in new window!");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/fxml/PaneRole.fxml"));
			root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Patient");
			stage.setScene(new Scene(root));
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}
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
