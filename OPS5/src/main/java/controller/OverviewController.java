package controller;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jooq.Tables;
import jooq.tables.Fall;
import jooq.tables.Operation;
import jooq.tables.Patient;
import main.Main;
import org.jooq.*;
import org.jooq.Record;

import static java.time.LocalTime.now;

public class OverviewController {
	
    @FXML
    private CheckBox opListFilterPostOp; 
    @FXML
    private TableView<model.Patient> opListPatients;
    @FXML
    private TableView<?> opListOpTable;
    @FXML
    private Button btnDiag;
    @FXML
    private Button btnProc;

    @FXML
	public void initialize() {
        System.out.println("Initialize OPlist-Tab!");
        createPatientList();
    }
    private void createPatientList(){
        opListPatients.getColumns().get(0).setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        opListPatients.getColumns().get(1).setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );
        opListPatients.getColumns().get(2).setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );
        opListPatients.getColumns().get(3).setCellValueFactory(
                new PropertyValueFactory<>("birthdate")
        );


        Result<Record> patients = Main.dslContext.selectDistinct(Patient.PATIENT.asterisk())
                .from(Patient.PATIENT)
                .leftJoin(Fall.FALL).onKey()
                .leftJoin(Operation.OPERATION).onKey()
                .fetch();
        patients.forEach(patient -> {
            Integer id = patient.getValue(Patient.PATIENT.PAT_ID);
            String firstName = patient.getValue(Patient.PATIENT.VORNAME);
            String lastName = patient.getValue(Patient.PATIENT.NAME);
            LocalDate birthdate = patient.getValue(Patient.PATIENT.GEBURTSDATUM);
            opListPatients.getItems().add(new model.Patient(id, firstName, lastName, birthdate));
        });
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
