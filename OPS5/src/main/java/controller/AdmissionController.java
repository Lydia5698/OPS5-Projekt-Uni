package controller;

import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.sql.Timestamp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import jooq.Tables;
import jooq.tables.pojos.Operation;
import jooq.tables.daos.OperationDao;
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
				opController.setCase(selectPatient.getValue().getPatId());
			}
		});
		setPatient();

    	System.out.println("Initialize Admission-Tab!");
    }	
	
    @FXML
	public void create() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Fehlende Einträge!");
		if (opController.getOpCaseId() == null){
			alert.setContentText("Es muss ein Fall ausgewählt werden!");
			alert.showAndWait();
		}
		else if(opController.getOpDateBegin().getDateTimeValue() == null){
			alert.setContentText("Es muss ein gültiger Beginn eingetragen werden!");
			alert.showAndWait();
		}
		else if(opController.getOpDateEnd().getDateTimeValue() == null){
			alert.setContentText("Es muss ein gültiges Ende eingetragen werden!");
			alert.showAndWait();
		}
		else {
			Operation operation = new Operation(
					null, //opId -> automatisch mit AutoIncrement gesetzt
					opController.getOpDateBegin().getDateTimeValue(), //beginn
					opController.getOpDateEnd().getDateTimeValue(), //ende
					opController.getTowelBefore(), //bauchtuecherPrae -> hat immer einen Wert
					opController.getTowelAfter(), //bauchtuecherPost -> hat immer einen Wert
					opController.getCutTime().getDateTimeValue(), //schnittzeit
					opController.getSewTime().getDateTimeValue(), //nahtzeit
					new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //erstellZeit
					null, //bearbeiterZeit
					false, //storniert
					opController.getOpCaseId(), //fallId
					opController.getOpRoomCode(), //opSaal
					opController.getNarkose(), //narkoseSt
					opController.getOpType(), //opTypSt
					MainController.getUserId(), //ersteller
					null //bearbeiter
			);
			OperationDao operationDao = new OperationDao(Main.configuration);
			//operationDao.insert(operation);
			Alert confirm = new Alert(AlertType.INFORMATION);
			confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
			confirm.showAndWait();
		}
		System.out.println("Creating OP!");
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
