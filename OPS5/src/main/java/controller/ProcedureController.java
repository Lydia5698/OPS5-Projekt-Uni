package controller;
import ExternalFiles.Converter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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

    @FXML
	public void initialize() {

    	System.out.println("Initialize Procedure-Tab!");
    	initializeColumns();
		procedureTable.setItems(prozedurView());
		setProcedureOpID();
		setProcedureOpsCode();
	}
	
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
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
	}

	private void insertNewProcedure() {
    	Integer prozID = null; // durch null automatisch generiert
		boolean storniert = false;
    	Integer opID = procedureOpID.getValue().getOpId(); //abfangen wenn nichts ausgewählt
    	String opsCodeValue = procedureOpsCode.getValue().getOpsCode();
    	String anmerkungText = procedureAnmerkung.getText();
    	LocalDateTime erstellZeit = null; // nur beim neuen erstellen
    	LocalDateTime bearbeiterZeit = null;
    	String bearbeiter = null; // eingeloggter Benutzer
    	String ersteller = "00191184"; // TODO: 25.11.21 bearbeiter ersteller eingeloggter Benutzer

		if(flagEditProzedure){
			prozID = onEditProzedur();
			bearbeiterZeit = LocalDateTime.now();
			bearbeiter = "0101040";
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
