package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.TimeoutException;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import main.Main;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    public Client(String ipAdress, int port) throws HL7Exception {
            Connection connection = Main.hapiContext.newClient(ipAdress, port, Main.tls);

            initiator = connection.getInitiator();
            initiator.setTimeout(7, TimeUnit.SECONDS);
    }

    /**
     * this method sends a given message to the given ipadress
     * @param message the message which should be send
     * @return the response message (Ack from the receiving part)
     * @throws HL7Exception thrown if the message can not be send
     * @throws LLPException thrown if the message can not be send
     * @throws IOException thrown if the message can not be send
     */
    public Message sendMessage(Message message) throws HL7Exception, LLPException, IOException {
        Message response;
        for (int i=0; i < 4; i++){ //try 4 times to send the message
            try{
                response = initiator.sendAndReceive(message);
                return response;
            }
            catch(TimeoutException e){ } // if a timeout occures send the message again
        }
        response = initiator.sendAndReceive(message);  // if the response is not already returned in the try statement, send the message one last time
        return response;
    }

}
