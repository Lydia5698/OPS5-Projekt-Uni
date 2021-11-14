package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import jooq.tables.Patient;
import jooq.tables.daos.PatientDao;
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
    private RadioButton zerominus;
    @FXML
    private RadioButton zeroplus;
    @FXML
    private RadioButton aminus;
    @FXML
    private RadioButton aplus;
    @FXML
    private RadioButton bminus;
    @FXML
    private RadioButton bplus;
    @FXML
    private RadioButton abminus;
    @FXML
    private RadioButton abplus;




    @FXML
	public void initialize() {
    	System.out.println("Initialize Patient-Tab!");
	}
	
    @FXML
    void createPatient(ActionEvent event) {
//	    Main.dslContext.insertInto(PATIENT, PATIENT.NAME, PATIENT.VORNAME, PATIENT.GEBURTSDATUM, PATIENT.BLUTGRUPPE,
 //               PATIENT.GESCHLECHT, PATIENT.GEBURTSORT, PATIENT.STRASSE, PATIENT.POSTLEITZAHL, PATIENT.TELEFONNUMMER).values(
//                        getPatientLastname().getText(), getPatientFirstname().getText(), getPatientBirthdate().getValue().atTime(), getPatientBirthplace().getText(), getPatientStreet().getText(),
//                getPatientPostcode().getText(), getPatientCellphone().getText()).execute();
//	    System.out.println(getSexGroup().getSelectedToggle());
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

