package controller;
import com.google.protobuf.NullValue;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.ProzedurDao;
import jooq.tables.pojos.Diagnose;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Prozedur;
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
	private ComboBox<Integer> procedureOpID;

	@FXML
	private ComboBox<String> procedureOpsCode;

	@FXML
	private TextField procedureAnmerkung;

	boolean flagEditProzedure = false;

    @FXML
	public void initialize() {

    	System.out.println("Initialize Procedure-Tab!");
    	initializeColumns();
		procedureTable.setItems(prozedurView());
		// TODO: 23.11.21 Daten in DB speichern

	}
	
	@FXML
	public void createProcedure() {

    	System.out.println("Create procedure!");
    	insertNewProcedure();
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
		Byte storniert = null;
    	int opID = procedureOpID.getValue();
    	String opsCodeValue = procedureOpsCode.getValue();
    	String anmerkungText = procedureAnmerkung.getText();
    	LocalDateTime erstellZeit = null; // nur beim neuen erstellen
    	LocalDateTime bearbeiterZeit = null;
    	String bearbeiter = null; // eingeloggter Benutzer
    	String ersteller = null;

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

}
