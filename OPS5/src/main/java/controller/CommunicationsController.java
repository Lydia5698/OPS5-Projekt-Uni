package controller;
import ExternalFiles.Converter;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import connection.Client;
import connection.MessageParser;
import connection.Server;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Operation;
import main.Main;

import java.io.IOException;

public class CommunicationsController {
	
    @FXML
    private TextField communicationsIpAddress;
    @FXML
    private TextField communicationsPort;
    @FXML
    private ComboBox<Operation> communicationsObject;
    @FXML
    private TableView<?> ts;
    @FXML
    private TableColumn<?, String> hl7Message;
    @FXML
    private TableColumn<?, String> dateOfMessage;
    @FXML
    private TableColumn<?, String> ackMessage;

    private Server server;
    
	@FXML
	public void initialize() throws InterruptedException {
        startServer();
        setCommunicationsObjectBox();

    	System.out.println("Initialize Communications-Tab!");

    }

    private void startServer() throws InterruptedException {
	    server = new Server();
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
        } else{
            Client client = new Client();
            System.out.println(MessageParser.parseBar05(communicationsObject.getValue()).printStructure());
            client.sendMessage(MessageParser.parseBar05(communicationsObject.getValue()));
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