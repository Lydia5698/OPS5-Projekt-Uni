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

public class ProcedureController {
	
	@FXML
	private TableView<Prozedur> procedureTable;

	@FXML
	private TableColumn<Prozedur, Integer> prozCol;

	@FXML
	private TableColumn<Prozedur, String> anmerkungCol;

	@FXML
	private TableColumn<Prozedur, Byte> storniertCol;

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

    @FXML
	public void initialize() {

    	System.out.println("Initialize Procedure-Tab!");
    	initializeColumns();
		procedureTable.setItems(prozedurView());
		setProcedureOpID();
		setProcedureOpsCode();
		// TODO: 23.11.21 Daten in DB speichern
		// TODO: 24.11.21 storniert ist noch da nur nicht sichtbar? 
		// TODO: 24.11.21 buttons speichern und neu erstellen

	}
	
	@FXML
	public void createProcedure(ActionEvent event) {

    	System.out.println("Create procedure!");
		Node source = (Node) event.getSource();
		Stage thisStage = (Stage) source.getScene().getWindow();
		thisStage.close();
	}
	@FXML
	void createNewProcedure(ActionEvent event) {
		flagEditProzedure = false;
		insertNewProcedure();
		Node source = (Node) event.getSource();
		Stage thisStage = (Stage) source.getScene().getWindow();
		thisStage.close();



	}

	public static ObservableList<Prozedur> prozedurView(){
		ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
		List<Prozedur> prozedur = prozedurDao.findAll();
		return FXCollections.observableArrayList(prozedur);
	}

	private void initializeColumns() {
		// tabellencols werden erstellt
		// create columns

		// columns Case
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

	private void insertNewProcedure() {
    	int prozID = 8; // automatisch generieren?
		Byte storniert = 0;
    	int opID = procedureOpID.getValue().getOpId(); //abfangen wenn nichts ausgewählt
    	String opsCodeValue = procedureOpsCode.getValue().getOpsCode();
    	String anmerkungText = procedureAnmerkung.getText();
    	LocalDateTime erstellZeit = null; // nur beim neuen erstellen
    	LocalDateTime bearbeiterZeit = null;
    	String bearbeiter = null; // eingeloggter Benutzer
    	String ersteller = "00191184"; // TODO: 25.11.21 bearbeiter ersteller eingeloggter Benutzer

		if(flagEditProzedure){
			prozID = onEditProzedur();
			bearbeiterZeit = LocalDateTime.now();
			// bearbeiter
			Prozedur prozedur = new Prozedur(prozID,anmerkungText,storniert,erstellZeit,bearbeiterZeit,opID,opsCodeValue,bearbeiter,ersteller);
			ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
			prozedurDao.update(prozedur);
		}
		else {
			erstellZeit = LocalDateTime.now();
			// ersteller
			Prozedur prozedur = new Prozedur(prozID,anmerkungText,storniert,erstellZeit,bearbeiterZeit,opID,opsCodeValue,bearbeiter,ersteller);
			ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
			prozedurDao.insert(prozedur);
		}



    }

	/*
	 boolean insertEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(Employee employee);
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

	@FXML
	void mouseEntered() {
		flagEditProzedure = true;
	}

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
