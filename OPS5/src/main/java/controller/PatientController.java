package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.stage.Stage;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Patient;
import main.Main;
import org.jooq.exception.DataAccessException;

import java.sql.Timestamp;


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
    private ToggleGroup sex_group;
    @FXML
    private RadioButton weiblich;
    @FXML
    private RadioButton männlich;
    @FXML
    private RadioButton divers;
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
    private Button speichern;




    @FXML
	public void initialize() {
    	System.out.println("Initialize Patient-Tab!");
	}

    /**
     * After pressing the button, the entries from the text fields are transferred to the attribute values and the
     * patient is inserted into the database with the help of the DAO and all fields are cleared after that
     * @param event event which is fired when the button is pushed
     */
    @FXML
    void createPatient(ActionEvent event) {
        try {
            PatientDao patientDao = new PatientDao(Main.configuration);
            Patient patient = new Patient();
            patient.setName(patientLastname.getText());
            patient.setVorname(patientFirstname.getText());
            patient.setGeburtsdatum(patientBirthdate.getValue());
            patient.setBlutgruppe(getBlutgruppe());
            //if(sex_group.getSelectedToggle() != null){patient.setGeschlecht(getGeschlecht());}
            patient.setGeschlecht(getGeschlecht());
            patient.setErstellZeit(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            if(!patientStreet.getText().equals("")){patient.setStrasse(patientStreet.getText());}
            if(!patientPostcode.getText().equals("")){patient.setPostleitzahl(patientPostcode.getText());}
            if(!patientBirthplace.getText().equals("")){patient.setGeburtsort(patientBirthplace.getText());}
            if(!patientCellphone.getText().equals("")){patient.setTelefonnummer(patientCellphone.getText());}
            if(MainController.getUserId() != null){patient.setErsteller(MainController.getUserId());}
            patient.setStorniert(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlende Einträge!");
            if (patient.getVorname().equals("")) {
                alert.setContentText("Der Vorname des Patienten muss eingefügt werden!");
                alert.showAndWait();
            } else if (patient.getName().equals("")) {
                alert.setContentText("Der Nachname des Patienten muss eingefügt werden!");
                alert.showAndWait();
            }
            else {
                patientDao.insert(patient);
                System.out.println("Creating patient!");
                Stage stage = (Stage) speichern.getScene().getWindow();
                stage.close();
            }
        }
        catch(DataAccessException e){
            e.printStackTrace();
        }
	}


    /**
     * this method converts the selected toggle into a string
     * @return String of the selected blutgruppe
     */
    private String getBlutgruppe() {
        if (blutgruppe.getSelectedToggle() == null) {
            return "nb.";
        } else {
            return ((RadioButton) blutgruppe.getSelectedToggle()).getText();
        }
    }

    /**
     * this method converts the selected toggle into a sex
     * @return String of the selected sex
     */
    private String getGeschlecht(){
        if(sex_group.getSelectedToggle() == null){return null;}
        String sG = ((RadioButton)sex_group.getSelectedToggle()).getText();
        if(sG.equals("weiblich")){return "w";}
        else if(sG.equals("männlich")){return "m";}
        else{return "d";}
    }
}

