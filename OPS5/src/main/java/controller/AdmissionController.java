package controller;

import ExternalFiles.Converter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * The class AdmissionController is responsible for creating a new operation.
 * It also contains the buttons for opening windows to creating new roles and patients.
 */
public class AdmissionController {

    private Parent root;

    @FXML
    private SearchableComboBox<Patient> selectPatient;

    @FXML
    private OPController opController;

    private Integer opId = null;

    /**
     * This method selects all patients of the system as choosing options of the combobox for the selection of the patient.
     */
    public void setPatient() {
        Platform.runLater(()->{
            Converter.setPatient(selectPatient, false);
        });

    }

    /**
     * When the window for creating an operation is opened, this method is called.
     * The patients can be selected and the initial case id gets selected by the chosen patient, which can be null.
     */
    @FXML
    public void initialize() {
        selectPatient.setOnAction(e -> {
            if (selectPatient.getValue() != null) {
                opController.setCase(selectPatient.getValue().getPatId());
            }
        });
        setPatient();
    }

    /**
     * This method is called when a button is pushed.
     * Checks are run that all input is valid and all the non-nullable objects have a value.
     * A new operation is created and inserted into the database.
     */
    @FXML
    public void create() {
        if (opController.getOpCaseId() == null) {
            Main.logger.warning("Fehlende Einträge: Es muss ein Fall ausgewählt werden.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlende Einträge!");
            alert.setContentText("Es muss ein Fall ausgewählt werden!");
            alert.showAndWait();
        }//Op-Ende vor Op-Start
        else if (noFalseStatement()) {
            Operation operation = new Operation(
                    null, //opId -> automatisch mit AutoIncrement gesetzt
                    opController.getOpDateBegin(), //beginn
                    opController.getOpDateEnd(), //ende
                    opController.getTowelBefore(), //bauchtücherPrä -> hat immer einen Wert
                    opController.getTowelAfter(), //bauchtücherPost -> hat immer einen Wert
                    opController.getCutTime(), //schnittzeit
                    opController.getSewTime(), //nahtzeit
                    new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //ersteller Zeit
                    null, //bearbeiterZeit
                    false, //storniert
                    opController.getOpCaseId(), //fallId
                    opController.getOpRoomCode(), //opSaal
                    opController.getNarkose(), //narkoseSt
                    opController.getOpType(), //opTypSt
                    MainController.getUserId(), //ersteller
                    null, //bearbeiter
                    false //geplant
            );

            OperationDao operationDao = new OperationDao(Main.configuration);
            operationDao.insert(operation);

            //Bauchtücher nicht gleich
            if (opController.getTowelAfter() != opController.getTowelBefore()) {
                Main.logger.warning("Die Anzahl der Bauchtücher nach der OP stimmt nicht mit der Anzahl vor der Operation überein.");
                Alert alert1 = new Alert(AlertType.WARNING);
                alert1.setTitle("Achtung");
                alert1.setHeaderText("Bauchtücher");
                alert1.setContentText("Die Anzahl der Bauchtücher nach der OP stimmt nicht mit der Anzahl vor der Operation überein!");
                alert1.showAndWait();
            }

            Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
            Alert confirm = new Alert(AlertType.INFORMATION);
            confirm.setTitle("Information");
            confirm.setHeaderText("Erfolgreich eingefügt");
            confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
            confirm.showAndWait();
            clearFields();
            CommunicationsController.getInstance().setCommunicationsObjectBox();
            MainController.getInstance().getOverviewController().reload();
        }
    }

    /**
     * Checks the user input for the operation for incorrect input
     *
     * @return True if no false Statement
     */
    private Boolean noFalseStatement() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        if (opController.getOpDateEnd() != null && opController.getOpDateBegin() != null && opController.getOpDateEnd().isBefore(opController.getOpDateBegin())) {
            Main.logger.warning("Das OP-Ende kann nicht vor dem OP-Start sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Das Op-Ende kann nicht vor dem Op-Start sein!");
            alert.showAndWait();
            return false;
        }//Schnittzeit vor Op-Beginn
        else if (opController.getOpDateBegin() != null && opController.getCutTime() != null && opController.getOpDateBegin().isAfter(opController.getCutTime())) {
            Main.logger.warning("Die Schnittzeit kann nicht vor dem OP-Start sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Die Schnittzeit kann nicht vor dem Op-Start sein!");
            alert.showAndWait();
            return false;
        }//Schnittzeit nach Op-Ende
        else if (opController.getOpDateEnd() != null && opController.getCutTime() != null && opController.getOpDateEnd().isBefore(opController.getCutTime())) {
            Main.logger.warning("Die Schnittzeit kann nicht nach dem OP-Ende sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Die Schnittzeit kann nicht nach dem Op-Ende sein!");
            alert.showAndWait();
            return false;
        }//Nahtzeit vor Op-Beginn
        else if (opController.getOpDateBegin() != null && opController.getSewTime() != null && opController.getOpDateBegin().isAfter(opController.getSewTime())) {
            Main.logger.warning("Die Nahtzeit kann nicht vor dem OP-Start sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Die Nahtzeit kann nicht vor dem Op-Start sein!");
            alert.showAndWait();
            return false;
        }//Nahtzeit vor Schnittzeit
        else if (opController.getCutTime() != null && opController.getSewTime() != null && opController.getCutTime().isAfter(opController.getSewTime())) {
            Main.logger.warning("Die Nahtzeit kann nicht vor der Schnittzeit sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Die Nahtzeit kann nicht vor der Schnittzeit sein!");
            alert.showAndWait();
            return false;
        }//Nahtzeit nach Op-Ende
        else if (opController.getOpDateEnd() != null && opController.getSewTime() != null && opController.getOpDateEnd().isBefore(opController.getSewTime())) {
            Main.logger.warning("Die Nahtzeit kann nicht nach dem OP-Ende sein.");
            alert.setHeaderText("Falscher Eintrag!");
            alert.setContentText("Die Nahtzeit kann nicht nach dem Op-Ende sein!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Edits an existing Operation
     */
    public void editOperation() {

        if (noFalseStatement()) {
            Operation operation = new Operation(
                    opId, //opId -> the operation to be edited
                    opController.getOpDateBegin(), //beginn
                    opController.getOpDateEnd(), //ende
                    opController.getTowelBefore(), //bauchtücherPrä -> hat immer einen Wert
                    opController.getTowelAfter(), //bauchtücherPost -> hat immer einen Wert
                    opController.getCutTime(), //schnittzeit
                    opController.getSewTime(), //nahtzeit
                    null, //ersteller Zeit
                    LocalDateTime.now(), //bearbeiterZeit
                    false, //storniert
                    opController.getOpCaseId(), //fallId
                    opController.getOpRoomCode(), //opSaal
                    opController.getNarkose(), //narkoseSt
                    opController.getOpType(), //opTypSt
                    null, //ersteller
                    MainController.getUserId(),//bearbeiter
                    false //geplant
            );
            OperationDao operationDao = new OperationDao(Main.configuration);
            operationDao.update(operation);
            CommunicationsController.getInstance().setCommunicationsObjectBox();
            MainController.getInstance().getOverviewController().reload();

            Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
            Alert confirm = new Alert(AlertType.INFORMATION);
            confirm.setTitle("Information");
            confirm.setHeaderText("Erfolgreich eingefügt");
            confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
            confirm.showAndWait();

            if (!opController.getTowelBefore().equals(opController.getTowelAfter())) {
                Main.logger.warning("Die Bauchtücher vor und nach der Operation haben eine unterschiedliche Anzahl.");
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Achtung");
                alert.setHeaderText("Bauchtücher");
                alert.setContentText("Die Bauchtücher vor und nach der Operation haben eine unterschiedliche Anzahl!");
                alert.show();
            }
        }

    }

    /**
     * When the button for creating a new role is pushed, a new window is opened.
     */
    @FXML
    public void createRole() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneRole.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createAndShowNewPatientWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PanePatient.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void createAndShowNewFallWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneFall.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Fall");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * After successfully insertion of operation set all fields to default
     */
    private void clearFields() {
        selectPatient.getSelectionModel().clearSelection();
        opController.clearFields();
    }

    /**
     * Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
     * the Operation. The Operation is edited and the Window closes
     *
     * @param event The event of pushing the Speichern Button
     */
    @FXML
    public void saveEditOp(ActionEvent event) {
        opId = OverviewController.getOpId();
        editOperation();
        Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Sets the combobox in the Edit Op window to the values of the previously selected op to be edited.
     *
     * @param opID The OpId to be processed
     */
    public void initializeCombobox(int opID) {
        opController.initializeDefaultCombobox(opID);
        opController.initializeDefaultDateTimePicker(opID);
        Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(opID);
        Fall fall = new FallDao(Main.configuration).fetchOneByFallId(operation.getFallId());
        Patient patient = new PatientDao(Main.configuration).fetchOneByPatId(fall.getPatId());
        Patient patient1 = new Patient(patient) {
            @Override
            public String toString() {
                return "" + patient.getName() + ", " +
                        patient.getVorname() + ", PatID: " +
                        patient.getPatId();
            }
        };
        selectPatient.setValue(patient1);
    }


}
