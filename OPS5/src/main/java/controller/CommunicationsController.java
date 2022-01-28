package controller;

import ExternalFiles.Converter;
import ExternalFiles.TableViewMessage;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.MllpConstants;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import connection.Client;
import connection.MessageParser;
import connection.Server;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Diagnose;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Is responsible for communication with the KIS
 */
public class CommunicationsController {

    @FXML
    private TextField communicationsIpAddress;
    @FXML
    private TextField communicationsPort;
    @FXML
    private SearchableComboBox<Operation> communicationsObject;
    @FXML
    private TableView<TableViewMessage> ts;
    @FXML
    private TableColumn<TableViewMessage, String> hl7Message;
    @FXML
    private TableColumn<TableViewMessage, String> dateOfMessage;
    @FXML
    private TableColumn<TableViewMessage, String> ackMessage;

    private Server server;

    @FXML
    public void initialize() {
        try {
            System.setProperty(MllpConstants.CHARSET_KEY, "ISO-8859-1");
            startServer();
            setCommunicationsObjectBox();
            hl7Message.setCellValueFactory(param -> param.getValue().hl7MessageProperty());
            dateOfMessage.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getDateOfMessage().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), param.getValue().dateOfMessageProperty()));
            ackMessage.setCellValueFactory(param -> param.getValue().ackMessageProperty());

            communicationsIpAddress.setText(InetAddress.getLocalHost().getHostAddress());
            communicationsPort.setText(String.valueOf(Main.port));
            Main.logger.info("Initialize Communications-Tab!");
        } catch (UnknownHostException e) {
            Platform.runLater(() -> {
                Main.logger.warning("Die Adresse kann nicht zu einer IP Adresse gecastet werden.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Achtung");
                alert.setHeaderText("Adresse");
                alert.setContentText("Die Adresse kann nicht zu einer IP Adresse gecastet werden.");
                alert.showAndWait();
            });
        }
    }

    /**
     * This method starts the server so he listens to incoming ADT01 and BARP05(test) messages
     */
    private void startServer() {
        server = new Server();
    }

    /**
     * This method closes the server (when the application is closed)
     */
    public void closeServer() {
        server.closeServer();
    }

    /**
     * This method returns the instance of the CommunicationController
     *
     * @return The communication controller
     */
    public static CommunicationsController getInstance() {
        return MainController.getInstance().getCommTabController();
    }

    /**
     * This method sets all Objects which can be sent to the kis depending on the type
     * if the patients are chosen the patient combobox is visible and the operation combobox is set to invisible
     * and the same for operation
     */
    private void setCommunicationsObjectBox() {
        Converter.setOperation(communicationsObject, "communication");
    }

    /**
     * This method inserts the received message into the tableview as hl7 string
     *
     * @param message The incoming message
     */
    public void insertReceivedMessage(Message message) {
        try {
            ts.getItems().add(new TableViewMessage(message.encode(), LocalDateTime.now(), "ja"));
        } catch (HL7Exception e) {
            Platform.runLater(() -> {
                Main.logger.warning("Die Nachricht kann nicht in einen String umgewandelt werden.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Achtung");
                alert.setHeaderText("Nachricht");
                alert.setContentText("Die Nachricht kann nicht in einen String umgewandelt werden.");
                alert.showAndWait();
            });
        }

    }

    /**
     * When the user pushes the button the selected patient/operation will be sent to the kis
     */
    @FXML
    public void send() {
        Main.logger.info("Sending something!");

        if (communicationsObject.getValue() == null) {
            Main.logger.warning("Es muss eine Operation ausgewählt werden, die verschickt werden soll.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Keine Operation ausgewählt!");
            alert.setContentText("Es muss eine Operation ausgewählt werden, die verschickt werden soll!");
            alert.showAndWait();
        } else {
            Thread thread = new Thread(()->{
                Client client = new Client(communicationsIpAddress.getText(), Integer.parseInt(communicationsPort.getText()));
                try {
                    Message sendMessage = MessageParser.parseBar05(communicationsObject.getValue());
                    String stringFromMessage = MessageParser.messageToString(sendMessage);

                    ts.getItems().add(new TableViewMessage(stringFromMessage, LocalDateTime.now(), "nein"));

                    Message responseMessage = client.sendMessage(MessageParser.parseBar05(communicationsObject.getValue()));

                    //if in ack was sent back, the value of gültig changes to true
                    if (responseMessage instanceof ACK) {
                        ACK ack = (ACK) responseMessage;
                        if (ack.getMSA().getAcknowledgmentCode().getValue().equals("AA")) {
                            ts.getItems().stream()
                                    .filter(tM -> tM.getHl7Message().equals(stringFromMessage))
                                    .forEach(tM -> tM.setAckMessage("ja"));
                        }
                    }
                } finally {
                    client.closeClient();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * This method checks if the sent patient can be inserted into our database
     *
     * @param patient The sent patient
     * @return True if he can be inserted and false if not
     */
    public boolean canInsertPatient(Patient patient) {
        //checking for values which can not be null (in this case it is the patients first and lastname)
        return !patient.getVorname().equals("") && !patient.getName().equals("") && (patient.getGeburtsdatum() == null || !patient.getGeburtsdatum().isAfter(LocalDate.now()));
    }

    public boolean isNewPatient(Patient patient) {
        return new PatientDao(Main.configuration).findById(patient.getPatId()) == null;
    }

    public boolean isNewDiagnosis(Diagnose diagnose){
        return new DiagnoseDao(Main.configuration).findById(diagnose.getDiagnoseId()) == null;
    }

    /**
     * This method inserts if its possible the new Patient into our database
     *
     * @param patient The sent patient
     */
    public static void insertNewPatient(Patient patient) {
        PatientDao patientDao = new PatientDao(Main.configuration);
        //checking for values which can not be null (in this case it is the patients first and lastname)
        Platform.runLater(() -> {
            if (!getInstance().canInsertPatient(patient)) {
                Main.logger.warning("Der gesendete Patient enthält fehlerhafte Eingaben und kann somit nicht eingefügt werden.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Patient kann nicht eingefügt werden!");
                alert.setContentText("Der gesendete Patient enthält fehlerhafte Eingaben und kann somit nicht eingefügt werden!");
                alert.showAndWait();
            } else {
                patientDao.insert(patient);
                Main.logger.info("Creating sent patient!");
            }
        });

    }

    /**
     * Checks if a case can be inserted (has no invalid entries)
     *
     * @param fall The case
     * @return false if there is wrong information about the case
     */
    public boolean canInsertCase(Fall fall) {
        if (fall.getEntlassungsdatum() != null && fall.getAufnahmedatum() == null && fall.getEntlassungsdatum().isBefore(LocalDateTime.now())) {
            return true;
        }
        return !(fall.getEntlassungsdatum() != null && fall.getEntlassungsdatum().isBefore(fall.getAufnahmedatum()));
    }

    public boolean isNewCase(Fall fall){
        return new FallDao(Main.configuration).findById(fall.getFallId()) == null;
    }

    /**
     * This method checks if the sent case can be inserted in our database and if yes , the case will be inserted
     *
     * @param fall The sent case
     */
    public static void insertNewCase(Fall fall) {
        FallDao fallDao = new FallDao(Main.configuration);
        //checking for values which can not be null (in this case it is only the patient)
        Platform.runLater(() -> {

            //checking for invalid entries concerning the dates
            //Entlassungsdatum ist vor dem Aufnahmedatum
            if (getInstance().canInsertCase(fall)) {
                Main.logger.warning("Der Fall hat invalide Eingaben und kann nicht eingefügt werden.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Fehlender Eintrag!");
                alert.setContentText("Der Fall hat invalide Eingaben und kann nicht eingefügt werden.");
                alert.showAndWait();
            } else {
                //if the aufnahmedatum is null set it to the current date and time
                if (fall.getAufnahmedatum() == null) {
                    fall.setAufnahmedatum(LocalDateTime.now());
                }
                fallDao.insert(fall);
                Main.logger.info("Creating sent case!");
            }
        });

    }

}