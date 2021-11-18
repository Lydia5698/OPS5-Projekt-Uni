package controller;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;


import java.time.LocalDateTime;


import jooq.Tables;
import jooq.tables.Fall;

import main.Main;
import controller.FallObject;


import org.jooq.impl.DSL;
import java.sql.*;

import org.jooq.*;
import org.jooq.impl.*;

import java.util.List;



public class OverviewController {
	
    @FXML
    private CheckBox opListFilterPostOp; 
    @FXML
    private TableView<?> opListPatients;
    @FXML
    private TableView<FallObject> opListCase;
    @FXML
    private TableView<OpObject> opListOperation;

    @FXML
    private Button btnStornieren;

    @FXML
    private Button btnDiag;
    @FXML
    private Button btnProc;

    private ObservableList<FallObject> fallData = FXCollections.observableArrayList();
    private ObservableList<OpObject> opData = FXCollections.observableArrayList();


    @FXML
	public void initialize() {
        System.out.println("Initialize OPlist-Tab!");

        // tabellencols werden erstellt
        // create columns
        TableColumn<FallObject, Integer> fallIDCol = new TableColumn<FallObject, Integer>("Fall-ID");
        fallIDCol.setCellValueFactory(new PropertyValueFactory<>("fallID"));

        TableColumn<FallObject, LocalDateTime> aufnahmeCol = new TableColumn<FallObject, LocalDateTime>("Aufnahmedatum");
        aufnahmeCol.setCellValueFactory(new PropertyValueFactory<>("aufnahmedatum"));

        TableColumn<FallObject, Integer> fallTypCol = new TableColumn<FallObject, Integer>("Stationär/Ambulant");
        fallTypCol.setCellValueFactory(new PropertyValueFactory<>("stationär"));

        // add columns
        opListCase.getColumns().addAll(fallIDCol, aufnahmeCol, fallTypCol);

        TableColumn<OpObject, Integer> opIDCol = new TableColumn<OpObject, Integer>("Op-ID");
        opIDCol.setCellValueFactory(new PropertyValueFactory<>("opID"));

        // add columns
        opListOperation.getColumns().addAll(opIDCol);


        // Alle Daten von der Tabelle Fall
        Result<Record> resultFall = Main.dslContext.select().from(Tables.FALL).fetch();

        for (Record r : resultFall) {
            Integer id = r.getValue(Tables.FALL.FALL_ID);
            LocalDateTime aufnahmedatum = r.getValue(Tables.FALL.AUFNAHMEDATUM);
            Integer stationär = r.getValue(Tables.FALL.FALL_TYP);
            System.out.println("ID: " + id + aufnahmedatum + stationär);
            fallData.add(new FallObject(id, aufnahmedatum, 1));  // TODO: 18.11.21  stationär mit Stammtabelle verbinden
        }
        opListCase.setItems(fallData);

        Result<Record> resultOp = Main.dslContext.select().from(Tables.OPERATION, Tables.FALL).where(Tables.OPERATION.FALL_ID.eq(Tables.FALL.FALL_ID)).fetch();

        for (Record r : resultOp) { // TODO: 18.11.21 nur ops zum Fall anzeigen
            Integer id = r.getValue(Tables.OPERATION.OP_ID);
            System.out.println("ID: " + id);
            opData.add(new OpObject(id));
        }

        opListCase.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                onEdit();
            }
        });
    }

        public void onEdit() {
            // check the table's selected item and get selected item
            if (opListCase.getSelectionModel().getSelectedItem() != null) {
                FallObject selectedFall = opListCase.getSelectionModel().getSelectedItem();
                System.out.println(selectedFall);
            }
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

}
