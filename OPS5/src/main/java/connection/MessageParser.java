package connection;


import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.BAR_P05;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import jooq.tables.pojos.Patient;
import main.Main;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.segment.EVN;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

import java.time.LocalDateTime;


public class MessageParser {

    public static Patient parseA01(String a01Message){
        try {
            Message hapiMsg = Main.p.parse(a01Message);
            ADT_A01 adtMsg = (ADT_A01)hapiMsg;

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

            //TODO erstellzeitpunk = aufnahmezeitpunkt oder Messagezeitpunkt?
            return patient;

        } catch (EncodingNotSupportedException e) {
            e.printStackTrace();
            return null;
        } catch (HL7Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseBar05() throws HL7Exception {
        BAR_P05 bar05 = new BAR_P05();

        //msh
        MSH msh = bar05.getMSH();
        msh.getDateTimeOfMessage().getTime().setValue(LocalDateTime.now().toString());
        //TODO switch case from edit ?


        return Main.p.encode(bar05);
    }
 }

