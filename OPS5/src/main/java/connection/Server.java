package connection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.*;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import controller.CommunicationsController;
import controller.MainController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.*;
import main.Main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class Server {

    private HL7Service hapiServer;

    /**
     * Constructs a new server which listens to ADT01 and BARP05 messages while the application runs
     */
    public Server(){

        //creating a new server which should listen to the incomming messages
        hapiServer = Main.hapiContext.newServer(Main.port, Main.tls);

        //handles and listens to adt01 messages
        hapiServer.registerApplication("ADT", "A01", new ReceivingApplication<>() {
            @Override
            public Message processMessage(Message message, Map<String, Object> map){
                try{
                    String encodedMessage = MessageParser.pipeParser.encode(message);
                    CommunicationsController.getInstance().insertReceivedMessage(message);

                    Platform.runLater(()->{
                        //dem Nutzer zeigen, dass das Kis einen neuen Patienten gesendet hat
                        Main.logger.info("Das KIS hat eine Nachricht gesendet.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Es wurde etwas geschickt");
                        alert.setHeaderText("Das KIS hat eine Nachricht gesendet");
                        alert.setContentText(encodedMessage);
                        alert.show();
                    });
                } catch(HL7Exception e){
                    Platform.runLater(()->{
                        Main.logger.warning("Die Nachricht konnte nicht gelesen werden.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Die Nachricht konnte nicht gelesen werden.");
                        alert.showAndWait();
                    });
                }

                Patient patient = MessageParser.parseA01Patient(message);
                //only insert the patient if it is a new patient
                if(CommunicationsController.getInstance().isNewPatient(patient)){
                    patient.setErsteller("00000000");
                    patient.setErstellZeit(LocalDateTime.now());
                    CommunicationsController.insertNewPatient(patient);
                    Main.logger.info("Neuer Patient eingefügt");
                    //Patienten aktualisieren, wenn ein neuer gesendet wurde
                    MainController.getInstance().getAdmissionController().setPatient();
                }
                else{
                    patient.setBearbeiter("00000000");
                    patient.setBearbeiterZeit(LocalDateTime.now());
                    new PatientDao(Main.configuration).update(patient);
                    Main.logger.info("Patient bekannt und bearbeitet");
                }
                if(CommunicationsController.getInstance().canInsertPatient(patient)){
                    Fall fall = MessageParser.parseA01Case(message);
                    //wenn es ein valider Fall ist
                    if(CommunicationsController.getInstance().canInsertCase(fall)){
                        //wenn es ein neuer Fall ist wird er eingefügt
                        if(CommunicationsController.getInstance().isNewCase(fall)){
                            fall.setErsteller("00000000");
                            fall.setErstellZeit(LocalDateTime.now());
                            CommunicationsController.insertNewCase(fall);
                            Main.logger.info("Neuer Fall eingefügt");
                        }
                        else{
                            fall.setBearbeiter("00000000");
                            fall.setBearbeiterZeit(LocalDateTime.now());
                            new FallDao(Main.configuration).update(fall);
                        }
                        if(MessageParser.a01WithDiagnosis(message)){
                            List<Diagnose> diagnoseList = MessageParser.parseA01Diagnose(message);
                            assert diagnoseList != null;
                            for (Diagnose diagnose : diagnoseList) {
                                //falls neue Diagnose muss diese eingefügt werden
                                if(CommunicationsController.getInstance().isNewDiagnosis(diagnose)){
                                    //falls noch keine Operation zu dem Fall exisitert, wird eine neue erstellt
                                    if(new OperationDao(Main.configuration).fetchByFallId(fall.getFallId()).size() == 0){
                                        Operation operation = new Operation();
                                        operation.setErsteller("00000000");
                                        operation.setStorniert(false);
                                        operation.setErstellZeit(LocalDateTime.now());
                                        operation.setFallId(fall.getFallId());
                                        operation.setBauchtuecherPrae(0);
                                        operation.setBauchtuecherPost(0);
                                        operation.setGeplant(true);
                                        new OperationDao(Main.configuration).insert(operation);
                                        //setze die op id
                                        diagnose.setOpId(new OperationDao(Main.configuration).fetchByFallId(fall.getFallId()).get(0).getOpId());
                                    }
                                    else{
                                        //falls es schon eine Operation zu dem Fall gibt, wird die diagnose der neusten Operation
                                        //hinzugefügt
                                        List<Operation> operationList = new OperationDao(Main.configuration).fetchByFallId(fall.getFallId());
                                        operationList.sort(Comparator.comparing(Operation::getOpId));
                                        System.out.println();
                                        diagnose.setOpId(operationList.get(operationList.size()-1).getOpId());
                                    }
                                    diagnose.setErsteller("00000000");
                                    diagnose.setErstellZeit(LocalDateTime.now());
                                    new DiagnoseDao(Main.configuration).insert(diagnose);
                                }
                                else{
                                    diagnose.setBearbeiter("00000000");
                                    diagnose.setBearbeiterZeit(LocalDateTime.now());
                                    int opid = new DiagnoseDao(Main.configuration).fetchOneByDiagnoseId(diagnose.getDiagnoseId()).getOpId();
                                    diagnose.setOpId(opid);
                                    new DiagnoseDao(Main.configuration).update(diagnose);
                                }
                            }
                        }
                        //wenn alles korrekt war, dann wir der gültig auf wahr gesetzt
                        CommunicationsController.getInstance().setGueltig(message);
                    }
                    //reload the comboboxes with new patient/cases,...
                    Platform.runLater(()->{
                        CommunicationsController.getInstance().setCommunicationsObjectBox();
                        MainController.getInstance().getOverviewController().reload();
                    });
                }
                try {
                    return message.generateACK();
                } catch (IOException | HL7Exception e) {
                    Platform.runLater(()->{
                        Main.logger.warning("Es kann keine ACK-Nachricht erstellt werden.");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Es kann keine ACK-Nachricht erstellt werden!");
                        alert.showAndWait();
                    });
                }
                return null;
            }
             @Override
            public boolean canProcess(Message message) {
                return true;
            }
        });

        //handles and listens to barp05 messages
        hapiServer.registerApplication("BAR", "P05", new ReceivingApplication<>() {
            @Override
            public Message processMessage(Message message, Map<String, Object> map){
                try{
                    String encodedMessage = MessageParser.pipeParser.encode(message);
                    CommunicationsController.getInstance().insertReceivedMessage(message);

                    Platform.runLater(()->{
                        //dem Nutzer zeigen, dass das Kis einen neuen Patienten gesendet hat
                        Main.logger.info("Testnachricht von dem OPS empfangen.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Es wurde etwas geschickt");
                        alert.setHeaderText("Testnachricht von dem OPS");
                        alert.setContentText(encodedMessage);
                        alert.showAndWait();
                    });
                    CommunicationsController.getInstance().setGueltig(message);
                }catch(HL7Exception e){
                    Platform.runLater(()->{
                        Main.logger.warning("Die Nachricht kann nicht gelesen werden.");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Die Nachricht kann nicht gelesen werden!");
                        alert.showAndWait();
                    });
                }
                try {
                    return message.generateACK();
                } catch (IOException | HL7Exception e) {
                    Platform.runLater(()->{
                        Main.logger.warning("Es kann keine ACK-Nachricht erstellt werden.");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Es kann keine ACK-Nachricht erstellt werden!");
                        alert.showAndWait();
                    });
                }
                return null;
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
                //Main.logger.info("New connection received: " + connection.getRemoteAddress().toString());
            }

            @Override
            public void connectionDiscarded(Connection connection) {
                //Main.logger.info("Lost connection from: " + connection.getRemoteAddress().toString());
            }
        });
        //if a progress failes like the receiving or the process/responde of the message, the exception handler
        //handles this problem
        hapiServer.setExceptionHandler((s, map, s1, e) -> {
            Platform.runLater(()->{
                Main.logger.warning("Es ist ein Fehler beim Versenden aufgetreten.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Es ist ein Fehler beim Versenden aufgetreten.");
                alert.showAndWait();
            });
            return null;
        });

        //with this method the receiver starts to run
        try{
            hapiServer.startAndWait();
        } catch(InterruptedException e){
            Platform.runLater(()->{
                Main.logger.severe("Der Server wurde unterbrochen und hört auf keine Nachrichten mehr.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Der Server wurde unterbrochen und hört auf keine Nachrichten mehr.");
                alert.showAndWait();
            });
        }

    }

    public void closeServer() {
        hapiServer.stop();
    }
}