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
import java.time.LocalDate;


/**
 * The controller inserts emergency patients
 */
public class PatientController {

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
        Main.logger.info("Initialize Patient-Tab!");
	}

    /**
     * After pressing the button, the entries from the text fields are transferred to the attribute values and the
     * patient is inserted into the database with the help of the DAO and the window is closed afterwards
     *
     * @param event Event which is fired when the button is pushed
     */
    @FXML
    void createPatient(ActionEvent event) {
        try {
            PatientDao patientDao = new PatientDao(Main.configuration);
            Patient patient = new Patient();
            patient.setName(patientLastname.getText());
            patient.setVorname(patientFirstname.getText());
            //if the value of the datepicker cant be converted to localdate, it sets automatically null
            patient.setGeburtsdatum(patientBirthdate.getValue());
            patient.setBlutgruppe(getBlutgruppe());
            patient.setGeschlecht(getGeschlecht());
            patient.setErstellZeit(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            if (!patientStreet.getText().equals("")) {
                patient.setStrasse(patientStreet.getText());
            }
            if (!patientPostcode.getText().equals("")) {
                patient.setPostleitzahl(patientPostcode.getText());
            }
            if (!patientBirthplace.getText().equals("")) {
                patient.setGeburtsort(patientBirthplace.getText());
            }
            if (!patientCellphone.getText().equals("")) {
                patient.setTelefonnummer(patientCellphone.getText());
            }
            if (MainController.getUserId() != null) {
                patient.setErsteller(MainController.getUserId());
            }
            patient.setStorniert(false);

            //checking for values which can not be null (in this case it is the patients first and lastname)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if (patient.getVorname().trim().equals("")) {
                Main.logger.warning("Fehlende Einträge: Der Vorname des Patienten muss eingefügt werden.");
                alert.setHeaderText("Fehlende Einträge!");
                alert.setContentText("Der Vorname des Patienten muss eingefügt werden!");
                alert.showAndWait();
            } 
            else if (patient.getName().trim().equals("")) {
                Main.logger.warning("Fehlende Einträge: Der Nachname des Patienten muss eingefügt werden.");
                alert.setHeaderText("Fehlende Einträge!");
                alert.setContentText("Der Nachname des Patienten muss eingefügt werden!");
                alert.showAndWait();
            }
            //checking if special char are used which are reserved for the hl7 message like &,^,\,~
            else if (usingReservedChars(patient)) {
                Main.logger.warning("Falscher Eintrag: Die Sonderzeichen sind für HL7 blockiert.");
                alert.setHeaderText("Falsche Einträge!");
                alert.setContentText("Es dürfen keine Sonderzeichen verwendet werden (&,^,\\,~)!");
                alert.showAndWait();
            } //invalid birthdate
            else if(patientBirthdate.getValue() != null && patientBirthdate.getValue().isAfter(LocalDate.now())){
                    Main.logger.warning("Falscher Eintrag: Das gewählte Geburtsdatum liegt in der Zukunft.");
                    alert.setHeaderText("Falscher Eintrag!");
                    alert.setContentText("Das gewählte Geburtsdatum liegt in der Zukunft!");
                    alert.showAndWait();
            } 
            else {
                patientDao.insert(patient);
                Main.logger.info("Der Patient wurde in die Datenbank eingefügt.");

                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Information");
                confirm.setHeaderText("Erfolgreich eingefügt");
                confirm.setContentText("Der Patient wurde in die Datenbank eingefügt.");
                confirm.showAndWait();

                //close the window
                Stage stage = (Stage) speichern.getScene().getWindow();
                stage.close();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        MainController.getInstance().getAdmissionController().setPatient();
    }


    /**
     * This method converts the selected toggle into a string
     *
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
     * This method converts the selected toggle into a sex
     *
     * @return String of the selected sex
     */
    private String getGeschlecht() {
        if (sex_group.getSelectedToggle() == null) {
            return null;
        }
        String sG = ((RadioButton) sex_group.getSelectedToggle()).getText();
        if (sG.equals("weiblich")) {
            return "w";
        } else if (sG.equals("männlich")) {
            return "m";
        } else {
            return "d";
        }
    }

    /**
     * Checks if a blocked char is used in a testfield
     * @return true if a reserved char is used
     */
    private boolean usingReservedChars(Patient patient) {
        if (patient.getName().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        if (patient.getVorname().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        if (patient.getGeburtsort() != null && patient.getGeburtsort().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        if (patient.getStrasse() != null && patient.getStrasse().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        if (patient.getPostleitzahl() != null && patient.getPostleitzahl().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        if (patient.getTelefonnummer() != null && patient.getTelefonnummer().matches(Main.blockedCharsForHL7)) {
            return true;
        }
        return false;
    }

}
