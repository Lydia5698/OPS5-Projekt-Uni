package connection;


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
            patient.setName(pid.getPid5_PatientName(0).getFamilyName().getName());
            patient.setVorname(pid.getPid5_PatientName(0).getGivenName().getValue());
            patient.setGeburtsdatum(LocalDateTime.parse(pid.getPid7_DateTimeOfBirth().toString()).toLocalDate());
            //patient.setBlutgruppe();
            patient.setGeschlecht(pid.getPid8_AdministrativeSex().getValue());
            patient.setStorniert(false);
            patient.setGeburtsort(pid.getPid23_BirthPlace().getValue());
            patient.setStrasse(pid.getPid11_PatientAddress(0).getStreetAddress().toString());
            patient.setPostleitzahl(pid.getPid11_PatientAddress(0).getXad5_ZipOrPostalCode().getValue());
            patient.setTelefonnummer(pid.getPid13_PhoneNumberHome().toString());


            String msgType = msh.getMessageType().toString();
            String msgTrigger = msh.getMessageType().getTriggerEvent().getValue();
            // Prints "ADT A01"
            System.out.println(msgType + " " + msgTrigger);

            //PN patientName = adtMsg.getPID().getPatientName().toString();

            // Prints "SMITH"
            //String familyName = patientName.getFamilyName().getValue();
            //System.out.println(familyName);
        } catch (EncodingNotSupportedException e) {
            e.printStackTrace();
            return null;
        } catch (HL7Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void parseBar05(){}
 }

