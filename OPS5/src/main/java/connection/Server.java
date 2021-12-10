package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.*;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import controller.CommunicationsController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.pojos.Diagnose;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.Patient;
import main.Main;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Server {

    private HL7Service hapiServer;

    /**
     * Constructs a new server which listens to ADT01 and BARP05 messages while the application runs
     * @throws InterruptedException
     */
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
                CommunicationsController.insertNewPatient(patient);
                if(CommunicationsController.getInstance().canInsert(patient)){
                    Fall fall = MessageParser.parseA01Case(message);
                    CommunicationsController.insertNewCase(fall);
                    System.out.println("Patient und Fall eingefügt");

                    Platform.runLater(()-> {
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setContentText("Der Patient und der Fall wurden in die Datenbank eingefügt.");
                        confirm.showAndWait();
                    });
                if(MessageParser.a01WithDignosis(message)){
                    List<Diagnose> diagnoseList = MessageParser.parseA01Diagnose(message);
                    for(int i = 0; i < diagnoseList.size(); i++){
                        new DiagnoseDao(Main.configuration).insert(diagnoseList.get(i));
                    }
                    Platform.runLater(()-> {
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setContentText("Die gesendeten Diagnosen wurden dem gesendeten Fall zugeordnet.");
                        confirm.showAndWait();
                    });
                }
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