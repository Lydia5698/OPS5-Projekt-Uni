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
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This Controller displays the Diagnosis. You can create a new one or edit an existent
 */
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
	private DateTimePicker dateDiagnosis;


	boolean flagEditDiagnose = false;
	private static Integer opID;

	/**
	 * This Methode initialize the TableView for the existing Diagnosis and shows the Op-IDs, ICD-10 codes and
	 * the Diagnosis Type in the Comboboxes
	 */
	@FXML
	public void initialize() {

		System.out.println("Initialize Diagnosis-Tab!");

		initializeColumns();
		diagnosisTable.setItems(diagnoseView());
		setDiagnosisOpId();
		setDiagnosisIcdCode();
		setDiagnosisDiagnoseTyp();

	}

	/**
	 * Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
	 * a Diagnosis. If the User isn't missing any necessary Values the Diagnose is edited and the Window closes
	 * @param event the event of pushing the Speichern Button
	 */
	@FXML
	public void editDiagnosis(ActionEvent event){
		System.out.println("Create diagnosis!");
		flagEditDiagnose = true;
		if(diagnosisTable.getSelectionModel().isEmpty() && flagEditDiagnose){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlende Diagnose");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus");
			alert.show();
		}
		else if(diagnosisIcdCode.getSelectionModel().getSelectedItem().getIcd10Code().endsWith("-")){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Fehlender Diagnose-Code");
			alert.setContentText("Bitte wählen Sie einen endständigen Diagnose-Code aus");
			alert.show();

		}

		else{
			insertNewDiagnose();
			Node source = (Node) event.getSource();
			Stage thisStage = (Stage) source.getScene().getWindow();
			thisStage.close();
		}


	}


	/**
	 * Launches when the Button Neue Diagnose is pressed. It sets the flag false so that we know that the user wants to create
	 * a new Diagnosis. If the User isn't missing any necessary Values the Diagnose is saved and the Window closes
	 * @param event the event of pushing the Neue Diagnose Button
	 */
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

	/**
	 * Collects all Diagnosis from the Database and saves them in a observable Array List from Type Diagnose pojo
	 * @return all Diagnosis
	 */
	public static ObservableList<Diagnose> diagnoseView(){
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration); // Patient->Fall->OP->Diagnose
		List<Diagnose> diagnose = diagnoseDao.findAll(); // TODO: 12.12.21 auch von Fall zu Diagnose? Wie?
		OperationDao operationDao = new OperationDao(Main.configuration);
		//Operation operation = operationDao.fetchOneByOpId(diagnose)

		Predicate<Diagnose> byOpID = diagnose1 -> diagnose1.getOpId().equals(opID);
		var result = diagnose.stream().filter(byOpID)
				.collect(Collectors.toList());

		return FXCollections.observableArrayList(result);
	}

	/**
	 * Initializes all Columns from the Table View diagnosisTable
	 */
	private void initializeColumns() {
		// create columns

		// columns Diagnosis
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

	/**
	 * Inserts or edits a Diagnose in the Database.
	 */
	private void insertNewDiagnose() {
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
		Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
		confirm.showAndWait();

	}

	/**
	 * Gets the Diagnosis ID from the selected Diagnosis in the Table View
	 * @return Diagnosis ID
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

	/**
	 * Gets all the Operation IDs and saves them in the diagnosisOpId Combobox
	 */
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

	/**
	 * Gets all the ICD-10 codes and the description and saves them in the diagnosisIcdCode Combobox
	 */
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

	/**
	 * Gets all the Diagnosis Types and saves them in the diagnosisType Combobox
	 */
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

	/**
	 * Gets triggered when a Diagnosis in the TableView gets selected
	 */
	@FXML
	void diagnosisClicked(MouseEvent event) {
		flagEditDiagnose = true;
		if(event.getClickCount() > 0){
			Diagnose diagnose = diagnosisTable.getSelectionModel().getSelectedItem();
			Icd10CodeSt icd10CodeSt = new Icd10CodeStDao(Main.configuration).fetchOneByIcd10Code(diagnose.getIcd10Code());
			Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(diagnose.getOpId());
			DiagnosetypSt diagnosetypSt = new DiagnosetypStDao(Main.configuration).fetchOneByDiagnosetyp(diagnose.getDiagnosetyp());
			diagnosisIcdCode.setValue(icd10CodeSt);
			diagnosisOpId.setValue(operation);
			diagnosisType.setValue(diagnosetypSt);
		}
	}

	/**
	 * Checks if all the necessary Values for the Diagnosis are selected
	 * @return boolean if no Statement is missing
	 */
	public boolean noMissingStatement(){

		if(diagnosisIcdCode.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehlender Diagnose-Code ");
			alert.setContentText("Bitte wählen Sie einen Diagnose-Code aus aus");
			alert.show();
			return false;
		}

		if(diagnosisIcdCode.getSelectionModel().getSelectedItem().getIcd10Code().endsWith("-")){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehlender Diagnose-Code");
			alert.setContentText("Bitte wählen Sie einen endständigen Diagnose-Code aus");
			alert.show();
			return false;
		}

		if(diagnosisType.getSelectionModel().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehlender Diagnosetyp");
			alert.setContentText("Bitte wählen Sie einen Diagnosetyp aus");
			alert.show();
			return false;
		}


		if(diagnosisTable.getSelectionModel().isEmpty() && flagEditDiagnose){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehlende Diagnose");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus");
			alert.show();
			return false;
		}
		return true;

	}

	public Integer getOpID() {
		return opID;
	}

	public static void setOpID(Integer opID) {
		DiagnosisController.opID = opID;
	}

}
