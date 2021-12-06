package controller;
import ExternalFiles.Converter;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import connection.MessageParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;

import java.io.IOException;

public class CommunicationsController {
	
    @FXML
    private TextField communicationsIpAddress;
    @FXML
    private TextField communicationsPort;
    @FXML
    private ComboBox<String> communicationsType;
    @FXML
    private ComboBox<Patient> communicationsObjectPatient;
    @FXML
    private ComboBox<Operation> communicationsObjectOperation;
    @FXML
    private TableView<?> ts;
    
	@FXML
	public void initialize() {
	    setCommunicationsTypeBox();
        //the communicationobject depends on the type which is chosen
        communicationsType.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                if(communicationsType.getValue() != null){setCommunicationsObjectBox(communicationsType.getValue());}
            }
        });

    	System.out.println("Initialize Communications-Tab!");        
    }

    /**
     * This method sets the communication types which are able (for now it is patient and operation)
     */
    private void setCommunicationsTypeBox(){
	    communicationsType.getItems().setAll("Patient", "Operation");
    }

    /**
     * This method sets all Objects which can be sended to the kis depending on the type
     * if the patients are choosen the patient combobox is visible and the operation combobox is set to invisible
     * and the same for operation
     * @param s string of the type which is chosen (patient or operation)
     */
    private void setCommunicationsObjectBox(String s){
	    if(s.equals("Patient")){
	        communicationsObjectPatient.setVisible(true);
	        communicationsObjectOperation.setVisible(false);
            Callback<ListView<Patient>, ListCell<Patient>> cellFactory = Converter.getPatient();
            communicationsObjectPatient.setButtonCell(cellFactory.call(null));
            communicationsObjectPatient.setCellFactory(cellFactory);
            communicationsObjectPatient.getItems().setAll(new PatientDao(Main.configuration).findAll());
        }
	    else{
	        communicationsObjectOperation.setVisible(true);
	        communicationsObjectPatient.setVisible(false);
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
            communicationsObjectOperation.setButtonCell(cellFactory.call(null));
            communicationsObjectOperation.setCellFactory(cellFactory);
            communicationsObjectOperation.getItems().setAll(new OperationDao(Main.configuration).findAll());
        }

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

        if(communicationsType.getValue().equals(null)){
            alert.setHeaderText("Nichts ausgewählt!");
            alert.setContentText("Es muss etwas ausgewählt werden, dass verschickt werden soll!");
            alert.showAndWait();
        }
        else if(communicationsType.getValue().equals("Patient")){
            if(communicationsObjectPatient.getValue() == null){
                alert.setHeaderText("Kein Patient ausgewählt!");
                alert.setContentText("Es muss ein Patient ausgewählt werden, der verschickt werden soll!");
                alert.showAndWait();
            } else{
                Main.client.sendMessage(MessageParser.parseBar05Patient(communicationsObjectPatient.getValue()));
            }
        }
        else{
            if(communicationsObjectOperation.getValue() == null){
                alert.setHeaderText("Keine Operation ausgewählt!");
                alert.setContentText("Es muss eine Operation ausgewählt werden, die verschickt werden soll!");
                alert.showAndWait();
            } else{
                Main.client.sendMessage(MessageParser.parseBar05Operation(communicationsObjectOperation.getValue()));
            }
        }

    }
    

}

