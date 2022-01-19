package controller;
import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import ExternalFiles.DateTimePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.controlsfx.control.SearchableComboBox;
import org.jooq.meta.derby.sys.Sys;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This Controller displays the Diagnosis. You can create a new one or edit an existent
 */
public class DiagnosisController {
	
	@FXML
	private SearchableComboBox<Operation> diagnosisOpId;
	@FXML
	private SearchableComboBox<Icd10CodeSt> diagnosisIcdCode;
	@FXML
	private SearchableComboBox<DiagnosetypSt> diagnosisType;
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

	/**
	 * This Methode initialize the TableView for the existing Diagnosis and shows the Op-IDs, ICD-10 codes and
	 * the Diagnosis Type in the Comboboxes
	 */
	@FXML
	public void initialize() {

		System.out.println("Initialize Diagnosis-Tab!");
		initializeColumns();
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
	 */
	public void diagnoseView(Integer opID){
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration); // Patient->Fall->OP->Diagnose
		List<Diagnose> diagnose = diagnoseDao.findAll();
		if(opID == 0){
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			confirm.setContentText("Es werden zurzeit alle Diagnosen angezeigt. Bitte wähle eine Operation aus, um eine spezifische Diagnose zu sehen.");
			confirm.showAndWait();
			diagnosisTable.setItems(FXCollections.observableArrayList(diagnose));
		}
		else {
			List<Diagnose> result = diagnoseDao.fetchByOpId(opID);
			diagnosisTable.setItems(FXCollections.observableArrayList(result));
		}
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
		diagnosisOpId.setSelectionModel(new CustomSelectionModel<>(diagnosisOpId));
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
		diagnosisIcdCode.setSelectionModel(new CustomSelectionModel<>(diagnosisIcdCode));
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
		diagnosisType.setSelectionModel(new CustomSelectionModel<>(diagnosisType));
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

	/**
	 * If you want to see information about a diagnosis the JSONObject of the icd-10 code is returned.
	 * If the chosen code returns no result the code is modified a few times until a valid result is returned
	 * or an alert is shown when no result is given.
	 */
	@FXML
	public void showInfo() {
		Icd10CodeSt code = diagnosisIcdCode.getValue();
		if(code==null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Kein Code ausgewählt.");
			alert.setContentText("Für weitere Informationen muss ein ICD-10 Code ausgewählt werden");
			alert.showAndWait();
			return;
		}
		try {
			JSONObject result = searchForResult(code);
			if(result==null) {
				Alert infoalert = new Alert(Alert.AlertType.INFORMATION);
				infoalert.setHeaderText("Kein Ergebnis gefunden");
				infoalert.setContentText("Es konnte keine Information zu Ihrer Anfrage gefunden werden!");
				infoalert.show();
			} else {
				JSONObject match = findJsonByName((JSONArray) result.get("parameter"), "match");
				JSONObject concept = findJsonByName((JSONArray) match.get("part"), "concept");
				JSONObject coding = (JSONObject) concept.get("valueCoding");
				String display = (String) coding.get("display");
				openWebView("https://pubmed.ncbi.nlm.nih.gov/?term=" + display + "%5BMeSH+Major+Topic%5D");
			}


		} catch (Exception e) {e.printStackTrace();}
	}


	 /**
	  * Opens a new Window for the Web View
	 */
	@FXML
	public void openWebView(String url) {
		System.out.println("New Patient Window!");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/fxml/WebView.fxml"));
			Parent root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			WebViewController controller = fxmlLoader.getController();
			controller.webView(url);
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	private JSONObject getJsonForCode(String code) throws Exception {
		URL url = new URL("https://fhir.imi.uni-luebeck.de/fhir/ConceptMap/$translate?url=http://imi.uni-luebeck.de/ehealth/fhir/ConceptMap/icd-10-to-msh&code="+code+"&system=http://fhir.de/CodeSystem/bfarm/icd-10-gm");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("accept", "application/json");
		InputStream responseStream = connection.getInputStream();
		JSONParser jsonParser = new JSONParser();
		return (JSONObject)jsonParser.parse(
				new InputStreamReader(responseStream, "UTF-8"));
	}

	private JSONObject searchForResult(Icd10CodeSt code) throws Exception {
		JSONObject result = getJsonForCode(code.getIcd10Code());
		if(wasFound(result)){return result;}
		String shortCode = code.getIcd10Code().substring(0,3);
		result = getJsonForCode(shortCode);
		if (wasFound(result)) { return result;}

		for(int i=9; i>=0; i--){
			String codeX = shortCode + "." + i;
			result = getJsonForCode(codeX);
			if (wasFound(result)){return result;}
		}
		return null;
	}

	private boolean wasFound(JSONObject json) {
		JSONArray array = (JSONArray) json.get("parameter");
		for (int i = 0; i < array.size(); i++) {
			JSONObject item = (JSONObject) array.get(i);
			if(item.get("name").equals("result") && (Boolean) item.get("valueBoolean")) {
				return true;
			}
		}
		return false;
	}

	private JSONObject findJsonByName(JSONArray array, String name) {
		if (array==null) {return null;}
		for (int i = 0; i < array.size(); i++) {
			JSONObject item = (JSONObject) array.get(i);
			if(item.get("name").equals(name)) {
				return item;
			}
		}
		return null;
	}
}
