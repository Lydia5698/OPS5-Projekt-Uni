package controller;

import com.sun.javafx.geom.AreaOp;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Patient;
import main.Main;
import org.jooq.*;
import org.jooq.exception.DataAccessException;

import java.sql.Timestamp;
import java.util.List;

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
    private ToggleGroup blutgruppe;
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

    /**
     * After pressing the button, the entries from the text fields are transferred to the attribute values and the
     * patient is inserted into the database with the help of the DAO
     * @param event event which is fired when the button is pushed
     */
    @FXML
    void createPatient(ActionEvent event) {
        insertPatient();
        clearFields();
	    System.out.println("Creating patient!");

	}

	private void insertPatient(){
        try{
            PatientDao patientDao = new PatientDao(Main.configuration);
            Patient patient = new Patient();
            patient.setName(patientLastname.getText());
            patient.setVorname(patientFirstname.getText());
            patient.setGeburtsdatum(patientBirthdate.getValue());
            patient.setBlutgruppe(blutgruppe.getSelectedToggle().toString());
            patient.setGeschlecht(sexGroup.getSelectedToggle().toString());
            patient.setErstellZeit(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            patient.setStrasse(patientStreet.getText());
            patient.setPostleitzahl(patientPostcode.toString());
            patient.setGeburtsort(patientBirthplace.toString());
            patient.setTelefonnummer(patientCellphone.toString());

            patientDao.insert(patient);
        }
        catch(DataAccessException e){
            e.printStackTrace();
        }
    }

    private void clearFields(){
        patientFirstname.clear();
        patientLastname.clear();
        patientBirthdate.setValue(null);
        patientBirthplace.clear();
        patientStreet.clear();
        patientPostcode.clear();
        patientCellphone.clear();
    }
}

