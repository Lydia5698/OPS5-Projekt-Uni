package controller;

import ExternalFiles.Converter;
import ExternalFiles.DateTimePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.DiagnosetypStDao;
import jooq.tables.daos.Icd10CodeStDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Diagnose;
import jooq.tables.pojos.DiagnosetypSt;
import jooq.tables.pojos.Icd10CodeSt;
import jooq.tables.pojos.Rolle;
import main.Main;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This Controller displays the Diagnosis. You can create a new one or edit an existent
 */
public class RoleOverviewController {

	@FXML
	private TableView<Rolle> roleTable;

	/**
	 * This Methode initialize the TableView for the existing Diagnosis and shows the Op-IDs, ICD-10 codes and
	 * the Diagnosis Type in the Comboboxes
	 */
	@FXML
	public void initialize() {

		System.out.println("Initialize Diagnosis-Tab!");

	}

	/**
	 * Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
	 * a Diagnosis. If the User isn't missing any necessary Values the Diagnose is edited and the Window closes
	 * @param event the event of pushing the Speichern Button
	 */
	@FXML
	public void createRole(ActionEvent event){
		System.out.println("Create role!");

	}


	/**
	 * Launches when the Button Neue Diagnose is pressed. It sets the flag false so that we know that the user wants to create
	 * a new Diagnosis. If the User isn't missing any necessary Values the Diagnose is saved and the Window closes
	 * @param event the event of pushing the Neue Diagnose Button
	 */
	@FXML
	void createNewRole(ActionEvent event) {
		System.out.println("Create new role!");
	}

	/**
	 * Collects all Diagnosis from the Database and saves them in a observable Array List from Type Diagnose pojo
	 * @return all Diagnosis
	 */
	/*
	public static ObservableList<Diagnose> diagnoseView(){
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
		List<Diagnose> diagnose = diagnoseDao.findAll();
		return FXCollections.observableArrayList(diagnose);
	}
	*/

	/**
	 * Inserts or edits a Diagnose in the Database.
	 */
	/*
	private void insertNewRole() {
		Integer diagID = null;
		boolean storniert = false;
		Integer opID = diagnosisOpId.getValue().getOpId();
		String icdCode = diagnosisIcdCode.getValue().getIcd10Code();
		Integer diagTyp = diagnosisType.getValue().getDiagnosetyp();
		String freitext = diagnosisFreetext.getText();
		LocalDateTime datum;
		String ersteller = null;
		LocalDateTime erstellZeit = null;
		String bearbeiter = null;
		LocalDateTime bearbeiterZeit = null;

		// Edits Diagnosis
		if(flagEditDiagnose){
			diagID = onEditDiagnose();
			bearbeiter = MainController.getUserId();
			bearbeiterZeit = LocalDateTime.now();
			datum = dateDiagnosis.getDateTimeValue();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit,bearbeiterZeit,storniert,opID,diagTyp,icdCode,ersteller,bearbeiter);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.update(diagnose);
		}
		// Creates new Diagnosis
		else{
			ersteller = MainController.getUserId();
			erstellZeit = LocalDateTime.now();
			datum = LocalDateTime.now();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit,bearbeiterZeit,storniert,opID,diagTyp,icdCode,ersteller,bearbeiter);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.insert(diagnose);
		}
	}
	*/


	/**
	 * Gets the Diagnosis ID from the selected Diagnosis in the Table View
	 * @return Diagnosis ID
	 */
	/*
	public int onEditRole() {
		int id = 0;
		// check the table's selected item and get selected item
		if (diagnosisTable.getSelectionModel().getSelectedItem() != null) {
			Diagnose selectedItem = diagnosisTable.getSelectionModel().getSelectedItem();
			id = selectedItem.getDiagnoseId();
		}
		return id;
	}
	*/


	/**
	 * Gets triggered when a Diagnosis in the TableView gets selected
	 */
	@FXML
	void roleClicked() {
		roleTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() > 0) {
				System.out.println("Role clicked!");
			}
		});
	}
}
