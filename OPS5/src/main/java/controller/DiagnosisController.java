package controller;
import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import ExternalFiles.DateTimePicker;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
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
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

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
	private TableColumn<Diagnose, String> dateCol;

	@FXML
	private TableColumn<Diagnose, String> erstellzeitCol;

	@FXML
	private TableColumn<Diagnose, String> bearbeiterzeitCol;

	@FXML
	private TableColumn<Diagnose, String> storniertCol;

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
	 * the Diagnosis Type in the Combobox
	 */
	@FXML
	public void initialize() {
		initializeColumns();
		setDiagnosisOpId();
		setDiagnosisIcdCode();
		setDiagnosisDiagnoseTyp();
	}

	/**
	 * Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
	 * a Diagnosis. If the User isn't missing any necessary Values the Diagnose is edited and the Window closes
	 * @param event The event of pushing the Speichern Button
	 */
	@FXML
	public void editDiagnosis(ActionEvent event){
		flagEditDiagnose = true;
		if(diagnosisTable.getSelectionModel().isEmpty()){
			Main.logger.warning("Fehlende Diagnose: Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus.");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fehlende Diagnose");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus.");
			alert.showAndWait();
		}
		else if(diagnosisIcdCode.getSelectionModel().getSelectedItem().getIcd10Code().endsWith("-")){
			Main.logger.warning("Fehlender Diagnose-Code: Bitte wählen Sie einen endständigen Diagnose-Code aus.");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fehlender Diagnose-Code");
			alert.setContentText("Bitte wählen Sie einen endständigen Diagnose-Code aus.");
			alert.showAndWait();
		} 
		else if(diagnosisFreetext.getText() != null && diagnosisFreetext.getText().matches(Main.blockedCharsForHL7)) {
			Main.logger.warning("Falscher Eintrag: Sonderzeichen sind für die HL7 Nachrichten blockiert.");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setHeaderText("Falscher Eintrag");
			alert.setContentText("Es dürfen keine Sonderzeichen verwendet werden (&,^,\\,~)!");
			alert.show();
		}
		else if(diagnosisFreetext.getText() != null && diagnosisFreetext.getText().length() > 200){
			Main.logger.warning("Falscher Eintrag: Die Länge des Freitextes ist zu lang.");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setHeaderText("Falscher Eintrag");
			alert.setContentText("Der Eintrag des Freitextes ist zu lang");
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
	 * @param event The event of pushing the Neue Diagnose Button
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
			Main.logger.info("Es werden zurzeit alle Diagnosen angezeigt. Bitte wähle eine Operation aus, um eine spezifische Diagnose zu sehen.");
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			confirm.setTitle("Information");
			confirm.setHeaderText("Alle Diagnosen");
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
		dateCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getDatum(), true)));
		erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
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
		Integer opID = diagnosisOpId.getValue().getOpId();
		String icdCode = diagnosisIcdCode.getValue().getIcd10Code();
		Integer diagTyp = diagnosisType.getValue().getDiagnosetyp();
		String freitext = diagnosisFreetext.getText();
		LocalDateTime datum;
		String ersteller;
		LocalDateTime erstellZeit;
		String bearbeiter;
		LocalDateTime bearbeiterZeit;

		// Edits Diagnosis
		if(flagEditDiagnose){
			diagID = onEditDiagnose();
			bearbeiter = MainController.getUserId();
			bearbeiterZeit = LocalDateTime.now();
			datum = dateDiagnosis.getDateTimeValue();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum, null,bearbeiterZeit, false,opID,diagTyp,icdCode, null,bearbeiter);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.update(diagnose);
			Main.logger.info("Der Datensatz wurde geupdatet.");
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			confirm.setTitle("Information");
			confirm.setHeaderText("Erfolgreich geupdatet");
			confirm.setContentText("Der Datensatz wurde geupdatet.");
			confirm.showAndWait();
		}
		// Creates new Diagnosis
		else{
			ersteller = MainController.getUserId();
			erstellZeit = LocalDateTime.now();
			datum = LocalDateTime.now();

			Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit, null, false,opID,diagTyp,icdCode,ersteller, null);
			DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
			diagnoseDao.insert(diagnose);
			Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
			Alert confirm = new Alert(Alert.AlertType.INFORMATION);
			confirm.setTitle("Information");
			confirm.setHeaderText("Erfolgreich eingefügt");
			confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
			confirm.showAndWait();
		}
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
		Converter.setOperation(diagnosisOpId, "diagnosisController");
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
		diagnosisIcdCode.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue == null){
				Platform.runLater(()-> diagnosisIcdCode.setValue(oldValue));
			}
		});
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
		diagnosisType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue == null){
				Platform.runLater(()-> diagnosisType.setValue(oldValue));
			}
		});


	}

	/**
	 * Gets triggered when a Diagnosis in the TableView gets selected
	 */
	@FXML
	void diagnosisClicked(MouseEvent event) {
		flagEditDiagnose = true;
		if(event.getClickCount() > 0 && !diagnosisTable.getItems().isEmpty() && diagnosisTable.getSelectionModel().getSelectedItem() != null){
			Diagnose diagnose = diagnosisTable.getSelectionModel().getSelectedItem();
			Icd10CodeSt icd10CodeSt = new Icd10CodeStDao(Main.configuration).fetchOneByIcd10Code(diagnose.getIcd10Code());
			Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(diagnose.getOpId());
			DiagnosetypSt diagnosetypSt = new DiagnosetypStDao(Main.configuration).fetchOneByDiagnosetyp(diagnose.getDiagnosetyp());
			Icd10CodeSt icd10CodeSt1 = new Icd10CodeSt(icd10CodeSt){
				@Override
				public String toString(){
					return icd10CodeSt.getIcd10Code() + " " +
							icd10CodeSt.getBeschreibung();
				}
			};
			if(operation != null){
				Operation operation1 = new Operation(operation){
					@Override
					public String toString(){
						return operation.getOpId().toString();
					}
				};
				diagnosisOpId.setValue(operation1);
			}
			//can not be null
			DiagnosetypSt diagnosetypSt1 = new DiagnosetypSt(diagnosetypSt){
				@Override
				public String toString(){
					return diagnosetypSt.getBeschreibung();
				}
			};
			diagnosisIcdCode.setValue(icd10CodeSt1);
			diagnosisType.setValue(diagnosetypSt1);
			diagnosisFreetext.setText(diagnose.getKlartextDiagnose());
			dateDiagnosis.setDateTimeValue(diagnose.getDatum());
		}
	}

	/**
	 * Checks if all the necessary Values for the Diagnosis are selected
	 * @return Boolean if no Statement is missing
	 */
	public boolean noMissingStatement(){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");

		if(diagnosisOpId.getValue() == null){
			Main.logger.warning("Fehlende OP: Bitte wählen Sie eine OP aus.");
			alert.setHeaderText("Fehlender Op");
			alert.setContentText("Bitte wählen Sie eine OP aus.");
			alert.show();
			return false;
		}
		
		if(diagnosisIcdCode.getValue() == null){
			Main.logger.warning("Fehlender Diagnose-Code: Bitte wählen Sie einen Diagnose-Code aus.");
			alert.setHeaderText("Fehlender Diagnose-Code ");
			alert.setContentText("Bitte wählen Sie einen Diagnose-Code aus.");
			alert.showAndWait();
			return false;
		}

		if(diagnosisIcdCode.getSelectionModel().getSelectedItem().getIcd10Code().endsWith("-")){
			Main.logger.warning("Falscher Diagnose-Code: Bitte wählen Sie einen endständigen Diagnose-Code aus.");
			alert.setHeaderText("Fehlender Diagnose-Code");
			alert.setContentText("Bitte wählen Sie einen endständigen Diagnose-Code aus.");
			alert.showAndWait();
			return false;
		}

		if(diagnosisType.getValue() == null){
			Main.logger.warning("Fehlender Diagnosetyp: Bitte wählen Sie einen Diagnosetyp aus.");
			alert.setHeaderText("Fehlender Diagnosetyp");
			alert.setContentText("Bitte wählen Sie einen Diagnosetyp aus");
			alert.showAndWait();
			return false;
		}
		if(diagnosisTable.getSelectionModel().isEmpty() && flagEditDiagnose){
			Main.logger.warning("Fehlende Diagnose: Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus.");
			alert.setHeaderText("Fehlende Diagnose");
			alert.setContentText("Bitte wählen Sie die zu bearbeitende Diagnose in der Tabelle aus");
			alert.showAndWait();
			return false;
		}
		if(diagnosisFreetext.getText() != null && diagnosisFreetext.getText().matches(Main.blockedCharsForHL7)){
			Main.logger.warning("Falscher Eintrag: Die Sonderzeichen sind für HL7 blockiert.");
			alert.setHeaderText("Falscher Eintrag");
			alert.setContentText("Es dürfen keine Sonderzeichen verwendet werden (&,^,\\,~)!");
			alert.show();
			return false;
		}
		if(diagnosisFreetext.getText() != null && diagnosisFreetext.getText().length() > 200){
			Main.logger.warning("Falscher Eintrag: Die Länge des Freitextes ist zu lang.");
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setHeaderText("Falscher Eintrag");
			alert.setContentText("Der Eintrag des Freitextes ist zu lang");
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
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		if(code==null) {
			Main.logger.info("Für weitere Informationen muss ein ICD-10 Code ausgewählt werden.");
			alert.setHeaderText("Kein Code ausgewählt.");
			alert.setContentText("Für weitere Informationen muss ein ICD-10 Code ausgewählt werden.");
			alert.showAndWait();
			return;
		}
		try {
			JSONObject result = searchForResult(code);
			if(result==null) {
				Main.logger.info("Es konnte keine Information zu Ihrer Anfrage gefunden werden.");
				alert.setHeaderText("Kein Ergebnis gefunden");
				alert.setContentText("Es konnte keine Information zu Ihrer Anfrage gefunden werden!");
				alert.showAndWait();
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

	/**
	 * Finds the JSON object for the given icd-10 code
	 * @param code ICD-10 Code to be found
	 * @return JSON Object
	 * @throws Exception
	 */
	private JSONObject getJsonForCode(String code) throws Exception {
		URL url = new URL("https://fhir.imi.uni-luebeck.de/fhir/ConceptMap/$translate?url=http://imi.uni-luebeck.de/ehealth/fhir/ConceptMap/icd-10-to-msh&code="+code+"&system=http://fhir.de/CodeSystem/bfarm/icd-10-gm");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("accept", "application/json");
		InputStream responseStream = connection.getInputStream();
		JSONParser jsonParser = new JSONParser();
		return (JSONObject)jsonParser.parse(
				new InputStreamReader(responseStream, "UTF-8"));
	}

	/**
	 * Searches for an result for the given ICD-10 code. To achieve this, various terminal codes are compared with the JSON file.
	 * @param code Selected ICD-10 Code
	 * @return JSON Object of the ICD-10 code, null if nothing was found
	 * @throws Exception
	 */
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

	/**
	 * Checks if the JSON file was found and has a content
	 * @param json The JSON file to be checked
	 * @return True if found, false if not
	 */
	private boolean wasFound(JSONObject json) {
		JSONArray array = (JSONArray) json.get("parameter");
		for (Object o : array) {
			JSONObject item = (JSONObject) o;
			if (item.get("name").equals("result") && (Boolean) item.get("valueBoolean")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds the JSON entry with the Name name and returns the item
	 * @param array With JSON Items
	 * @param name Name of the item
	 * @return The item if found else null
	 */
	private JSONObject findJsonByName(JSONArray array, String name) {
		if (array==null) {return null;}
		for (Object o : array) {
			JSONObject item = (JSONObject) o;
			if (item.get("name").equals(name)) {
				return item;
			}
		}
		return null;
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
