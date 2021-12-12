package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.Main;

import java.io.IOException;

/**
 * the receiver is listening for incomming messages
 */
public class Client {

    Initiator initiator;

    /**
     * Creates a client with a given ipadress and a given port (given from the communicationcontroller
     * @param ipAdress given ipadress (default is the localhost)
     * @param port the port
     * @throws HL7Exception it is thrown if the connection can not be built
     */
    public Client(String ipAdress, int port){
        try{
            Connection connection = Main.hapiContext.newClient(ipAdress, port, Main.tls);

            initiator = connection.getInitiator();
        } catch(HL7Exception e){
            Platform.runLater(()->{
                Alert alertConnection = new Alert(Alert.AlertType.WARNING);
                alertConnection.setContentText("Es kann keine Verbindung aufgebaut werden.");
                alertConnection.showAndWait();
            });
        }

    }

    /**
     * this method sends a given message to the given ipadress
     * @param message the message which should be send
     * @return the response message (Ack from the receiving part)
     * @throws HL7Exception thrown if the message can not be send
     * @throws LLPException thrown if the message can not be send
     * @throws IOException thrown if the message can not be send
     */
    public Message sendMessage(Message message){
        try{
            if(this.initiator == null){return null;} //if the initiator is null an alert has already be thrown so the message
            //can not be sent at all
            Message response = initiator.sendAndReceive(message);
            return response;
        } catch(HL7Exception | LLPException | IOException e){
            Platform.runLater(()->{
                Alert alertConnection = new Alert(Alert.AlertType.WARNING);
                alertConnection.setContentText("Die Nachricht kann nicht gesendet werden.");
                alertConnection.showAndWait();
            });
        }
        return null;
    }

}
