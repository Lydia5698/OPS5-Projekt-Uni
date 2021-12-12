package controller;
import ExternalFiles.Converter;
import ExternalFiles.TableViewMessage;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import connection.Client;
import connection.MessageParser;
import connection.Server;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommunicationsController {


    private static CommunicationsController communicationsController;

    @FXML
    private TextField communicationsIpAddress;
    @FXML
    private TextField communicationsPort;
    @FXML
    private ComboBox<Operation> communicationsObject;
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
	public void initialize(){
        communicationsController = this;
        try {
            startServer();
            setCommunicationsObjectBox();
            hl7Message.setCellValueFactory(param -> param.getValue().hl7MessageProperty());
            dateOfMessage.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getDateOfMessage().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), param.getValue().dateOfMessageProperty()));
            ackMessage.setCellValueFactory(param -> param.getValue().ackMessageProperty());

            communicationsIpAddress.setText(InetAddress.getLocalHost().getHostAddress());
            communicationsPort.setText(String.valueOf(Main.port));
            System.out.println("Initialize Communications-Tab!");
        } catch (UnknownHostException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Die Adresse kann nicht zu einer IP Adresse gecastet werden.");
                alert.showAndWait();
            });
        }
    }

    /**
     * This method starts the server so he listens to incoming ADT01 and BARP05(test) messages
     */
    private void startServer(){
	    try{
            server = new Server();
        } catch(InterruptedException e){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Der Server wurde unterbrochen und hört nicht mehr auf einkommende Nachrichten.");
                alert.showAndWait();
            });
        }
    }

    /**
     * This method returns the instance of the CommunicationController
     * @return the communicationcontroller
     */
    public static CommunicationsController getInstance(){
	    return communicationsController;
    }

    /**
     * This method sets all Objects which can be sended to the kis depending on the type
     * if the patients are choosen the patient combobox is visible and the operation combobox is set to invisible
     * and the same for operation
     */
    private void setCommunicationsObjectBox(){
            Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
                @Override
                public ListCell<Operation> call(ListView<Operation> patientListView) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(Operation op, boolean empty) {
                            super.updateItem(op, empty);
                            if (op == null || empty) {
                                setGraphic(null);
                            } else {
                                setText("OP-ID: " + op.getOpId() + ", " + "Fall-ID: " + op.getFallId() + "(" + Converter.fallIdToPatientsNameConverter(op.getFallId()) + ")");
                            }
                        }
                    };
                }
            };
            communicationsObject.setButtonCell(cellFactory.call(null));
            communicationsObject.setCellFactory(cellFactory);
            communicationsObject.getItems().setAll(new OperationDao(Main.configuration).findAll());
    }

    /**
     * This method inserts the received message into the tableview as hl7 string
     * @param message the incomming message
     */
    public void insertReceivedMessage(Message message){
        try{
            ts.getItems().add(new TableViewMessage(message.encode(), LocalDateTime.now(), "ja"));
        } catch(HL7Exception e){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Die Nachricht kann nicht in einen String umgewandelt werden.");
                alert.showAndWait();
            });
        }

    }

    /**
     * when the user pushes the button the selected patient/operation will be sent to the kis
     */
    @FXML
	public void send(){
    	System.out.println("Sending something!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");


        if(communicationsObject.getValue() == null){
            alert.setHeaderText("Keine Operation ausgewählt!");
            alert.setContentText("Es muss eine Operation ausgewählt werden, die verschickt werden soll!");
            alert.showAndWait();
        }
        else{
            Client client = new Client(communicationsIpAddress.getText(), Integer.parseInt(communicationsPort.getText()));

            Message sendMessage = MessageParser.parseBar05(communicationsObject.getValue());
            String stringFromMessage = MessageParser.messageToString(sendMessage);

            ts.getItems().add(new TableViewMessage(stringFromMessage, LocalDateTime.now(), "nein"));

            Message responseMessage = client.sendMessage(MessageParser.parseBar05(communicationsObject.getValue()));

            //if in ack was sended back, the value of gueltig changes to true
            if(responseMessage instanceof ACK){
                ACK ack = (ACK) responseMessage;
                if(ack.getMSA().getAcknowledgmentCode().getValue().equals("AA")){
                    ts.getItems().stream()
                            .filter(tM -> tM.getHl7Message().equals(stringFromMessage))
                            .forEach(tM -> tM.setAckMessage("ja"));
                }
            }
        }
    }

    /**
     * This method checks if the sent patient can be inserted into our database
     * @param patient the sent patient
     * @return true if he can be inserted and false if not
     */
    public boolean canInsert(Patient patient){
        //checking for values which can not be null (in this case it is the patients first and lastname)
            if (patient.getVorname().equals("") || patient.getName().equals("") || (patient.getGeburtsdatum() != null && patient.getGeburtsdatum().isAfter(LocalDate.now()))) {
                return false;
            }else{
                return true;
            }
    }

    /**
     * This method inserts if its possible the new Patient into our database
     * @param patient the sent patient
     */
    public static void insertNewPatient(Patient patient){
        PatientDao patientDao = new PatientDao(Main.configuration);
        //checking for values which can not be null (in this case it is the patients first and lastname)
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if (!getInstance().canInsert(patient)) {
                alert.setHeaderText("Patient kann nicht eingefügt werden!");
                alert.setContentText("Der gesendete Patient enthält fehlerhafte Eingaben und kann somit nicht eingefügt werden!");
                alert.showAndWait();
            }
            else {
                System.out.println(patient.getTelefonnummer());
                patientDao.insert(patient);
                System.out.println("Creating sent patient!");
            }
        });

    }

    /**
     * This method checks if the sent case can be inserted in our database and if yes , the case will be inserted
     * @param fall the sent case
     */
    public static void insertNewCase(Fall fall){
        FallDao fallDao = new FallDao(Main.configuration);
        //checking for values which can not be null (in this case it is only the patient)
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlender Eintrag!");

            //checking for invalid entrys concerning the dates
            //Entlassungsdatum ist vor dem Aufnahmedatum
            if(fall.getEntlassungsdatum() != null && fall.getAufnahmedatum() == null && fall.getEntlassungsdatum().isBefore(LocalDateTime.now())){
                alert.setHeaderText("Falscher Eintrag!");
                alert.setContentText("Das gesendete Entlassungsdatum liegt vor dem Aufnahmedatum!");
                alert.showAndWait();
            }
            //Entlassungsdatum ist vor dem Aufnahmedatum
            else if(fall.getEntlassungsdatum() != null && fall.getEntlassungsdatum().isBefore(fall.getAufnahmedatum())){
                alert.setHeaderText("Falscher Eintrag!");
                alert.setContentText("Das gesendete Entlassungsdatum liegt vor dem Aufnahmedatum!");
                alert.showAndWait();
            }
            else {
                //if the aufnahmedatum is null set it to the current date and time
                if (fall.getAufnahmedatum() == null) {
                    fall.setAufnahmedatum(LocalDateTime.now());
                }
                fallDao.insert(fall);
                System.out.println("Creating sent case!");
            }
        });

    }

}