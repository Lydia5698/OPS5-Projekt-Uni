package controller;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.FallDao;
import jooq.tables.pojos.Diagnose;
import jooq.tables.pojos.Fall;
import main.Main;

import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.sql.Timestamp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;


/**
 * The class AdmissionController is responsible for creating a new operation.
 * It also contains the buttons for opening windows to creating new roles and patients.
 */
public class AdmissionController {
	
	private Parent root;

	@FXML
    private ComboBox<Patient> selectPatient;

    @FXML
    private OPController opController;

	@FXML
	private Button saveBtn;

	private boolean flagOpEdit = false;
	private Integer opId = null;

//TODO: Nur Patienten, die nicht storniert sind!
	/**
	 * This method selects all patients of the system as choosing options of the combobox for the selection of the patient.
	 */
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

	/**
	 * When the window for creating an operation is opened, this method is called.
	 * The patients can be selected and the initial case id gets selected by the chosen patient, which can be null.
	 */
	@FXML
	public void initialize() {
		selectPatient.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if(selectPatient.getValue() != null){opController.setCase(selectPatient.getValue().getPatId());}
			}
		});
		setPatient();

    	System.out.println("Initialize Admission-Tab!");
    }

	/**
	 * This method is called when a button is pushed.
	 * Checks are run that all input is valid and all the non-nullable objects have a value.
	 * A new operation is created and inserted into the database.
	 */
	@FXML
	public void create() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		if (opController.getOpCaseId() == null){
			alert.setHeaderText("Fehlende Einträge!");
			alert.setContentText("Es muss ein Fall ausgewählt werden!");
			alert.showAndWait();
		}//Op-Ende vor Op-Start
		else if(opController.getOpDateEnd() != null && opController.getOpDateBegin() != null){
			if(opController.getOpDateEnd().isBefore(opController.getOpDateBegin())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Das Op-Ende kann nicht vor dem Op-Start sein!");
				alert.showAndWait();
			}
		}//Schnittzeit vor Op-Beginn
		else if(opController.getOpDateBegin() != null && opController.getCutTime() != null){
			if(opController.getOpDateBegin().isAfter(opController.getCutTime())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Die Schnittzeit kann nicht vor dem Op-Start sein!");
				alert.showAndWait();
			}
		}//Schnittzeit nach Op-Ende
		else if(opController.getOpDateEnd() != null && opController.getCutTime() != null){
			if(opController.getOpDateEnd().isBefore(opController.getCutTime())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Die Schnittzeit kann nicht nach dem Op-Ende sein!");
				alert.showAndWait();
			}
		}//Nahtzeit vor Op-Beginn
		else if(opController.getOpDateBegin() != null && opController.getSewTime() != null){
			if(opController.getOpDateBegin().isAfter(opController.getSewTime())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Die Nahtzeit kann nicht vor dem Op-Start sein!");
				alert.showAndWait();
			}
		}//Nahtzeit vor Schnittzeit
		else if(opController.getCutTime() != null && opController.getSewTime() != null){
			if(opController.getCutTime().isAfter(opController.getSewTime())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Die Nahtzeit kann nicht vor der Schnittzeit sein!");
				alert.showAndWait();
			}
		}//Nahtzeit nach Op-Ende
		else if(opController.getOpDateEnd() != null && opController.getSewTime() != null){
			if(opController.getOpDateEnd().isBefore(opController.getSewTime())){
				alert.setHeaderText("Falscher Eintrag!");
				alert.setContentText("Die Nahtzeit kann nicht nach dem Op-Ende sein!");
				alert.showAndWait();
			}
		} // TODO: 06.12.21 else if verhindert das speichern
		else { //TODO: Op-Beginn muss vor dem Ende sein!

			Operation operation = new Operation(
					null, //opId -> automatisch mit AutoIncrement gesetzt
					opController.getOpDateBegin(), //beginn
					opController.getOpDateEnd(), //ende
					opController.getTowelBefore(), //bauchtuecherPrae -> hat immer einen Wert
					opController.getTowelAfter(), //bauchtuecherPost -> hat immer einen Wert
					opController.getCutTime(), //schnittzeit
					opController.getSewTime(), //nahtzeit
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
			operationDao.insert(operation);

			Alert confirm = new Alert(AlertType.INFORMATION);
			confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
			confirm.showAndWait();
			clearFields();
		}
		System.out.println("Creating OP!");
	}

	/**
	 * Edits an existing Operation
	 */
	public void editOperation(){ // TODO: 06.12.21 zeit sachen überprüfen also schnitt nahtzeit etc.

		Operation operation = new Operation(
				opId, //opId -> the operation to be edited
				opController.getOpDateBegin(), //beginn
				opController.getOpDateEnd(), //ende
				opController.getTowelBefore(), //bauchtuecherPrae -> hat immer einen Wert
				opController.getTowelAfter(), //bauchtuecherPost -> hat immer einen Wert
				opController.getCutTime(), //schnittzeit
				opController.getSewTime(), //nahtzeit
				null, //erstellZeit
				LocalDateTime.now(), //bearbeiterZeit
				false, //storniert
				opController.getOpCaseId(), //fallId
				opController.getOpRoomCode(), //opSaal
				opController.getNarkose(), //narkoseSt
				opController.getOpType(), //opTypSt
				null, //ersteller
				MainController.getUserId() //bearbeiter
		);
		OperationDao operationDao = new OperationDao(Main.configuration);
		operationDao.update(operation);

		Alert confirm = new Alert(AlertType.INFORMATION);
		confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
		confirm.showAndWait();

	}

	/**
	 * When the button for creating a new role is pushed, a new window is opened.
	 */
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


	public void createAndShowNewFallWindow(ActionEvent actionEvent) {
		System.out.println("New Fall Window!");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/fxml/PaneFall.fxml"));
			root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Fall");
			stage.setScene(new Scene(root));
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * after succussfully insertion of operation set all fields to default
	 */
	private void clearFields(){
    	selectPatient.getSelectionModel().clearSelection();
    	opController.clearFields();
	}

	/**
	 *  Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
	 * 	the Operation. The Operation is edited and the Window closes
	 * 	@param event the event of pushing the Speichern Button
	 *
	 */
	@FXML
	public void saveEditOp(ActionEvent event) {
    	flagOpEdit = true;
    	opId = OverviewController.getOpId();
    	editOperation();
		Node source = (Node) event.getSource();
		Stage thisStage = (Stage) source.getScene().getWindow();
		thisStage.close();
	}


}
