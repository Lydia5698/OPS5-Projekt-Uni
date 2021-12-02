package controller;
import ExternalFiles.Converter;
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

public class DiagnosisController {
	
	@FXML
	private ComboBox<Operation> diagnosisOpId;
	@FXML
	private ComboBox<Icd10CodeSt> diagnosisIcdCode;
	@FXML
	private ComboBox<DiagnosetypSt> diagnosisType;
	@FXML
	private TextField diagnosisFreetext;	
	@FXML
	private TableView<Diagnose> diagnosisTable;
	@FXML
	private TableColumn<Diagnose, Integer> diagIDCol;

	@FXML
	private TableColumn<Diagnose, String> textCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> dateCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> erstellzeitCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> bearbeiterzeitCol;

	@FXML
	private TableColumn<Diagnose, Boolean> storniertCol;

	@FXML
	private TableColumn<Diagnose, Integer> opIDCol;

	@FXML
	private TableColumn<Diagnose, String> diagnosetypCol;

	@FXML
	private TableColumn<Diagnose, String> icdCol;

	@FXML
	private TableColumn<Diagnose, String> erstellerCol;

	@FXML
	private TableColumn<Diagnose, String> bearbeiterCol;

	@FXML
	private Button btnDiagnose;

	@FXML
	private DatePicker dateDiagnosis;


	boolean flagEditDiagnose = false;

	@FXML
	public void initialize() {

		System.out.println("Initialize Diagnosis-Tab!");
		initializeColumns();

		diagnosisTable.setItems(diagnoseView());
		setDiagnosisOpId();
		setDiagnosisIcdCode();
		setDiagnosisDiagnoseTyp();

	}
	
	@FXML
	public void createDiagnosis(ActionEvent event){
		System.out.println("Create diagnosis!");
		flagEditDiagnose = true;
		if(noMissingStatement()){
			insertNewDiagnose();
			Node source = (Node) event.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		}
	}


	@FXML
	void createNewDiagnosis(ActionEvent event) {
		flagEditDiagnose = false;
		if(noMissingStatement()){
			insertNewDiagnose();
			Node source = (Node) event.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		}
	}

	public static ObservableList<Diagnose> diagnoseView(){
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
		List<Diagnose> diagnose = diagnoseDao.findAll();
		return FXCollections.observableArrayList(diagnose);
	}

	private void initializeColumns() {
		// tabellencols werden erstellt
		// create columns

		// columns Case
		diagIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getDiagnoseId()));
		textCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getKlartextDiagnose()));
		dateCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getDatum()));
		erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
		opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
		diagnosetypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.diagnoseTypConverter(features.getValue().getDiagnosetyp())));
		icdCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getIcd10Code()));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
	}

	private void insertNewDiagnose() {
		Integer diagID = null; // durch null wird sie automatisch generiert
		boolean storniert = false;
		Integer opID = null;
		if(!diagnosisOpId.getSelectionModel().isEmpty()){
			opID = diagnosisOpId.getValue().getOpId();
		}
		String icdCode = diagnosisIcdCode.getValue().getIcd10Code();
		Integer diagTyp = diagnosisType.getValue().getDiagnosetyp();
		String freitext = diagnosisFreetext.getText();
		//LocalDateTime datum = dateDiagnosis.getValue().atStartOfDay(); // TODO: 25.11.21 local Date to Local date time
		LocalDateTime datum;
		String ersteller = null;
		LocalDateTime erstellZeit = null;
		String bearbeiter = null;
		LocalDateTime bearbeiterZeit = null; // bei null update nimmt er immer das vorhandene

		if(flagEditDiagnose){
			diagID = onEditDiagnose();
			bearbeiter = "0101040";
			bearbeiterZeit = LocalDateTime.now();
			datum = LocalDateTime.now();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit,bearbeiterZeit,storniert,opID,diagTyp,icdCode,ersteller,bearbeiter);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.update(diagnose);
		}
		else{
			ersteller = "00191184";
			erstellZeit = LocalDateTime.now();
			datum = LocalDateTime.now();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit,bearbeiterZeit,storniert,opID,diagTyp,icdCode,ersteller,bearbeiter);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.insert(diagnose);
		}

	}

	/*
	 boolean insertEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(Employee employee);
	 */

	public int onEditDiagnose() {
		int id = 0;
		// check the table's selected item and get selected item
		if (diagnosisTable.getSelectionModel().getSelectedItem() != null) {
			Diagnose selectedItem = diagnosisTable.getSelectionModel().getSelectedItem();
			id = selectedItem.getDiagnoseId();
		}
		return id;

	}
	private void setDiagnosisOpId(){
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
		diagnosisOpId.setButtonCell(cellFactory.call(null));
		diagnosisOpId.setCellFactory(cellFactory);
		diagnosisOpId.getItems().setAll(new OperationDao(Main.configuration).findAll());
	}

	private void setDiagnosisIcdCode(){
		Callback<ListView<Icd10CodeSt>, ListCell<Icd10CodeSt>> cellFactory = new Callback<>() {
			@Override
			public ListCell<Icd10CodeSt> call(ListView<Icd10CodeSt> medPersonalListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Icd10CodeSt icd10Code, boolean empty) {
						super.updateItem(icd10Code, empty);
						if (icd10Code == null || empty) {
							setGraphic(null);
						} else {
							setText(icd10Code.getIcd10Code() + " " + icd10Code.getBeschreibung());
						}
					}
				};
			}
		};
		diagnosisIcdCode.setButtonCell(cellFactory.call(null));
		diagnosisIcdCode.setCellFactory(cellFactory);
		diagnosisIcdCode.getItems().setAll(new Icd10CodeStDao(Main.configuration).findAll());
	}

	private void setDiagnosisDiagnoseTyp(){
		Callback<ListView<DiagnosetypSt>, ListCell<DiagnosetypSt>> cellFactory = new Callback<>() {
			@Override
			public ListCell<DiagnosetypSt> call(ListView<DiagnosetypSt> medPersonalListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(DiagnosetypSt diagnosetypSt, boolean empty) {
						super.updateItem(diagnosetypSt, empty);
						if (diagnosetypSt == null || empty) {
							setGraphic(null);
						} else {
							setText(diagnosetypSt.getBeschreibung());
						}
					}
				};
			}
		};
		diagnosisType.setButtonCell(cellFactory.call(null));
		diagnosisType.setCellFactory(cellFactory);
		diagnosisType.getItems().setAll(new DiagnosetypStDao(Main.configuration).findAll());
	}

	@FXML
	void diagnosisClicked(MouseEvent event) {
		flagEditDiagnose = true;
	}

	public boolean noMissingStatement(){

		if(diagnosisIcdCode.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlender Diagnose-Code ");
			alert.setContentText("Bitte wählen Sie einen Diagnose-Code aus aus");
			alert.show();
			return false;
		}

		if(diagnosisIcdCode.getSelectionModel().getSelectedItem().getIcd10Code().endsWith("-")){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlender Diagnose-Code");
			alert.setContentText("Bitte wählen Sie einen endständigen Diagnose-Code aus");
			alert.show();
			return false;
		}

		/*if(dateDiagnosis.get){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlendes Datum");
			alert.setContentText("Bitte tragen Sie ein Datum für die Diagnose ein");
			alert.show();
			return true;
		}*/

		if(diagnosisType.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlender Diagnosetyp");
			alert.setContentText("Bitte wählen Sie einen Diagnosetyp aus");
			alert.show();
			return false;
		}


		if(diagnosisTable.getSelectionModel().isEmpty() && flagEditDiagnose){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlende Diagnose");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus");
			alert.show();
			return false;
		}
		return true;

	}









}
