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
    private TableView<?> opListStation;

    @FXML
    private Button btnStornieren;

    @FXML
    private Button btnDiag;
    @FXML
    private Button btnProc;

    private ObservableList<FallObject> fallData = FXCollections.observableArrayList();


    @FXML
	public void initialize() {
    	System.out.println("Initialize OPlist-Tab!");

    	// tabellencols werden erstellt
        TableColumn<FallObject, Integer> fallIDCol = new TableColumn<FallObject, Integer>("Fall-ID");
        fallIDCol.setCellValueFactory(new PropertyValueFactory<>("fallID"));

        TableColumn<FallObject, LocalDateTime> aufnahmeCol = new TableColumn<FallObject, LocalDateTime>("Aufnahmedatum");
        aufnahmeCol.setCellValueFactory(new PropertyValueFactory<>("aufnahmedatum"));

        TableColumn<FallObject, Integer> fallTypCol = new TableColumn<FallObject, Integer>("Stationär/Ambulant");
        fallTypCol.setCellValueFactory(new PropertyValueFactory<>("stationär"));

        opListCase.getColumns().addAll(fallIDCol,aufnahmeCol,fallTypCol);



        // Alle Daten von der Tabelle Fall
        Result<Record> resultFall = Main.dslContext.select().from(Tables.FALL).fetch();


        for (Record r : resultFall) {
            Integer id = r.getValue(Tables.FALL.FALL_ID);
            LocalDateTime aufnahmedatum = r.getValue(Tables.FALL.AUFNAHMEDATUM);
            Integer stationär = r.getValue(Tables.FALL.FALL_TYP);
            System.out.println("ID: " + id + aufnahmedatum + stationär);
            fallData.add(new FallObject(id, aufnahmedatum, 1));
        }
        opListCase.setItems(fallData);




        //opListCase.getItems().setAll(fallID_list);*/
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

    /*
    AuthorRecord author = context.fetchOne(Author.AUTHOR, Author.AUTHOR.ID.eq(1))


    Result<Record2<Integer, String>> articles = context.select(Article.ARTICLE.ID, Article.ARTICLE.TITLE)
    .from(Author.AUTHOR)
    .fetch();

    Result<Record<Integer, LocalDateTime, Integer>> fall = context.select(Fall.FALL_ID, Fall.AUFNAHMEDATUM, Fall.FALL_TYP)
    .from(

    Fälle darstellen mitte
    Fall Integer
    Aufnahmedatum LocalDateTime
    stationär Integer string dann aus FallTypSt
     */




}
