package controller;

import ExternalFiles.Converter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;


/**
 * This Controller displays the Roles. You can create a new one or edit an existing.
 */
public class RoleOverviewController {

	@FXML
	private TableView<Rolle> roleTable;
	@FXML
	private TableColumn<Rolle, String> userCol;
	@FXML
	private TableColumn<Rolle, String> roleCol;
	@FXML
	private TableColumn<Rolle, Integer> opCol;
	@FXML
	private TableColumn<Rolle, String> erstellzeitCol;
	@FXML
	private TableColumn<Rolle, String> bearbeiterzeitCol;
	@FXML
	private TableColumn<Rolle, String> storniertCol;
	@FXML
	private TableColumn<Rolle, String> erstellerCol;
	@FXML
	private TableColumn<Rolle, String> bearbeiterCol;
	@FXML
	private SearchableComboBox<MedPersonal> mitarbeiter;
	@FXML
	private SearchableComboBox<RolleSt> role;
	@FXML
	private SearchableComboBox<Operation> op;
	@FXML
	private Button speichern;


	/**
	 * This Methode initialize the TableView for the existing Roles and shows the roles, Op-IDs and medical Users.
	 */
	@FXML
	public void initialize() {
		initializeColumns();
		setRole();
		setOp();
		setMitarbeiter();
	}


	/**
	 * Initializes all Columns from the TableView roleTable
	 */
	private void initializeColumns() {
		// create columns
		opCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
		roleCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.roleConverter(features.getValue().getRolleSt())));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
		erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
		userCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getMedPersonalPersId()));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
	}


	/**
	 * Launches when the Button Neue Rolle is pressed. It creates a new Rolle with the selected values.
	 */
	@FXML
	void createNewRole() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Fehlende Einträge!");
		if(mitarbeiter.getValue()==null){
			Main.logger.warning("Fehlende Einträge: Es muss ein Mitarbeiter ausgewählt werden.");
			alert.setContentText("Es muss ein Mitarbeiter ausgewählt werden!");
			alert.showAndWait();
		}
		else if(op.getValue()==null){
			Main.logger.warning("Fehlende Einträge: Es muss eine Op ausgewählt werden.");
			alert.setContentText("Es muss eine Op ausgewählt werden!");
			alert.showAndWait();
		}
		else if(role.getValue()==null){
			Main.logger.warning("Fehlende Einträge: Es muss eine Rolle ausgewählt werden.");
			alert.setContentText("Es muss eine Rolle ausgewählt werden!");
			alert.showAndWait();
		}
		else{
			Rolle insertRole = new Rolle(
					op.getSelectionModel().getSelectedItem().getOpId(), //opId
					null, //bearbeiter
					role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
					null, //bearbeiterZeit
					new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //ersteller Zeit
					MainController.getUserId(), //ersteller
					mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonalPersonal-ID
					false //storniert
			);
			RolleDao roleDao = new RolleDao(Main.configuration);
			// test if role with this operation and medical_user is already in the database
			List<Rolle> allRoles = roleDao.findAll();
			boolean roleExists = false;
			for (Rolle r : allRoles){
				if (r.getOpId() == insertRole.getOpId() && r.getMedPersonalPersId().equals(insertRole.getMedPersonalPersId())){
					roleExists = true;
				}
			}
			if(roleExists){
				Main.logger.warning("Es existiert bereits ein Rollen-Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter.");
				alert.setHeaderText("Eintrag mit diesem Schlüssel bereits vorhanden.");
				alert.setContentText("Es existiert bereits ein Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter. " +
						"Zum Bearbeiten der Rolle nutzen Sie bitte den Speichern-Button.");
				alert.showAndWait();
			}
			else {
				roleDao.insert(insertRole);
				Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
				Alert confirm = new Alert(AlertType.INFORMATION);
				confirm.setTitle("Information");
				confirm.setHeaderText("Erfolgreich eingefügt");
				confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
				confirm.showAndWait();
				//close the window
				Stage stage = (Stage) speichern.getScene().getWindow();
				stage.close();
			}
		}
	}


	/**
	 * Launches when the Button Speichern is pressed. It updates the selected role to the values that the user selects.
	 * If no role is selected, an alert is shown that you need to select a role before you can edit one.
	 */
	@FXML
	public void createRole(){
		if (roleTable.getSelectionModel().getSelectedItem() != null) {
			Rolle selectedRole = roleTable.getSelectionModel().getSelectedItem();
			Rolle updateRole = new Rolle(
					op.getSelectionModel().getSelectedItem().getOpId(), //opId
					MainController.getUserId(), //bearbeiter
					role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
					LocalDateTime.now(), //bearbeiterZeit
					selectedRole.getErstellZeit(), //ersteller Zeit
					selectedRole.getErsteller(), //ersteller
					mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonal Personal-ID
					selectedRole.getStorniert() //storniert
			);
			RolleDao roleDao = new RolleDao(Main.configuration);
			// test if role with this operation and medical_user is already in the database
			List<Rolle> allRoles = roleDao.findAll();
			boolean roleExists = false;
			for (Rolle r : allRoles){
				if (r.getOpId() == updateRole.getOpId() && r.getMedPersonalPersId().equals(updateRole.getMedPersonalPersId()) &&
				 (r.getOpId() != selectedRole.getOpId() || !(r.getMedPersonalPersId().equals(selectedRole.getMedPersonalPersId())))){
					roleExists = true;
				}
			}
			if(roleExists){
				Main.logger.warning("Es existiert bereits ein Rollen-Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter.");
				Alert warning = new Alert(AlertType.WARNING);
				warning.setTitle("Error");
				warning.setHeaderText("Eintrag mit diesem Schlüssel bereits vorhanden.");
				warning.setContentText("Es existiert bereits ein Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter. ");
				warning.showAndWait();
			}
			else {
				roleDao.update(updateRole);
				Main.logger.info("Der Datensatz wurde geupdated.");
				Alert confirm = new Alert(AlertType.INFORMATION);
				confirm.setTitle("Information");
				confirm.setHeaderText("Erfolgreich eingefügt");
				confirm.setContentText("Der Datensatz wurde geupdatet.");
				confirm.showAndWait();
			}

			//close the window
			Stage stage = (Stage) speichern.getScene().getWindow();
			stage.close();
		}
		else{
			Main.logger.warning("Fehlende Auswahl: Es muss eine Rolle zum Bearbeiten ausgewählt werden.");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fehlende Auswahl!");
			alert.setContentText("Es muss eine Rolle zum Bearbeiten ausgewählt werden!");
			alert.showAndWait();
		}
	}


	/**
	 * Collects all Roles from the Database and saves them in a observable Array List from Type Role pojo
	 */
	public void roleView(int opID){
		RolleDao roleDao = new RolleDao(Main.configuration);
		List<Rolle> role = roleDao.fetchByOpId(opID);
		roleTable.setItems(FXCollections.observableArrayList(role));
	}


	/**
	 * Gets triggered when a Role in the TableView gets selected.
	 * It sets the values of the selected role as values of the ComboBoxes.
	 */
	@FXML
	void roleClicked() {
		roleTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() > 0) {
				if (roleTable.getSelectionModel().getSelectedItem() != null) {
					Rolle editRole = roleTable.getSelectionModel().getSelectedItem();
					MedPersonal medPersX = new MedPersonalDao(Main.configuration).fetchOneByPersId(editRole.getMedPersonalPersId());
					MedPersonal medPers = new MedPersonal(medPersX) {
						@Override
						public String toString() {
							return medPersX.getNachnameVorname() + " " +
									medPersX.getPersId();
						}
					};
					Operation operationX = new OperationDao(Main.configuration).fetchOneByOpId(editRole.getOpId());
					Operation operation = new Operation(operationX) {
						@Override
						public String toString() {
							return "OP: " + operationX.getOpId() + ", Fall: " + operationX.getFallId() +
									", Datum: " + Converter.dateTimeConverter(operationX.getBeginn(),false);
						}
					};
					RolleSt roleStX = new RolleStDao(Main.configuration).fetchOneByRolle(editRole.getRolleSt());
					RolleSt roleSt = new RolleSt(roleStX) {
						@Override
						public String toString() {
							return roleStX.getBezeichnung();
						}
					};
					mitarbeiter.setValue(medPers);
					op.setValue(operation);
					role.setValue(roleSt);
				}
			}
		});
	}

	/**
	 * This method is called when initialising the window.
	 * It sets all role types of the database as choosing options of the combobox.
	 */
	private void setRole() {
		Converter.setRolle(role);
	}

	/**
	 * This method is called when initialising the window.
	 * It sets all operations of the database as choosing options of the combobox.
	 */
	private void setOp() {
		Converter.setOperation(op, "role");
	}

	/**
	 * This method is called when initialising the window.
	 * It sets all medical users of the database as choosing options of the combobox.
	 */
	private void setMitarbeiter() {
		Converter.setMitarbeiter(mitarbeiter, false, 0);
	}

	private String booleanToString(Boolean notfall){
		if(notfall){
			return "ja";
		}
		else{
			return "nein";
		}
	}
}
