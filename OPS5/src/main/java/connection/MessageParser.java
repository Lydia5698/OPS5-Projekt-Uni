package connection;


import ExternalFiles.Converter;
import ca.uhn.hl7v2.model.v251.message.BAR_P05;
import ca.uhn.hl7v2.model.v251.segment.*;
import ca.uhn.hl7v2.parser.PipeParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MessageParser {

    public static PipeParser pipeParser = Main.hapiContext.getPipeParser();

    /**
     * This message parses a message (adt01) into a patient
     * @param a01Message the sent message
     * @return the created patient
     */
    public static Patient parseA01Patient(Message a01Message){
        ADT_A01 adtMsg = (ADT_A01) a01Message;

        PID pid = adtMsg.getPID();

        Patient patient = new Patient();
        patient.setPatId(Integer.parseInt(pid.getPatientID().getCx1_IDNumber().getValue()));
        patient.setName(pid.getPatientName(0).getFamilyName().getSurname().getValue());
        patient.setVorname(pid.getPatientName(0).getGivenName().getValue());
        patient.setGeburtsdatum(DateTimeFormatter.BASIC_ISO_DATE.parse(pid.getDateTimeOfBirth().getTime().getValue(), LocalDate::from));
        patient.setGeschlecht(Converter.SexFromISSToOurConverter(pid.getAdministrativeSex().getValue()));
        patient.setStorniert(false);
        patient.setGeburtsort(pid.getBirthPlace().getValue());
        patient.setStrasse(pid.getPatientAddress(0).getStreetAddress().getStreetName().getValue() + " " + pid.getPatientAddress(0).getStreetAddress().getDwellingNumber().getValue());
        patient.setPostleitzahl(pid.getPatientAddress(0).getXad5_ZipOrPostalCode().getValue());
        patient.setTelefonnummer(pid.getPhoneNumberHome(0).getTelephoneNumber().getValue());
        patient.setErsteller("00000000");
        patient.setErstellZeit(LocalDateTime.now()); //TODO hier evt noch umändern
        return patient;

    }

    /**
     * This method creates a case out of a given message
     * @param a01message sent message
     * @return the created case
     */
    public static Fall parseA01Case(Message a01message){
        ADT_A01 adtMsg = (ADT_A01) a01message;

        PID pid = adtMsg.getPID();
        PV1 pv1 = adtMsg.getPV1();

        Fall fall = new Fall();
        fall.setFallId(Integer.parseInt(pv1.getSetIDPV1().getValue()));
        fall.setPatId(Integer.parseInt(pid.getPatientID().getCx1_IDNumber().getValue()));
        fall.setFallTyp(pv1.getPatientClass().getValue().equals("Inpatient") ? 1 : 2);
        fall.setAufnahmedatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getAdmitDateTime().getTime().getValue())));
        if(pv1.getDischargeDateTime(0).getTime().getValue() != null){fall.setEntlassungsdatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getDischargeDateTime(0).getTime().getValue())));}
        fall.setStationSt(pv1.getAssignedPatientLocation().getPl1_PointOfCare().getValue());
        fall.setStorniert(false);
        fall.setErsteller(pv1.getAdmittingDoctor(0).getIDNumber().getValue());
        fall.setErstellZeit(LocalDateTime.now());
        return fall;
    }

    /**
     * In case that the KIS sends also diagnoses we have to create an empty operation to the new case and add the diagnosis to them
     * @param a01message the incoming message from kis
     * @return null if there is no diagnosis field
     */
    public static List<Diagnose> parseA01Diagnose(Message a01message){
        ADT_A01 adtMsg = (ADT_A01) a01message;
        List<Diagnose> diagnoses = new ArrayList<>();
        try{
            if(adtMsg.getDG1All().size() == 0){
                return null;
            }
            else{
                PV1 pv1 = adtMsg.getPV1();

                Operation operation = new Operation();
                operation.setErsteller("00000000");
                operation.setStorniert(false);
                operation.setErstellZeit(LocalDateTime.now());
                operation.setFallId(Integer.parseInt(pv1.getSetIDPV1().getValue()));
                operation.setBauchtuecherPrae(0);
                operation.setBauchtuecherPost(0);

                new OperationDao(Main.configuration).insert(operation);


                List<DG1> dg1List = adtMsg.getDG1All();
                for (DG1 dg1 : dg1List) {
                    Diagnose diagnose = new Diagnose();
                    diagnose.setErsteller("00000000");
                    diagnose.setErstellZeit(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(dg1.getDiagnosisDateTime().getTime().getValue())));
                    diagnose.setDiagnosetyp(1);
                    diagnose.setIcd10Code(dg1.getDiagnosisCodeDG1().getCe1_Identifier().getValue());
                    diagnose.setKlartextDiagnose(dg1.getDiagnosisDescription().getValue());
                    diagnose.setStorniert(false);
                    //TODO ersteller kis oder medpersonal
                    //TODO erstellzeit now oder aus der Nachricht
                    diagnoses.add(diagnose);
                    return diagnoses;
                }
             }
        } catch(HL7Exception e){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Die Nachricht kann nicht umgewandelt werden");
                    alert.showAndWait();
                });
                return null;
        }
        return null;
    }


    /**
     * Checks if an adt01 message contains diagnosis
     * @param message the sent message
     * @return ture, if the message contains a diagnosis
     */
    public static boolean a01WithDignosis(Message message){
        ADT_A01 adt_a01 = (ADT_A01) message;
        try{
            return adt_a01.getDG1All().size() > 0;
        } catch(HL7Exception e){
            return false;
        }

    }

    /**
     * This method parses an new operation into a message
     * @param operation operation which should be casted into a message
     * @return the message of the new operation
     */
    public static Message parseBar05(Operation operation){
        BAR_P05 bar05 = new BAR_P05();
        try{bar05.initQuickstart("BAR","P05", "P");}
        catch(Exception e){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Es kann keine Nachricht erstellt werden.");
                alert.showAndWait();
            });
        }

        Fall fall = new FallDao(Main.configuration).findById(operation.getFallId());
        Patient patient = new PatientDao(Main.configuration).findById(fall.getPatId());
        MedPersonal medPersonal = new MedPersonalDao(Main.configuration).findById(fall.getErsteller());

        //msh
        MSH msh = bar05.getMSH();
        try {
            msh.getDateTimeOfMessage().getTime().setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            msh.getSendingApplication().getNamespaceID().setValue("OPS5");
            msh.getReceivingApplication().getNamespaceID().setValue("KIS2");
            msh.getSequenceNumber().setValue("123");

            EVN evn = bar05.getEVN();
            evn.getEventTypeCode().setValue("P05");
            evn.getRecordedDateTime().getTime().setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            //pid
            PID pid = bar05.getPID();
            assert patient != null;
            pid.getPatientID().getCx1_IDNumber().setValue(patient.getPatId().toString());
            pid.getPatientName(0).getFamilyName().getSurname().setValue(patient.getName());
            pid.getPatientName(0).getGivenName().setValue(patient.getVorname());
            pid.getDateTimeOfBirth().getTime().setValue(patient.getGeburtsdatum().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            pid.getAdministrativeSex().setValue(Converter.IssSexConverter(patient.getGeschlecht()));
            pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(patient.getStrasse());
            pid.getPatientAddress(0).getZipOrPostalCode().setValue(patient.getPostleitzahl());
            pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(patient.getTelefonnummer());
            pid.getBirthPlace().setValue(patient.getGeburtsort());

            //pv1
            PV1 pv1 = bar05.getVISIT().getPV1();
            pv1.getSetIDPV1().setValue(operation.getFallId().toString());
            if (fall.getFallTyp() != null) {
                pv1.getPatientClass().setValue(Converter.fallTypConverter(fall.getFallTyp()).equals("stationär") ? "Inpatient" : "Outpatient");
            }
            pv1.getAdmitDateTime().getTime().setValue(fall.getAufnahmedatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            assert medPersonal != null;
            pv1.getAdmittingDoctor(0).getIDNumber().setValue(medPersonal.getPersId());
            pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(medPersonal.getName());
            pv1.getAdmittingDoctor(0).getGivenName().setValue(medPersonal.getVorname());
            pv1.getVisitNumber().getCx1_IDNumber().setValue(fall.getFallId().toString());
            if (fall.getEntlassungsdatum() != null) {
                pv1.getDischargeDateTime(0).getTime().setValue(fall.getEntlassungsdatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            pv1.getAssignedPatientLocation().getPointOfCare().setValue(fall.getStationSt());


            //DG1 fields
            List<Diagnose> diagnose = new DiagnoseDao(Main.configuration).fetchByOpId(operation.getOpId());
            for (int i = 0; i < diagnose.size(); i++) {
                DG1 dg1 = bar05.getVISIT().getDG1(i);
                dg1.getSetIDDG1().setValue(diagnose.get(i).getDiagnoseId().toString());
                dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(diagnose.get(i).getIcd10Code());
                dg1.getDiagnosisDescription().setValue(diagnose.get(i).getKlartextDiagnose());
                dg1.getDiagnosisDateTime().getTime().setValue(diagnose.get(i).getErstellZeit().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                dg1.getDiagnosingClinician(0).getIDNumber().setValue(diagnose.get(i).getErsteller());
            }

            //PR1 fields
            List<Prozedur> prozedurs = new ProzedurDao(Main.configuration).fetchByOpId(operation.getOpId());
            for (Prozedur prozedur : prozedurs) {
                PR1 pr1 = bar05.getVISIT(0).getPROCEDURE().getPR1();
                pr1.getSetIDPR1().setValue(prozedur.getProzId().toString());
                pr1.getProcedureCode().getIdentifier().setValue(prozedur.getOpsCode());
                pr1.getProcedureDescription().setValue(prozedur.getAnmerkung());
                pr1.getProcedureDateTime().getTime().setValue(prozedur.getErstellZeit().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                pr1.getSurgeon(0).getIDNumber().setValue(prozedur.getErsteller());
            }
        } catch(HL7Exception e){
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Es kann keine Nachricht aus den eingegebenen Daten geparsed werden.");
                alert.showAndWait();
            });
        }
       return bar05;
    }

    /**
     * This method converts a message to a string so it can be displayed in the tableview in the communicationcontroller
     * @param message the incomming/outgoing message
     * @return the string of the hl7 messagr
     */
    public static String messageToString(Message message) {
        try {
            return pipeParser.encode(message);
        } catch (HL7Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Die Nachricht kann nicht geparsed werden.");
                alert.showAndWait();
            });
        }
        return null;
    }
 }