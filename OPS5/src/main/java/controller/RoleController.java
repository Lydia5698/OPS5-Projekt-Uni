package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import main.Main;
import org.jooq.impl.DSL;
import org.jooq.Record1;
import org.jooq.Result;

import java.util.List;

import static jooq.Tables.*;

// TODO: statt Rolle mit der Stammtabelle arbeiten!
import jooq.tables.daos.RolleDao;
import jooq.tables.pojos.Rolle;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import jooq.Tables;

//import controller.AdmissionController;
//import controller.OPController;

public class RoleController{

    @FXML
    private ComboBox<String> mitarbeiter;
    @FXML
    private ComboBox<String> role;
    @FXML
    private ComboBox<String> op;

    /*public static ObservableList<Rolle> getDaoRoles(){
        RolleDao roleDao = new RolleDao(Main.configuration);
        List<Rolle> roleList = roleDao.fetchByOpId(1);
        return FXCollections.observableArrayList(roleList);
    }*/
/*
    public static ObservableList<MedPersonal> getDaoMedPersonal(){
        MedPersonalDao persDao = new MedPersonalDao(Main.condiguration);
        List<MedPersonal> persList = persDao.
    }
*/
    @FXML
    public void initialize() {
        System.out.println("Initialize Role-Tab!");
        //mitarbeiter.setItems();
        //role.setItems(getDaoRoles());

        Result<Record1<String>> resultRole = Main.dslContext.select(Tables.ROLLE_ST.BEZEICHNUNG.as("role"))
                .from(Tables.ROLLE_ST)
                .orderBy(Tables.ROLLE_ST.BEZEICHNUNG.asc())
                .fetch();
        List<String> rolelist = resultRole.map(record -> record.getValue("role").toString());
        role.getItems().setAll(rolelist);

        Result<Record1<String>> result = Main.dslContext.select(Tables.MED_PERSONAL.NACHNAME_VORNAME.as("fullname"))
                .from(Tables.MED_PERSONAL)
                .orderBy(Tables.MED_PERSONAL.NACHNAME_VORNAME.asc())
                .fetch();
        List<String> userlist = result.map(record -> record.getValue("fullname").toString());
        mitarbeiter.getItems().setAll(userlist);

        Result<Record1<Integer>> opresult = Main.dslContext.select(Tables.OPERATION.OP_ID.as("op"))
                .from(Tables.OPERATION)
                .orderBy(Tables.OPERATION.OP_ID.asc())
                .fetch();
        List<String> oplist = opresult.map(record -> record.getValue("op").toString());
        op.getItems().setAll(oplist);

    }

    @FXML
    void createRole(ActionEvent event) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Fehlende Einträge!");
        if(op.getValue()==null){
            alert.setContentText("Es muss eine OP ausgewählt werden!");
            alert.showAndWait();
        }
        else if(role.getValue()==null){
            alert.setContentText("Es muss eine Rolle ausgewählt werden!");
            alert.showAndWait();
        }
        else if(mitarbeiter.getValue()==null){
            alert.setContentText("Es muss ein Mitarbeiter ausgewählt werden!");
            alert.showAndWait();
        }
        else{ //es fehlt noch der Ersteller!
            //List<Record> records = Main.dslContext.insertInto(Tables.ROLLE, Tables.ROLLE.OP_ID, Tables.ROLLE.ROLLE_ST, Tables.ROLLE.MED_PERSONAL, Tables.ROLLE.STORNIERT)
            // .values(op, role, mitarbeiter, false)
            //System.out.println("Saved role!");
        }
//	    List<Record> records = Main.dslContext.insertInto(PATIENT, PATIENT.NAME, PATIENT.VORNAME, PATIENT.GEBURTSDATUM, PATIENT.BLUTGRUPPE,
        //              PATIENT.GESCHLECHT, PATIENT.GEBURTSORT, PATIENT.STRASSE, PATIENT.POSTLEITZAHL, PATIENT.TELEFONNUMMER).values(
        //                    getPatientLastname().getText(), getPatientFirstname().getText(), getPatientBirthdate().getValue(),
        //          getBlutgruppe().getSelectedToggle(), getSexGroup().getSelectedToggle(), getPatientBirthplace().getText(),
        //        getPatientStreet().getText(), getPatientPostcode().getText(), getPatientCellphone().getText());
        //System.out.println(getSexGroup().getSelectedToggle());
        System.out.println("Creating role!");
    }


    public ComboBox getMitarbeiter() {
        return mitarbeiter;
    }

    public ComboBox getRole() {
        return role;
    }

    }

