package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import main.Main;
import org.jooq.*;
import static jooq.Tables.*;


public class PatientController{

    @FXML
    private TextField patientFirstname;
    @FXML
    private TextField patientLastname;
    @FXML
    private DatePicker patientBirthdate;
    @FXML
    private TextField patientBirthplace;
    @FXML
    private TextField patientStreet;
    @FXML
    private TextField patientPostcode;
    @FXML
    private TextField patientCellphone;
    @FXML
    private ToggleGroup sexGroup;
    @FXML
    private RadioButton patientFemale;
    @FXML
    private RadioButton patientMale;
    @FXML
    private RadioButton patientDiv;
       
	@FXML
	public void initialize() {
    	System.out.println("Initialize Patient-Tab!");
	}
	
    @FXML
    void createPatient(ActionEvent event) {
            Result<Record2<String, String>> result = Main.dslContext.select(PATIENT.NAME, PATIENT.VORNAME).from(PATIENT).fetch();
            for(Record r: result){
                String name = r.get(PATIENT.NAME);
                String vorname = r. get(PATIENT.VORNAME);
                System.out.println(name + ", " + vorname);
            }
            System.out.println("Creating patient!");
	}


    public TextField getPatientFirstname() {
        return patientFirstname;
    }

    public TextField getPatientLastname() {
        return patientLastname;
    }

    public DatePicker getPatientBirthdate() {
        return patientBirthdate;
    }

    public TextField getPatientBirthplace() {
        return patientBirthplace;
    }

    public TextField getPatientStreet() {
        return patientStreet;
    }

    public TextField getPatientPostcode() {
        return patientPostcode;
    }

    public TextField getPatientCellphone() {
        return patientCellphone;
    }

    public ToggleGroup getSexGroup() {
        return sexGroup;
    }

    public RadioButton getPatientFemale() {
        return patientFemale;
    }

    public RadioButton getPatientMale() {
        return patientMale;
    }

    public RadioButton getPatientDiv() {
        return patientDiv;
    }
}

