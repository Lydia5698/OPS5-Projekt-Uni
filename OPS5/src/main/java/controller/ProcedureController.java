package controller;
import com.google.protobuf.NullValue;
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
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This Controller displays the Procedure. You can create a new one or edit an existent
 */
public class ProcedureController {
	
	@FXML
	private TableView<Prozedur> procedureTable;

	@FXML
	private TableColumn<Prozedur, Integer> prozCol;

	@FXML
	private TableColumn<Prozedur, String> anmerkungCol;

	@FXML
	private TableColumn<Prozedur, Boolean> storniertCol;

	@FXML
	private TableColumn<Prozedur, LocalDateTime> erstelltzeitCol;

	@FXML
	private TableColumn<Prozedur, LocalDateTime> bearbeiterzeitCol;

	@FXML
	private TableColumn<Prozedur, Integer> opIDCol;

	@FXML
	private TableColumn<Prozedur, String> opsCol;

	@FXML
	private TableColumn<Prozedur, String> bearbeiterCol;

	@FXML
	private TableColumn<Prozedur, String> erstellerCol;

	@FXML
	private ComboBox<Operation> procedureOpID;

	@FXML
	private ComboBox<OpsCodeSt> procedureOpsCode;

	@FXML
	private TextField procedureAnmerkung;

	boolean flagEditProzedure = false;

	/**
	 * This Methode initialize the TableView for the existing Procedures and shows the Op-IDs and the OPs codes
	 * in the Comboboxes
	 */
    @FXML
	public void initialize() {

    	System.out.println("Initialize Procedure-Tab!");
    	initializeColumns();
		procedureTable.setItems(prozedurView());
		setProcedureOpID();
		setProcedureOpsCode();
	}

	/**
	 *  Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
	 * 	a Procedure. If the User isn't missing any necessary Values the Procedure is edited and the Window closes
	 * 	@param event the event of pushing the Speichern Button
	 */
	@FXML
	public void createProcedure(ActionEvent event) {
		flagEditProzedure = true;
    	System.out.println("Create procedure!");
		if(noMissingStatement()){
			insertNewProcedure();
			Node source = (Node) event.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		}

	}

	/**
	 * Launches when the Button Neue Prozedur is pressed. It sets the flag false so that we know that the user wants to create
	 * a new Procedure. If the User isn't missing any necessary Values the Procedure is saved and the Window closes
	 * @param event the event of pushing the Neue Diagnose Button
	 */
	@FXML
	void createNewProcedure(ActionEvent event) {
		flagEditProzedure = false;
		if(noMissingStatement()){
			insertNewProcedure();
			Node source = (Node) event.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		}
	}

	/**
	 *  Collects all Procedures from the Database and saves them in a observable Array List from Type Prozedur pojo
	 *  @return all Procedures
	 */
	public static ObservableList<Prozedur> prozedurView(){
		ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
		List<Prozedur> prozedur = prozedurDao.findAll();
		return FXCollections.observableArrayList(prozedur);
	}

	/**
	 *  Initializes all Columns from the Table View procedureTable
	 */
	private void initializeColumns() {
		// create columns

		// columns Procedure
		prozCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getProzId()));
		anmerkungCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getAnmerkung()));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
		erstelltzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
		opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
		opsCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpsCode()));
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiter()));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErsteller()));
	}

	/**
	 * Inserts or edits a Procedure in the Database.
	 */
	private void insertNewProcedure() {
    	Integer prozID = null;
		boolean storniert = false;
    	Integer opID = procedureOpID.getValue().getOpId();
    	String opsCodeValue = procedureOpsCode.getValue().getOpsCode();
    	String anmerkungText = procedureAnmerkung.getText();
    	LocalDateTime erstellZeit = null;
    	LocalDateTime bearbeiterZeit = null;
    	String bearbeiter = null;
    	String ersteller = null;

    	// Edit Procedure
		if(flagEditProzedure){
			prozID = onEditProzedur();
			bearbeiterZeit = LocalDateTime.now();
			bearbeiter = MainController.getUserId();
			Prozedur prozedur = new Prozedur(prozID,anmerkungText,storniert,erstellZeit,bearbeiterZeit,opID,opsCodeValue,bearbeiter,ersteller);
			ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
			prozedurDao.update(prozedur);

		}
		// new Procedure
		else {
			erstellZeit = LocalDateTime.now();
			ersteller = MainController.getUserId();
			Prozedur prozedur = new Prozedur(prozID,anmerkungText,storniert,erstellZeit,bearbeiterZeit,opID,opsCodeValue,bearbeiter,ersteller);
			ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
			prozedurDao.insert(prozedur);
		}

    }

	/**
	 * Gets the Procedure ID from the selected Procedure in the Table View
	 * @return Procedure ID
	 */
	public int onEditProzedur() {
		int id = 0;
		// check the table's selected item and get selected item
		if (procedureTable.getSelectionModel().getSelectedItem() != null) {
			Prozedur selectedItem = procedureTable.getSelectionModel().getSelectedItem();
			id = selectedItem.getProzId();
		}
		return id;

	}

	/**
	 *  Checks if all the necessary Values for the Procedure are selected
	 * 	@return boolean if no Statement is missing
	 */
	public boolean noMissingStatement(){
		if(procedureOpID.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlende OP-ID");
			alert.setContentText("Bitte wählen Sie eine Operations-ID aus");
			alert.show();
			return false;
		}

		if(procedureOpsCode.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlender OPS-Code");
			alert.setContentText("Bitte wählen Sie einen OPS-Code aus");
			alert.show();
			return false;
		}

		if(procedureTable.getSelectionModel().isEmpty() && flagEditProzedure){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlende Prozedur");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Prozedur in der Tabelle aus");
			alert.show();
			return false;
		}
		return true;
	}

	/**
	 * Gets triggered when a Procedure in the TableView gets selected
	 */
	@FXML
	void mouseEntered() {
		flagEditProzedure = true;
	}

	/**
	 * Gets all the Operation IDs and saves them in the procedureOpID Combobox
	 */
	private void setProcedureOpID(){
		Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
			@Override
			public ListCell<Operation> call(ListView<Operation> medPersonalListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Operation operation, boolean empty) {
						super.updateItem(operation, empty);
						if (operation == null || empty) {
							setGraphic(null);
						} else {
							setText(operation.getOpId().toString());
						}
					}
				};
			}
		};
		procedureOpID.setButtonCell(cellFactory.call(null));
		procedureOpID.setCellFactory(cellFactory);
		procedureOpID.getItems().setAll(new OperationDao(Main.configuration).findAll());
	}

	/**
	 * Gets all the OPs codes and saves them in the procedureOpsCode Combobox
	 */
	private void setProcedureOpsCode(){
		Callback<ListView<OpsCodeSt>, ListCell<OpsCodeSt>> cellFactory = new Callback<>() {
			@Override
			public ListCell<OpsCodeSt> call(ListView<OpsCodeSt> medPersonalListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(OpsCodeSt opsCodeSt, boolean empty) {
						super.updateItem(opsCodeSt, empty);
						if (opsCodeSt == null || empty) {
							setGraphic(null);
						} else {
							setText(opsCodeSt.getOpsCode() + " " + opsCodeSt.getBeschreibung());
						}
					}
				};
			}
		};
		procedureOpsCode.setButtonCell(cellFactory.call(null));
		procedureOpsCode.setCellFactory(cellFactory);
		procedureOpsCode.getItems().setAll(new OpsCodeStDao(Main.configuration).findAll());
	}
}
