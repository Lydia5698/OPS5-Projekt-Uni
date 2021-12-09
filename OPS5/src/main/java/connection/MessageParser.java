package connection;


import ExternalFiles.Converter;
import ca.uhn.hl7v2.model.v251.message.BAR_P05;
import ca.uhn.hl7v2.model.v251.segment.*;
import ca.uhn.hl7v2.parser.PipeParser;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        //neuer patient wird zugefügt, deshalb keine id, durch autoincrement
        patient.setName(pid.getPatientName(0).getFamilyName().getSurname().getValue());
        patient.setVorname(pid.getPatientName(0).getGivenName().getValue());
        System.out.println(DateTimeFormatter.BASIC_ISO_DATE.parse(pid.getDateTimeOfBirth().getTime().getValue(), LocalDate::from));
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
        //fallid nicht setzen, durchAutoincrement
        //patid wird in dem server gesetzt von dem patienten der in dern hl7 mitgesendet wurde
        fall.setFallTyp(pv1.getPatientClass().getValue().equals("Inpatient") ? 1 : 2);
        fall.setPatId(Integer.parseInt(pid.getPatientID().getCx1_IDNumber().getValue()));
        fall.setAufnahmedatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getAdmitDateTime().getTime().getValue())));
        if(pv1.getDischargeDateTime(0).getTime().getValue() != null){fall.setEntlassungsdatum(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").parse(pv1.getDischargeDateTime(0).getTime().getValue())));}
        fall.setStationSt(pv1.getAssignedPatientLocation().getPl1_PointOfCare().getValue());
        fall.setStorniert(false);
        fall.setErsteller(pv1.getAdmittingDoctor(0).getIDNumber().getValue());
        fall.setErstellZeit(LocalDateTime.now());
        return fall;
    }

    /**
     * This method parses an new operation into a message
     * @param operation operation which should be casted into a message
     * @return the message of the new operation
     */
    public static Message parseBar05(Operation operation) throws HL7Exception, IOException {
        BAR_P05 bar05 = new BAR_P05();
        bar05.initQuickstart("BAR","P05", "P");

        Fall fall = new FallDao(Main.configuration).findById(operation.getFallId());
        Patient patient = new PatientDao(Main.configuration).findById(fall.getPatId());
        MedPersonal medPersonal = new MedPersonalDao(Main.configuration).findById(fall.getErsteller());

        //msh
        MSH msh = bar05.getMSH();
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
        //TODO Blutgruppe
        pid.getAdministrativeSex().setValue(Converter.IssSexConverter(patient.getGeschlecht()));
        pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(patient.getStrasse());
        pid.getPatientAddress(0).getZipOrPostalCode().setValue(patient.getPostleitzahl());
        pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(patient.getTelefonnummer());
        pid.getBirthPlace().setValue(patient.getGeburtsort());

        //pv1
        PV1 pv1 = bar05.getVISIT().getPV1();
        pv1.getSetIDPV1().setValue(operation.getFallId().toString());
        if(fall.getFallTyp() != null){pv1.getPatientClass().setValue(Converter.fallTypConverter(fall.getFallTyp()).equals("stationär") ? "Inpatient" : "Outpatient");}
        pv1.getAdmitDateTime().getTime().setValue(fall.getAufnahmedatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        assert medPersonal != null;
        pv1.getAdmittingDoctor(0).getIDNumber().setValue(medPersonal.getPersId());
        pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(medPersonal.getName());
        pv1.getAdmittingDoctor(0).getGivenName().setValue(medPersonal.getVorname());
        pv1.getVisitNumber().getCx1_IDNumber().setValue(fall.getFallId().toString());
        if(fall.getEntlassungsdatum() != null){pv1.getDischargeDateTime(0).getTime().setValue(fall.getEntlassungsdatum().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));}
        pv1.getAssignedPatientLocation().getPointOfCare().setValue(fall.getStationSt());


        //DG1 fields
        List<Diagnose> diagnose = new DiagnoseDao(Main.configuration).fetchByOpId(operation.getOpId());
        for(int i = 0; i < diagnose.size(); i++){
            DG1 dg1 = bar05.getVISIT().getDG1(i);
            dg1.getSetIDDG1().setValue(diagnose.get(i).getDiagnoseId().toString());
            dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(diagnose.get(i).getIcd10Code());
            dg1.getDiagnosisDescription().setValue(diagnose.get(i).getKlartextDiagnose());
            dg1.getDiagnosisDateTime().getTime().setValue(diagnose.get(i).getErstellZeit().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            dg1.getDiagnosingClinician(0).getIDNumber().setValue(diagnose.get(i).getErsteller());
        }

        //PR1 fields
       List<Prozedur> prozedurs = new ProzedurDao(Main.configuration).fetchByOpId(operation.getOpId());
        for(int i = 0; i < prozedurs.size(); i++){
            PR1 pr1 = bar05.getVISIT(0).getPROCEDURE().getPR1();
            pr1.getSetIDPR1().setValue(prozedurs.get(i).getProzId().toString());
            pr1.getProcedureCode().getIdentifier().setValue(prozedurs.get(i).getOpsCode());
            pr1.getProcedureDescription().setValue(prozedurs.get(i).getAnmerkung());
            pr1.getProcedureDateTime().getTime().setValue(prozedurs.get(i).getErstellZeit().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            pr1.getSurgeon(0).getIDNumber().setValue(prozedurs.get(i).getErsteller());
        }
       return bar05;
    }

    /**
     * This method converts a message to a string so it can be displayed in the tableview in the communicationcontroller
     * @param message the incomming/outgoing message
     * @return the string of the hl7 messagr
     * @throws HL7Exception is thrown if the message can not be encoded
     */
    public static String messageToString(Message message) throws HL7Exception {
        return pipeParser.encode(message);
    }
 }

    /**
     * this method parses a patient into a message
     * @param patient patient which should be casted into a message
     * @return message of the new patient
     * @throws HL7Exception cause we try to set values

    public static Message parseBar05Patient(Patient patient) throws HL7Exception {
        BAR_P05 bar05 = new BAR_P05();

        //msh
        MSH msh = bar05.getMSH();
        msh.getDateTimeOfMessage().getTime().setValue(LocalDateTime.now().toString());
        msh.getSendingApplication().getNamespaceID().setValue("OPS");
        msh.getReceivingApplication().getNamespaceID().setValue("KIS");
        msh.getMessageType().getMsg1_MessageCode().setValue("BAR");
        msh.getMessageType().getMsg2_TriggerEvent().setValue("P05");
        msh.getMessageType().getMsg3_MessageStructure().setValue("BAR_P05");

        //pid
        PID pid = bar05.getPID();
        pid.getPatientID().getCx1_IDNumber().setValue(patient.getPatId().toString());//TODO wie wollen wir id umsetzen?
        pid.getPatientName(0).getFamilyName().getSurname().setValue(patient.getName());
        pid.getPatientName(0).getGivenName().setValue(patient.getVorname());
        pid.getDateTimeOfBirth().getTime().setValue(patient.getGeburtsdatum().toString());
        //TODO Blutgruppe
        pid.getAdministrativeSex().setValue(Converter.IssSexConverter(patient.getGeschlecht()));
        pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(patient.getStrasse());
        pid.getPatientAddress(0).getZipOrPostalCode().setValue(patient.getPostleitzahl());
        pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(patient.getTelefonnummer());
        pid.getBirthPlace().setValue(patient.getGeburtsort());

        return bar05;
    }*/