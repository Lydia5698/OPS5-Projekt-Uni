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
        if(pid.getDateTimeOfBirth().getTime().getValue() != null){
            patient.setGeburtsdatum(DateTimeFormatter.BASIC_ISO_DATE.parse(pid.getDateTimeOfBirth().getTime().getValue(), LocalDate::from));
        }
        patient.setGeschlecht(Converter.SexFromISSToOurConverter(pid.getAdministrativeSex().getValue()));
        patient.setStorniert(false);
        patient.setNotfall(false);
        patient.setBlutgruppe("nb.");
        patient.setGeburtsort(pid.getBirthPlace().getValue());
        String straße = pid.getPatientAddress(0).getStreetAddress().getStreetName().getValue();
        String number = pid.getPatientAddress(0).getStreetAddress().getDwellingNumber().getValue();
        if(straße != null){
            String patientsadress = straße;
            if(number != null){
                patientsadress = patientsadress + " " + number;
            }
            patient.setStrasse(patientsadress);
        }
        patient.setPostleitzahl(pid.getPatientAddress(0).getXad5_ZipOrPostalCode().getValue());
        patient.setTelefonnummer(pid.getPhoneNumberHome(0).getTelephoneNumber().getValue());
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
        fall.setFallId(Integer.parseInt(pv1.getVisitNumber().getCx1_IDNumber().getValue()));
        fall.setPatId(Integer.parseInt(pid.getPatientID().getCx1_IDNumber().getValue()));
        if(pv1.getPatientClass().getValue() != null){fall.setFallTyp(pv1.getPatientClass().getValue().equals("I") ? 1 : 2);}
        fall.setAufnahmedatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getAdmitDateTime().getTime().getValue())));
        if(pv1.getDischargeDateTime(0).getTime().getValue() != null){fall.setEntlassungsdatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getDischargeDateTime(0).getTime().getValue())));}
        fall.setStationSt(pv1.getAssignedPatientLocation().getPl1_PointOfCare().getValue());
        fall.setStorniert(false);
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
                List<DG1> dg1List = adtMsg.getDG1All();
                for (DG1 dg1 : dg1List) {
                    Diagnose diagnose = new Diagnose();
                    diagnose.setDiagnoseId(Integer.parseInt(dg1.getDg11_SetIDDG1().getValue()));
                    diagnose.setDiagnosetyp(1);
                    diagnose.setIcd10Code(dg1.getDiagnosisCodeDG1().getCe1_Identifier().getValue());
                    diagnose.setKlartextDiagnose(dg1.getDiagnosisDescription().getValue());
                    diagnose.setStorniert(false);
                    diagnoses.add(diagnose);
                }
                return diagnoses;
             }
        } catch(HL7Exception e){
                Platform.runLater(()->{
                    Main.logger.warning("Die Nachricht kann nicht umgewandelt werden.");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Die Nachricht kann nicht umgewandelt werden.");
                    alert.showAndWait();
                });
                return null;
        }
    }


    /**
     * Checks if an adt01 message contains diagnosis
     * @param message the sent message
     * @return ture, if the message contains a diagnosis
     */
    public static boolean a01WithDiagnosis(Message message){
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
                Main.logger.warning("Es kann keine Nachricht erstellt werden.");
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
            if(patient.getGeburtsdatum() != null){pid.getDateTimeOfBirth().getTime().setValue(patient.getGeburtsdatum().format(DateTimeFormatter.ofPattern("yyyyMMdd")));}
            pid.getAdministrativeSex().setValue(Converter.IssSexConverter(patient.getGeschlecht()));
            pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(patient.getStrasse());
            pid.getPatientAddress(0).getZipOrPostalCode().setValue(patient.getPostleitzahl());
            pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(patient.getTelefonnummer());
            pid.getBirthPlace().setValue(patient.getGeburtsort());

            //pv1
            PV1 pv1 = bar05.getVISIT().getPV1();
            if (fall.getFallTyp() != null) {
                pv1.getPatientClass().setValue(Converter.fallTypConverter(fall.getFallTyp()).equals("stationär") ? "I" : "O");
            }
            pv1.getAdmitDateTime().getTime().setValue(fall.getAufnahmedatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            assert medPersonal != null;
            pv1.getAdmittingDoctor(0).getIDNumber().setValue(medPersonal.getPersId());
            pv1.getAdmittingDoctor(0).getPrefixEgDR().setValue(medPersonal.getAnrede());
            pv1.getAdmittingDoctor(0).getDegreeEgMD().setValue(medPersonal.getTitel());
            pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(medPersonal.getName());
            pv1.getAdmittingDoctor(0).getGivenName().setValue(medPersonal.getVorname());
            pv1.getVisitNumber().getCx1_IDNumber().setValue(fall.getFallId().toString());
            pv1.getAdmissionType().setValue(patient.getNotfall() ? "E" : "R");
            if (fall.getEntlassungsdatum() != null) {
                pv1.getDischargeDateTime(0).getTime().setValue(fall.getEntlassungsdatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            }
            pv1.getAssignedPatientLocation().getPointOfCare().setValue(fall.getStationSt());


            //DG1 fields
            List<Diagnose> diagnose = new DiagnoseDao(Main.configuration).fetchByOpId(operation.getOpId());
            for (int i = 0; i < diagnose.size(); i++) {
                MedPersonal med = new MedPersonalDao(Main.configuration).fetchOneByPersId(diagnose.get(i).getErsteller());
                DG1 dg1 = bar05.getVISIT().getDG1(i);
                dg1.getSetIDDG1().setValue(diagnose.get(i).getDiagnoseId().toString());
                dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(diagnose.get(i).getIcd10Code());
                dg1.getDiagnosisDescription().setValue(diagnose.get(i).getKlartextDiagnose().replaceAll("\n", "").replaceAll("\r",""));
                dg1.getDiagnosisDateTime().getTime().setValue(diagnose.get(i).getDatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                dg1.getDiagnosingClinician(0).getIDNumber().setValue(med.getPersId());
                dg1.getDiagnosingClinician(0).getFamilyName().getSurname().setValue(med.getName());
                dg1.getDiagnosingClinician(0).getGivenName().setValue(med.getVorname());
                dg1.getDiagnosingClinician(0).getPrefixEgDR().setValue(med.getAnrede());
                dg1.getDiagnosingClinician(0).getDegreeEgMD().setValue(med.getTitel());
            }

            //PR1 fields
            List<Prozedur> prozedurs = new ProzedurDao(Main.configuration).fetchByOpId(operation.getOpId());
            for (int i = 0; i < prozedurs.size(); i++) {
                MedPersonal medProz = new MedPersonalDao(Main.configuration).fetchOneByPersId(prozedurs.get(i).getErsteller());
                PR1 pr1 = bar05.getVISIT().getPROCEDURE(i).getPR1();
                pr1.getSetIDPR1().setValue(prozedurs.get(i).getProzId().toString());
                pr1.getProcedureCode().getIdentifier().setValue(prozedurs.get(i).getOpsCode());
                pr1.getProcedureDescription().setValue(prozedurs.get(i).getAnmerkung().replaceAll("\n", "").replaceAll("\r",""));
                pr1.getProcedureDateTime().getTime().setValue(prozedurs.get(i).getErstellZeit().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                pr1.getSurgeon(0).getIDNumber().setValue(prozedurs.get(i).getErsteller());
                pr1.getSurgeon(0).getFamilyName().getSurname().setValue(medProz.getName());
                pr1.getSurgeon(0).getGivenName().setValue(medProz.getVorname());
                pr1.getSurgeon(0).getPrefixEgDR().setValue(medProz.getAnrede());
                pr1.getSurgeon(0).getDegreeEgMD().setValue(medProz.getTitel());
            }

            //rolle
            List<Rolle> rolles = new RolleDao(Main.configuration).fetchByOpId(operation.getOpId());
            for (int i = 0; i < rolles.size(); i++){
                ROL rol = bar05.getROL(i);
                MedPersonal medPersonal1 = new MedPersonalDao(Main.configuration).fetchOneByPersId(rolles.get(i).getMedPersonalPersId());
                rol.getRol3_RoleROL().getIdentifier().setValue(Converter.roleConverter(rolles.get(i).getRolleSt()));
                rol.getRol4_RolePerson(0).getIDNumber().setValue(medPersonal1.getPersId());
                rol.getRol4_RolePerson(0).getGivenName().setValue(medPersonal1.getVorname());
                rol.getRol4_RolePerson(0).getFamilyName().getSurname().setValue(medPersonal1.getName());
                rol.getRol4_RolePerson(0).getPrefixEgDR().setValue(medPersonal1.getAnrede());
                rol.getRol4_RolePerson(0).getDegreeEgMD().setValue(medPersonal1.getTitel());
            }

        } catch(HL7Exception e){
            Platform.runLater(()->{
                Main.logger.warning("Es kann keine Nachricht aus den eingegebenen Daten geparsed werden.");
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
                Main.logger.warning("Die Nachricht kann nicht geparsed werden.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Die Nachricht kann nicht geparsed werden.");
                alert.showAndWait();
            });
        }
        return null;
    }
 }