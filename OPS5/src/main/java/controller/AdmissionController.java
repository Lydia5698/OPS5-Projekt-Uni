package controller;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
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
    private ComboBox<Patient> selectPatient;

    @FXML
    private OPController opController;

	private void setPatient() {
		Callback<ListView<Patient>, ListCell<Patient>> cellFactory = new Callback<>() {
			@Override
			public ListCell<Patient> call(ListView<Patient> patientListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Patient pat, boolean empty) {
						super.updateItem(pat, empty);
						if (pat == null || empty) {
							setGraphic(null);
						} else {
							setText(pat.getName() + ", " + pat.getVorname() + ";  " + pat.getPatId());
						}
					}
				};
			}
		};
		selectPatient.setButtonCell(cellFactory.call(null));
		selectPatient.setCellFactory(cellFactory);
		selectPatient.getItems().setAll(new PatientDao(Main.configuration).findAll());
	}

	@FXML
	public void initialize() {
		selectPatient.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				opController.setCases(selectPatient.getValue().getPatId());
			}
		});
		setPatient();

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
