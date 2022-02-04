package controller;
import java.io.IOException;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.time.LocalDate;

import javafx.util.Callback;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.controlsfx.control.SearchableComboBox;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * This Controller displays all Patients that had an Operation and their corresponding Cases and Operations. You can
 * filter the Operations to only see the canceled Operations. And you can edit oder create new Diagnosis and Procedures
 */
public class OverviewController {

    @FXML
    private CheckBox opListFilterPostOp;

    @FXML
    private TableView<Patient> opListPatients;

    @FXML
    private TableView<Fall> opListCase;

    @FXML
    private TableColumn<Fall, Integer> fallIDCol;

    @FXML
    private TableColumn<Fall, String> aufnahmeCol;

    @FXML
    private TableColumn<Fall, String> entlassungCol;

    @FXML
    private TableColumn<Fall, String> erstellzeitCol;

    @FXML
    private TableColumn<Fall, String> bearbeiterzeitCol;

    @FXML
    private TableColumn<Fall, String> storniertCol;

    @FXML
    private TableColumn<Fall, String> patientenIDCol;

    @FXML
    private TableColumn<Fall,String> stationCol;

    @FXML
    private TableColumn<Fall, String> erstellerCol;

    @FXML
    private TableColumn<Fall, String> bearbeiterCol;

    @FXML
    private TableColumn<Fall, String> fallTypCol;

    @FXML
    private TableView<Operation> opListOperation;

    @FXML
    private TableColumn<Operation, Integer> opIDCol;

    @FXML
    private TableColumn<Operation, String> beginnCol;

    @FXML
    private TableColumn<Operation, String> endeCol;

    @FXML
    private TableColumn<Operation, Integer> bauchtuecherPraeCol;

    @FXML
    private TableColumn<Operation, Integer> bauchtuecherPostCol;

    @FXML
    private TableColumn<Operation, String> schnittzeitCol;

    @FXML
    private TableColumn<Operation, String> nahtzeitCol;

    @FXML
    private TableColumn<Operation, String> erstellzeitOPCol;

    @FXML
    private TableColumn<Operation, String> bearbeiterzeitOPCol;

    @FXML
    private TableColumn<Operation, String> storniertOPCol;

    @FXML
    private TableColumn<Operation, Integer> fallIdOPCol;

    @FXML
    private TableColumn<Operation, Integer> opSaalCol;

    @FXML
    private TableColumn<Operation, String> narkoseCol;

    @FXML
    private TableColumn<Operation, String> opTypCol;

    @FXML
    private TableColumn<Operation, String> erstellerOPCol;

    @FXML
    private TableColumn<Operation, String> bearbeiterOPCol;

    @FXML
    private TableColumn<Patient, Integer> paId;

    @FXML
    private TableColumn<Patient, String> paFirstname;

    @FXML
    private TableColumn<Patient, String> paLastname;

    @FXML
    private TableColumn<Patient, LocalDate> paBirthdate;

    @FXML
    private TableColumn<Patient, String> paBlutgruppe;

    @FXML
    private TableColumn<Patient, String> paGeschlecht;

    @FXML
    private TableColumn<Patient, String> paBearbeiter;

    @FXML
    private TableColumn<Patient, String> paBearbeiterzeit;

    @FXML
    private TableColumn<Patient, String> paErsteller;

    @FXML
    private TableColumn<Patient, String> paErstellzeit;

    @FXML
    private TableColumn<Patient, String> paStorniert;

    @FXML
    private TableColumn<Patient, String> paGeburtsort;

    @FXML
    private TableColumn<Patient, String> paStrasse;

    @FXML
    private TableColumn<Patient, String> paPostleitzahl;

    @FXML
    private TableColumn<Patient, String> paTelefonnummer;

    @FXML
    private TableColumn<Patient, String> panotfall;

    @FXML
    private TableColumn<Operation, String> geplantCol;

    @FXML
    private CheckBox stornierteOperation;

    @FXML
    private SearchableComboBox<StationSt> stations;

    @FXML
    private Button btnRole;

    private static Integer opId;

    private static Boolean storniertShown =  false;

    private Parent root;

    private AdmissionController admissionController;

    /**
     * This Methode initializes the TableViews for the Patients and their corresponding Cases and Operations
     */
    @FXML
	public void initialize() {
        btnRole.setVisible(false);
        initializeColumns();
        setStations();
        stations.setOnAction(e -> {showCasesOnStation();});
        opListPatients.setItems(patientView());
        // When a Patient gets selected the corresponding Cases show
        opListPatients.setOnMouseClicked((MouseEvent event) -> {
            stations.getSelectionModel().clearSelection();
            if (event.getClickCount() > 0) {
                int patientId = onEditPatient();
                opListCase.setItems(fallView(patientId));
                opListOperation.setItems(null);
                btnRole.setVisible(false);
            }

        });

        // When a Case gets selected the corresponding Operations show
        opListCase.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                int caseId = onEditCase();
                opListOperation.setItems(operationView(caseId));
                btnRole.setVisible(false);
            }
        });
        // When an Operation gets double clicked you can edit this Operation
        opListOperation.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1 && onEditOperation() != 0) {
                opId = onEditOperation();
                createAndShowOperationWindow();
                opListOperation.setItems(operationView(onEditCase()));

            }
            // when an operation gets clicked the button for changing the roles is shown
            if (event.getClickCount() > 0){
                btnRole.setVisible(true);
            }
        });

    }

    /**
     * Reloads the Patient Table after receiving a new Patient from the KIS
     */
    public void reload(){
        opListPatients.setItems(patientView());
    }

    /**
     * Initializes all Columns from the Table Views opListPatients, opListCase and opListOperation
     */
    private void initializeColumns() {
        // create columns

        // columns Patient
        paId.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getPatId()));
        paFirstname.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getVorname()));
        paLastname.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getName()));
        paBirthdate.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeburtsdatum()));
        paBlutgruppe.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBlutgruppe()));
        paGeschlecht.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.geschlechtConverter(features.getValue().getGeschlecht())));
        paBearbeiter.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
        paBearbeiterzeit.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
        paErsteller.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
        paErstellzeit.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
        paStorniert.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
        paGeburtsort.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeburtsort()));
        paStrasse.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStrasse()));
        paPostleitzahl.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getPostleitzahl()));
        paTelefonnummer.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getTelefonnummer()));
        panotfall.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getNotfall())));

        // columns Case
        fallIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getFallId()));
        aufnahmeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getAufnahmedatum(), true)));
        entlassungCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getEntlassungsdatum(), true)));
        erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
        bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
        storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
        patientenIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.patientConverter(features.getValue().getPatId())));
        stationCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStationSt()));
        erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
        bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
        fallTypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.fallTypConverter(features.getValue().getFallTyp())));

        // columns Operation
        opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
        beginnCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBeginn(), true)));
        endeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getEnde(), true)));
        bauchtuecherPraeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBauchtuecherPrae()));
        bauchtuecherPostCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBauchtuecherPost()));
        schnittzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getSchnittzeit(), true)));
        nahtzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getNahtzeit(), true)));
        erstellzeitOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
        bearbeiterzeitOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
        storniertOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
        fallIdOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getFallId()));
        opSaalCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpSaal()));
        narkoseCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.narkoseConverter(features.getValue().getNarkoseSt())));
        opTypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.opTypConverter(features.getValue().getOpTypSt())));
        erstellerOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
        bearbeiterOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
        geplantCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getGeplant())));

    }

    /**
     *  Gets the Patient ID from the selected Patient in the Table View
     * 	@return Patient ID
     */
    public int onEditPatient() {
        int id = 0;
        // check the table's selected item and get selected item
        if (opListPatients.getSelectionModel().getSelectedItem() != null) {
            Patient selected = opListPatients.getSelectionModel().getSelectedItem();
            id = selected.getPatId();
        }
        return id;

    }

    /**
     *  Gets the Case ID from the selected Case in the Table View
     *  @return Case ID
     */
    public int onEditCase() {
        int id = 0;
            // check the table's selected item and get selected item
            if (opListCase.getSelectionModel().getSelectedItem() != null) {
                Fall selectedFall = opListCase.getSelectionModel().getSelectedItem();
                id = selectedFall.getFallId();
            }
            return id;

    }

    /**
     * Gets the Operation ID from the selected Operation in the Table View
     * @return Operation ID
     */
    public int onEditOperation() {
        int id = 0;
        // check the table's selected item and get selected item
        if (opListOperation.getSelectionModel().getSelectedItem() != null) {
            Operation selectedOperation = opListOperation.getSelectionModel().getSelectedItem();
            id = selectedOperation.getOpId();
        }
        return id;
    }

    /**
     * Collects all Patients from the Database and saves them in a observable Array List from Type Patient pojo
     * @return All Patients
     */
    public static ObservableList<Patient> patientView(){
        //PatientDao patientDao = new PatientDao(Main.configuration);
        DSLContext context = DSL.using(Main.configuration);
        List<Patient> patients = context
                .selectDistinct(jooq.tables.Patient.PATIENT.asterisk())
                .from(jooq.tables.Patient.PATIENT)
                .leftJoin(jooq.tables.Fall.FALL).onKey()
                .leftSemiJoin(jooq.tables.Operation.OPERATION).onKey()
                .fetchInto(Patient.class);

        return FXCollections.observableArrayList(patients);
    }

    /**
     * Collects all Cases from the Database and saves them in a observable Array List from Type Fall pojo
     * @param patientId Patient ID from the selected Patient in the Table view
     * @return All cases by Patient ID
     */
    public static ObservableList<Fall> fallView(int patientId){
        FallDao fallDao = new FallDao(Main.configuration);
        List<Fall> fall = fallDao.fetchByPatId(patientId);
        return FXCollections.observableArrayList(fall);
    }

    /**
     * Collects the Medical staff from the Database and saves them in a observable Array List from Type MedPersonal pojo
     * @return The Medical staff
     */
    public static ObservableList<MedPersonal> medPersonal(){
        MedPersonalDao medPersonalDao = new MedPersonalDao(Main.configuration);
        List<MedPersonal> medPersonalList = medPersonalDao.findAll();
        return FXCollections.observableArrayList(medPersonalList);
    }


    /**
     * Collects all Operations from the Database and saves them in a observable Array List from Type Operation pojo
     * @param id Case ID from the selected Case in the Table view
     * @return All Operations by Case
     */
    public static ObservableList<Operation> operationView(Integer id){
        OperationDao operationDao = new OperationDao(Main.configuration);
        List<Operation> operation = operationDao.fetchByFallId(id);
        Predicate<Operation> byStorniert = operation1 -> !operation1.getStorniert();
        if(getStorniertShown()){
            byStorniert = Operation::getStorniert;
        }
        var result = operation.stream().filter(byStorniert)
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(result);

    }
    /**
     * Gets all the Stations and saves them in the stations Combobox
     */
    private void setStations() {
        Callback<ListView<StationSt>, ListCell<StationSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<StationSt> call(ListView<StationSt> medPersonalListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(StationSt stationSt, boolean empty) {
                        super.updateItem(stationSt, empty);
                        if (stationSt == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(stationSt.getStation() + " " + stationSt.getBezeichnung());
                        }
                    }
                };
            }
        };
        stations.setButtonCell(cellFactory.call(null));
        stations.setCellFactory(cellFactory);
        stations.getItems().setAll(new StationStDao(Main.configuration).findAll());
        stations.setSelectionModel(new CustomSelectionModel<>(stations));
    }

    /**
     * Shows all cases on the selected Station
     */
    public void showCasesOnStation() {
        if(stations.getValue() != null){
            List<Fall> fall = new FallDao(Main.configuration).fetchByStationSt(stations.getValue().getStation());
            opListCase.setItems(FXCollections.observableArrayList(fall));
            opListOperation.setItems(null);
        }
    }

    /**
     * Deletes an selected Operation (sets storniert true) and updates this Operation
     */
    @FXML
    void storniereOP() {
        if(opListOperation.getSelectionModel().getSelectedItem() != null){
            opListOperation.getSelectionModel().getSelectedItem().setStorniert(!opListOperation.getSelectionModel().getSelectedItem().getStorniert());
            Operation operation = opListOperation.getSelectionModel().getSelectedItem();
            OperationDao operationDao = new OperationDao(Main.configuration);
            DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
            ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
            List<Diagnose> diagnose = diagnoseDao.fetchByOpId(operation.getOpId());
            List<Prozedur> prozedur = prozedurDao.fetchByOpId(operation.getOpId());
            for (Diagnose value : diagnose) {
                value.setStorniert(opListOperation.getSelectionModel().getSelectedItem().getStorniert());
                diagnoseDao.update(value);
            }
            for (Prozedur value : prozedur){
                value.setStorniert(opListOperation.getSelectionModel().getSelectedItem().getStorniert());
                prozedurDao.update(value);
            }
            operationDao.update(operation);
            Main.logger.info("Die Operation und die dazugehörigen Diagnosen und Prozeduren wurden storniert.");
            opListOperation.setItems(operationView(onEditCase()));
        }
        else{
            Main.logger.warning("Fehlende Operation: Bitte wählen Sie eine Operation zum Stornieren aus.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlende Operation");
            alert.setContentText("Bitte wählen Sie eine Operation zum stornieren aus");
            alert.showAndWait();
        }
    }

    /**
     * Shows all deleted Operations in the Table View
     */
    @FXML
    void showStornierteOp() {
        int caseId = onEditCase();
        if(stornierteOperation.isSelected()) {
            setStorniertShown(true);
            opListOperation.setItems(FXCollections.observableArrayList(operationView(caseId)));
        }
        else{
            setStorniertShown(false);
            opListOperation.setItems(operationView(caseId));
        }
    }
    /**
     * Shows the Diagnosis Window where you can edit or create a new Diagnosis
     */
    @FXML
    public void createAndShowDiagnosisWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneDiagnosis.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Diagnosen");
            stage.setScene(new Scene(root));
            DiagnosisController controller = fxmlLoader.getController();
            controller.diagnoseView(onEditOperation());
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the Operation Window where you can edit a Operation
     */
    @FXML
    public void createAndShowOperationWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneOpEdit.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Operation Bearbeiten");
            stage.setScene(new Scene(root));
            AdmissionController controller = fxmlLoader.getController();
            controller.initializeCombobox(onEditOperation());
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the Procedure Window where you can edit or create a new Procedure
     */
    @FXML
    public void createAndShowProcedureWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneProcedure.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Prozeduren");
            stage.setScene(new Scene(root));
            ProcedureController controller = fxmlLoader.getController();
            controller.prozedurView(onEditOperation());
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Opens a new window where the roles are displayed. These can then be edited
     */
    @FXML
    void showRoles(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneRoleOverview.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Rolle");
            stage.setScene(new Scene(root));
            RoleOverviewController controller = fxmlLoader.getController();
            controller.roleView(onEditOperation());
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getOpId() {
        return opId;
    }

    public static Boolean getStorniertShown() {
        return storniertShown;
    }

    public void setStorniertShown(Boolean storniertShown) {
        OverviewController.storniertShown = storniertShown;
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
