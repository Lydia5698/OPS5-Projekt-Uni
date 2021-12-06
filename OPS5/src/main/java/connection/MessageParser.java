package connection;


import ExternalFiles.Converter;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.BAR_P05;
import ca.uhn.hl7v2.model.v251.segment.*;
import ca.uhn.hl7v2.parser.PipeParser;
import jooq.tables.daos.FallDao;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;

import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MessageParser {

    public static PipeParser pipeParser = Main.hapiContext.getPipeParser();

    public static Patient parseA01(Message a01Message){
        ADT_A01 adtMsg = (ADT_A01) a01Message;

        MSH msh = adtMsg.getMSH();
        EVN evn = adtMsg.getEVN();
        PID pid = adtMsg.getPID();
        PV1 pv1 = adtMsg.getPV1();

        Patient patient = new Patient();
        patient.setName(pid.getPatientName(0).getFamilyName().getSurname().getValue());
        patient.setVorname(pid.getPatientName(0).getGivenName().getValue());
        patient.setGeburtsdatum(LocalDateTime.parse(pid.getDateTimeOfBirth().toString()).toLocalDate());
        //patient.setBlutgruppe();
        patient.setGeschlecht(pid.getAdministrativeSex().getValue());
        patient.setStorniert(false);
        patient.setGeburtsort(pid.getBirthPlace().getValue());
        patient.setStrasse(pid.getPatientAddress(0).getStreetAddress().toString());
        patient.setPostleitzahl(pid.getPatientAddress(0).getXad5_ZipOrPostalCode().getValue());
        patient.setTelefonnummer(pid.getPhoneNumberHome().toString());


        //patient.setErsteller();
        //patient.setErstellZeit();
        //patient.setBearbeiter();
        //patient.setBearbeiterZeit();

        //TODO wird der Fall direkt mitgesendet?
        //TODO erstellzeitpunk = aufnahmezeitpunkt oder Messagezeitpunkt?
        return patient;

    }

    /**
     * this method parses a patient into a message
     * @param patient patient which should be casted into a message
     * @return message of the new patient
     * @throws HL7Exception cause we try to set values
     */
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
    }

    /**
     * This method parses an new operation into a message
     * @param operation operation which should be casted into a message
     * @return the message of the new operation
     */
    public static Message parseBar05Operation(Operation operation) throws DataTypeException {
        BAR_P05 bar05 = new BAR_P05();

        //msh
        MSH msh = bar05.getMSH();
        msh.getDateTimeOfMessage().getTime().setValue(LocalDateTime.now().toString());
        msh.getSendingApplication().getNamespaceID().setValue("OPS");
        msh.getReceivingApplication().getNamespaceID().setValue("KIS");

        //pid
        PID pid = bar05.getPID();
        pid.getPatientID().getCx1_IDNumber().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getPatId().toString());//TODO wie wollen wir id umsetzen?
        pid.getPatientName(0).getFamilyName().getSurname().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getName());
        pid.getPatientName(0).getGivenName().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getVorname());
        pid.getDateTimeOfBirth().getTime().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getGeburtsdatum().toString());
        //TODO Blutgruppe
        pid.getAdministrativeSex().setValue(Converter.IssSexConverter(Converter.fallIdToPatientConverter(operation.getFallId()).getGeschlecht()));
        pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getStrasse());
        pid.getPatientAddress(0).getZipOrPostalCode().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getPostleitzahl());
        pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getTelefonnummer());
        pid.getBirthPlace().setValue(Converter.fallIdToPatientConverter(operation.getFallId()).getGeburtsort());

        //pv1
        PV1 pv1 = bar05.getVISIT().getPV1();
        pv1.getSetIDPV1().setValue(operation.getFallId().toString());
        //TODO wie wollen wir aufnahmeart
        pv1.getAdmissionType().setValue(Converter.fallTypConverter(new FallDao(Main.configuration).findById(operation.getFallId()).getFallTyp()));
        pv1.getAdmitDateTime().getTime().setValue(new FallDao(Main.configuration).findById(operation.getFallId()).getAufnahmedatum().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        //dg1
        DG1 dg1 = bar05.getVISIT().getDG1();


       //pr1
       PR1 pr1 = bar05.getVISIT().getPROCEDURE().getPR1();



        return bar05;
    }
 }

