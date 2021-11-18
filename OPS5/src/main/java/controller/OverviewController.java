package controller;
import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


import java.time.LocalDateTime;


import jooq.tables.daos.FallDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Operation;
import main.Main;


import java.util.List;



public class OverviewController {
	
    @FXML
    private CheckBox opListFilterPostOp; 
    @FXML
    private TableView<?> opListPatients;
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
    private TableColumn<Fall, Byte> storniertCol;

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
    private TableColumn<Operation, Byte> storniertOPCol;

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
    private Button btnStornieren;

    @FXML
    private Button btnDiag;
    @FXML
    private Button btnProc;



    @FXML
	public void initialize() {
        System.out.println("Initialize OPlist-Tab!");

        initializeColumns();
        opListCase.setItems(fallView());
        opListCase.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                int id = onEdit();
                opListOperation.setItems(operationView(id));
            }
        });
    }

    private void initializeColumns() {
        // tabellencols werden erstellt
        // create columns

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

    public int onEdit() {
        int id = 0;
            // check the table's selected item and get selected item
            if (opListCase.getSelectionModel().getSelectedItem() != null) {
                Fall selectedFall = opListCase.getSelectionModel().getSelectedItem();
                id = selectedFall.getFallId();
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

    public static ObservableList<Fall> fallView(){
        FallDao fallDao = new FallDao(Main.configuration);
        List<Fall> fall = fallDao.findAll();
        return FXCollections.observableArrayList(fall);

    }

    public static ObservableList<Operation> operationView(Integer id){
        OperationDao operationDao = new OperationDao(Main.configuration);
        List<Operation> operation = operationDao.fetchByFallId(id);
        return FXCollections.observableArrayList(operation);
    }

}
