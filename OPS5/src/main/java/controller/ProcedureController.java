package controller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.ProzedurDao;
import jooq.tables.pojos.Diagnose;
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
	private ComboBox<?> procedureOp;

	@FXML
	private ComboBox<?> procedureOpsCode;
	
	
    @FXML
	public void initialize() {

    	System.out.println("Initialize Procedure-Tab!");
    	initializeColumns();
		procedureTable.setItems(prozedurView());

	}
	
	@FXML
	public void createProcedure() {
		System.out.println("Create procedure!");
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

}
