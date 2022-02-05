package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.TimeoutException;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.Main;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * the receiver is listening for incomming messages
 */
public class Client {

    Connection connection;
    Initiator initiator;

    /**
     * Creates a client with a given ipadress and a given port (given from the communicationcontroller
     * @param ipAdress given ipadress (default is the localhost)
     * @param port the port
     */
    public Client(String ipAdress, int port){
        try{
            connection = Main.hapiContext.newClient(ipAdress, port, Main.tls);

            initiator = connection.getInitiator();
            initiator.setTimeout(7, TimeUnit.SECONDS);
        } catch(HL7Exception e){
            Platform.runLater(()->{
                Main.logger.severe("Es kann keine Verbindung zu einem Server aufgebaut werden.");
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
     */
    public Message sendMessage(Message message){
        Message response;
        if(this.initiator == null){return null;} //if the initiator is null an alert has already be thrown so the message
        //can not be sent at all
        for (int i=0; i < 5; i++){ //try 5 times to send the message
            try{
                response = initiator.sendAndReceive(message);
                return response;
            }
            catch(TimeoutException ignored){ } // if a timeout occures send the message again
            catch(HL7Exception | LLPException | IOException e){
                Platform.runLater(()->{
                    Main.logger.warning("Die Nachricht kann nicht gesendet werden.");
                    Alert alertConnection = new Alert(Alert.AlertType.WARNING);
                    alertConnection.setContentText("Die Nachricht kann nicht gesendet werden.");
                    alertConnection.showAndWait();
                });
            }
        }
        return null;
    }

    /**
     * closes the connection when the process is finished
     */
    public void closeClient(){
        if(connection != null){connection.close();}
    }

}
