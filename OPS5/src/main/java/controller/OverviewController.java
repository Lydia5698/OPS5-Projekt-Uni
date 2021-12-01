package controller;
import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


import java.time.LocalDate;
import java.time.LocalDateTime;



import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import javafx.util.Callback;
import jooq.Keys;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.jooq.Result;



import java.util.List;



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
    private TableColumn<Fall, LocalDateTime> aufnahmeCol;

    @FXML
    private TableColumn<Fall, LocalDateTime> entlassungCol;

    @FXML
    private TableColumn<Fall, LocalDateTime> erstellzeitCol;

    @FXML
    private TableColumn<Fall, LocalDateTime> bearbeiterzeitCol;

    @FXML
    private TableColumn<Fall, Boolean> storniertCol;

    @FXML
    private TableColumn<Fall, Integer> patientenIDCol;

    @FXML
    private TableColumn<Fall,String> stationCol;

    @FXML
    private TableColumn<Fall, String> erstellerCol;

    @FXML
    private TableColumn<Fall, String> bearbeiterCol;

    @FXML
    private TableColumn<Fall, Integer> fallTypCol;

    @FXML
    private TableView<Operation> opListOperation;

    @FXML
    private TableColumn<Operation, Integer> opIDCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> beginnCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> endeCol;

    @FXML
    private TableColumn<Operation, Integer> bauchtuecherPraeCol;

    @FXML
    private TableColumn<Operation, Integer> bauchtuecherPostCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> schnittzeitCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> nahtzeitCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> erstellzeitOPCol;

    @FXML
    private TableColumn<Operation, LocalDateTime> bearbeiterzeitOPCol;

    @FXML
    private TableColumn<Operation, Boolean> storniertOPCol;

    @FXML
    private TableColumn<Operation, Integer> fallIdOPCol;

    @FXML
    private TableColumn<Operation, Integer> opSaalCol;

    @FXML
    private TableColumn<Operation, Integer> narkoseCol;

    @FXML
    private TableColumn<Operation, Integer> opTypCol;

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
    private TableColumn<Patient, LocalDateTime> paBearbeiterzeit;

    @FXML
    private TableColumn<Patient, String> paErsteller;

    @FXML
    private TableColumn<Patient, LocalDateTime> paErstellzeit;

    @FXML
    private TableColumn<Patient, Boolean> paStorniert;

    @FXML
    private TableColumn<Patient, String> paGeburtsort;

    @FXML
    private TableColumn<Patient, String> paStrasse;

    @FXML
    private TableColumn<Patient, String> paPostleitzahl;

    @FXML
    private TableColumn<Patient, String> paTelefonnummer;


    @FXML
    private Button btnStornieren;

    @FXML
    private Button btnDiag;

    @FXML
    private Button btnProc;

    @FXML
    private CheckBox stornierteOperation;

    @FXML
	public void initialize() {
        System.out.println("Initialize OPlist-Tab!");

        initializeColumns();
        opListPatients.setItems(patientView());
        opListPatients.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                int patientId = onEditPatient();
                opListCase.setItems(fallView(patientId));
            }
        });
        // TODO: 23.11.21 medPersonal(); nur die Namen rausfiltern und in die Tabelle einfügen

        //opListCase.setItems(fallView());
        // TODO: 23.11.21 medPersonal(); nur die Namen rausfiltern und in die Tabelle einfügen join?
        // TODO: 26.11.21 stornierte Ops rausfiltern
        // TODO: 26.11.21 diagnose update
        opListCase.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                int CaseId = onEditCase();
                opListOperation.setItems(operationView(CaseId));
            }
        });

        opListOperation.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                int opId = onEditOperation();
                createAndShowOperationWindow();
                //
            }
        });

    }

    private void initializeColumns() {
        // tabellencols werden erstellt
        // create columns

        // columns Patient
        paId.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getPatId()));
        paFirstname.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getVorname()));
        paLastname.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getName()));
        paBirthdate.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeburtsdatum()));
        paBlutgruppe.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBlutgruppe()));
        paGeschlecht.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeschlecht()));
        paBearbeiter.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiter()));
        paBearbeiterzeit.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
        paErsteller.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErsteller()));
        paErstellzeit.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
        paStorniert.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
        paGeburtsort.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeburtsort()));
        paStrasse.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStrasse()));
        paGeburtsort.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getGeburtsort()));
        paPostleitzahl.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getPostleitzahl()));
        paTelefonnummer.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getTelefonnummer()));



        // columns Case
        fallIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getFallId()));
        aufnahmeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getAufnahmedatum()));
        entlassungCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getEntlassungsdatum()));
        erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
        bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
        storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
        patientenIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getPatId()));
        stationCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStationSt()));
        erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErsteller()));
        bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiter()));
        fallTypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getFallTyp()));

        // columns Operation
        opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
        beginnCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBeginn()));
        endeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getEnde()));
        bauchtuecherPraeCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBauchtuecherPrae()));
        bauchtuecherPostCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBauchtuecherPost()));
        schnittzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getSchnittzeit()));
        nahtzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getNahtzeit()));
        erstellzeitOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
        bearbeiterzeitOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
        storniertOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
        fallIdOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getFallId()));
        opSaalCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpSaal()));
        narkoseCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getNarkoseSt()));
        opTypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpTypSt()));
        erstellerOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErsteller()));
        bearbeiterOPCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiter()));

    }

    public int onEditPatient() {
        int id = 0;
        // check the table's selected item and get selected item
        if (opListPatients.getSelectionModel().getSelectedItem() != null) {
            Patient selected = opListPatients.getSelectionModel().getSelectedItem();
            id = selected.getPatId();
        }
        return id;

    }

    public int onEditCase() {
        int id = 0;
            // check the table's selected item and get selected item
            if (opListCase.getSelectionModel().getSelectedItem() != null) {
                Fall selectedFall = opListCase.getSelectionModel().getSelectedItem();
                id = selectedFall.getFallId();
            }
            return id;

    }

    public int onEditOperation() {
        int id = 0;
        // check the table's selected item and get selected item
        if (opListOperation.getSelectionModel().getSelectedItem() != null) {
            Operation selectedOperation = opListOperation.getSelectionModel().getSelectedItem();
            id = selectedOperation.getOpId();
        }
        return id;
    }

    @FXML
   	public void createAndShowDiagnosisWindow() {
       	System.out.println("New Patient Window!");
       	try {
       		FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("/fxml/PaneDiagnosis.fxml"));
               Parent root = fxmlLoader.load();
               Stage stage = new Stage();
               stage.setTitle("Diagnosen");
               stage.setScene(new Scene(root));
               stage.show();
       	}catch (IOException e) {
       		e.printStackTrace();
       	}
       	
    }

    @FXML
    public void createAndShowOperationWindow() {
        System.out.println("New Patient Window!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneOp.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Operation Bearbeiten");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @FXML
   	public void createAndShowProcedureWindow() {
       	System.out.println("New Patient Window!");
       	try {
       		FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("/fxml/PaneProcedure.fxml"));
               Parent root = fxmlLoader.load();
               Stage stage = new Stage();
               stage.setTitle("Prozeduren");
               stage.setScene(new Scene(root));
               stage.show();
       	}catch (IOException e) {
       		e.printStackTrace();
       	}
       	
    }

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

    public static ObservableList<Fall> fallView(int patientId){
        FallDao fallDao = new FallDao(Main.configuration);
        List<Fall> fall = fallDao.fetchByPatId(patientId);

        //Keys.FK_FALL_MED_PERSONAL1.
        return FXCollections.observableArrayList(fall);
    }

    public static ObservableList<MedPersonal> medPersonal(){
        MedPersonalDao medPersonalDao = new MedPersonalDao(Main.configuration);
        List<MedPersonal> medPersonalList = medPersonalDao.findAll();

        return FXCollections.observableArrayList(medPersonalList);
    }


    public static ObservableList<Operation> operationView(Integer id){
        OperationDao operationDao = new OperationDao(Main.configuration);
        List<Operation> operation = operationDao.fetchByFallId(id);
        return FXCollections.observableArrayList(operation);

    }

    @FXML
    void storniereOP(ActionEvent event) {
        opListOperation.getSelectionModel().getSelectedItem().setStorniert(true); // TODO: 25.11.21 stornierte Ops nicht anzeigen
        Operation operation = opListOperation.getSelectionModel().getSelectedItem();
        OperationDao operationDao = new OperationDao(Main.configuration);
        operationDao.update(operation);
    }

    @FXML
    void showStornierteOp(MouseEvent event) {
        if(stornierteOperation.isSelected()) {
            OperationDao operationDao = new OperationDao(Main.configuration);
            List<Operation> operation = operationDao.fetchByStorniert(true);
            opListOperation.setItems(FXCollections.observableArrayList(operation));
        }
        else{
            int CaseId = onEditCase();
            opListOperation.setItems(operationView(CaseId));
        }
    }

   /* private void setCaseFallTyp(){
        Callback<ListView<FallTypSt>, ListCell<jooq.tables.FallTypSt>> cellFactory = new Callback<>() {
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
*/
    /*private void insertNewOperation(int opID) {
        LocalDateTime beginn;
        LocalDateTime ende;
        Integer       bauchtuecherPrae;
        Integer       bauchtuecherPost;
        LocalDateTime schnittzeit;
        LocalDateTime nahtzeit;
        LocalDateTime erstellZeit;
        LocalDateTime bearbeiterZeit;
        Byte          storniert;
        Integer       fallId;
        Integer       opSaal;
        Integer       narkoseSt;
        Integer       opTypSt;
        String        ersteller;
        String        bearbeiter;

        Operation operation = new Operation(opID,beginn,ende,bauchtuecherPrae,bauchtuecherPost,schnittzeit,nahtzeit,
                erstellZeit,bearbeiterZeit,storniert,fallId,opSaal,narkoseSt,opTypSt,ersteller,bearbeiter);
        OperationDao operationDao = new OperationDao(Main.configuration);
        operationDao.insert(operation);
        // reicht bei update die opID? als schlüssel
    }*/

	/*
	 boolean insertEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(Employee employee);
	 */


}
