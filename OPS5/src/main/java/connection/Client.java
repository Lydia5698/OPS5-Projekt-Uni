package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;
import main.Main;

import java.io.IOException;
import java.util.Map;

/**
 * the receiver is listening for incomming messages
 */
public class Client {

    Initiator initiator;

    public Client() throws HL7Exception {
            Connection connection = Main.hapiContext.newClient("localhost", Main.port, Main.tls);

            initiator = connection.getInitiator();
        }

        public void sendMessage(Message message) throws HL7Exception, LLPException, IOException {
            Message response = initiator.sendAndReceive(message);
        }

    //TODO how to send a lot of messages

}
