package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.*;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import controller.CommunicationsController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Patient;
import main.Main;

import java.io.IOException;
import java.util.Map;


public class Server {

    private HL7Service hapiServer;

    public Server() throws InterruptedException{

        //creating a new server which should listen to the incomming messages
        hapiServer = Main.hapiContext.newServer(Main.port, Main.tls);

        //handles and listens to adt01 messages
        hapiServer.registerApplication("ADT", "A01", new ReceivingApplication<>() {
            @Override
            public Message processMessage(Message message, Map<String, Object> map) throws HL7Exception {
                String encodedMessage = MessageParser.pipeParser.encode(message);
                //System.out.println(encodedMessage);
                CommunicationsController.getInstance().insertReceivedMessage(message);

                Platform.runLater(()->{
                    //dem Nutzer zeigen, dass das Kis einen neuen Patienten gesendet hat
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Es wurde etwas geschickt");
                    alert.setHeaderText("Das KIS hat einen neuen Patienten geschickt");
                    alert.setContentText(encodedMessage);
                    alert.showAndWait();
                });


                Patient patient = MessageParser.parseA01Patient(message);
                Integer patid = patient.getPatId();
                //CommunicationsController.insertNewPatient(patient);
                if(CommunicationsController.getInstance().canInsert(patient)){
                    Fall fall = MessageParser.parseA01Case(message);
                    //CommunicationsController.insertNewCase(fall, patid);
                }
                try {
                    return message.generateACK();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
             @Override
            public boolean canProcess(Message message) {
                return true;
            }
        });

        //handles and listens to barp05 messages
        hapiServer.registerApplication("BAR", "P05", new ReceivingApplication<>() {
            @Override
            public Message processMessage(Message message, Map<String, Object> map) throws HL7Exception {
                String encodedMessage = MessageParser.pipeParser.encode(message);
                //System.out.println(encodedMessage);

                Platform.runLater(()->{
                //dem Nutzer zeigen, dass das Kis einen neuen Patienten gesendet hat
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Es wurde etwas geschickt");
                alert.setHeaderText("Testnachricht von dem OPS");
                alert.setContentText(encodedMessage);
                alert.showAndWait();
                });

                //Patient patient = MessageParser.parseA01(message);
                //PatientDao patientDao = new PatientDao(Main.configuration);
                //patientDao.insert(patient);
                //TODO valide Abfragen tätigen (not null und geburtstag,...)
                try {
                    return message.generateACK();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public boolean canProcess(Message message) {
                return true;
            }
        });

        //the connection listener notifies if the connection gets lost or a new connection has built
        hapiServer.registerConnectionListener(new ConnectionListener() {
            @Override
            public void connectionReceived(Connection connection) {
                System.out.println("New connection received: " + connection.getRemoteAddress().toString());
            }

            @Override
            public void connectionDiscarded(Connection connection) {
                System.out.println("Lost connection from: " + connection.getRemoteAddress().toString());
            }
        });
        //if a progress failes like the receiving or the process/responde of the message, the exception handler
        //handles this problem
        //TODO: show an alert or something if a progress failes
        hapiServer.setExceptionHandler((s, map, s1, e) -> {
            //s1 is the negative ackloglegement if the message cant be send
            return s1;
        });
        //with this method the receiver starts to run
        hapiServer.startAndWait();
    }

}

 /*   int port = 1011;
    boolean useTls = false;
    HapiContext context = new DefaultHapiContext();
    HL7Service server = context.newServer(port, useTls);
    ReceivingApplication<Message> handler = new ExampleReceiverApplication();
    server.registerApplication("ADT", "A01", handler);
    server.start();
    String msg = "MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01|12345|P|2.2\r"
        + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
        + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333|(888)999-0000|||||||ORGANIZATION\r"
        + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
        + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
        + "AL1||SEV|001^POLLEN\r"
        + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
        + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554";
    Parser p = context.getPipeParser();

    Message adt = p.parse(msg);

    Connection connection = context.newClient("localhost", port, useTls);

    Initiator initiator = connection.getInitiator();
    Message response = initiator.sendAndReceive(adt);

    String responseString = p.encode(response);
    System.out.println("Received response:\n" + responseString);

    connection = context.newClient("localhost", port, useTls);
    initiator = connection.getInitiator();
    response = initiator.sendAndReceive(adt);

    connection.close();

    server.stopAndWait();
    */

