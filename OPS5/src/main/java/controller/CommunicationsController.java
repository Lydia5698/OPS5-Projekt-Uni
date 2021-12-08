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
	public void initialize() throws InterruptedException, UnknownHostException {
        communicationsController = this;
	    startServer();
        setCommunicationsObjectBox();
        hl7Message.setCellValueFactory(param -> param.getValue().hl7MessageProperty());
        dateOfMessage.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getDateOfMessage().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), param.getValue().dateOfMessageProperty()));
        ackMessage.setCellValueFactory(param -> param.getValue().ackMessageProperty());

        communicationsIpAddress.setText(InetAddress.getLocalHost().getHostAddress());
        communicationsPort.setText(String.valueOf(Main.port));

    	System.out.println("Initialize Communications-Tab!");

    }

    /**
     * This method starts the server so he listens to incomint ADT01 and BARP05(test) messages
     * @throws InterruptedException it is thrown if the server is interrupted
     */
    private void startServer() throws InterruptedException {
	    server = new Server();
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
                                setText("OP-ID: " + op.getFallId() + ", " + "Fall-ID: " + op.getFallId() + "(" + Converter.fallIdToPatientsNameConverter(op.getFallId()) + ")");
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
     * @throws HL7Exception if the message can not be encoded
     */
    public void insertReceivedMessage(Message message) throws HL7Exception {
        ts.getItems().add(new TableViewMessage(message.encode(), LocalDate.now(), "ja"));
    }

    /**
     * when the user pushes the button the selected patient/operation will be sent to the kis
     * @throws HL7Exception if the message cannot be sent to the kis
     * @throws LLPException if the message cannot be sent to the kis
     * @throws IOException if the message cannot be sent to the kis
     */
    @FXML
	public void send() throws HL7Exception, LLPException, IOException {
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

            ts.getItems().add(new TableViewMessage(stringFromMessage, LocalDate.now(), "nein"));

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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (getInstance().canInsert(patient) == false) {
            alert.setHeaderText("Patient kann nicht eingefügt werden!");
            alert.setContentText("Der gesendete Patient enthält fehlerhafte Eingaben und kann somit nicht eingefügt werden!");
            alert.showAndWait();
        }
        else {
            patientDao.insert(patient);
            System.out.println("Creating sended patient!");
        }
    }

    /**
     * This method checks if the sent case can be inserted in our database and if yes , the case will be inserted
     * @param fall the sent case
     * @param patid the patientid of the sent patient
     */
    public static void insertNewCase(Fall fall, Integer patid){
        FallDao fallDao = new FallDao(Main.configuration);
        fall.setPatId(patid);
        //checking for values which can not be null (in this case it is only the patient)
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
            System.out.println("Creating sended case!");
        }
    }

}
/**
else if(communicationsType.getValue().equals("Patient")){
        if(communicationsObjectPatient.getValue() == null){
        alert.setHeaderText("Kein Patient ausgewählt!");
        alert.setContentText("Es muss ein Patient ausgewählt werden, der verschickt werden soll!");
        alert.showAndWait();
        } else{
        Client client = new Client();
        client.sendMessage(MessageParser.parseBar05Patient(communicationsObjectPatient.getValue()));
        }
        }
 */