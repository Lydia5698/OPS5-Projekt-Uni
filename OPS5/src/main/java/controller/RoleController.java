package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import main.Main;
import org.jooq.impl.DSL;
import org.jooq.Record1;
import org.jooq.Result;

import java.util.List;

import static jooq.Tables.*;

//import jooq.tables.daos.RolleDao;
//import jooq.tables.pojos.Rolle;
import jooq.tables.pojos.RolleSt;
import jooq.tables.daos.RolleStDao;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Operation;
import jooq.Tables;

//import controller.AdmissionController;
//import controller.OPController;

public class RoleController{

    @FXML
    private ComboBox<MedPersonal> mitarbeiter;
    @FXML
    private ComboBox<RolleSt> role;
    @FXML
    private ComboBox<Operation> op;

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

        setRole();
        setOp();

        /*Result<Record1<String>> result = Main.dslContext.select(Tables.MED_PERSONAL.NACHNAME_VORNAME.as("fullname"))
                .from(Tables.MED_PERSONAL)
                .orderBy(Tables.MED_PERSONAL.NACHNAME_VORNAME.asc())
                .fetch();
        List<String> userlist = result.map(record -> record.getValue("fullname").toString());
        mitarbeiter.getItems().setAll(userlist);*/

    }

    private void setRole() {
        Callback<ListView<RolleSt>, ListCell<RolleSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<RolleSt> call(ListView<RolleSt> rolleListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(RolleSt ro, boolean empty) {
                        super.updateItem(ro, empty);
                        if (ro == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(ro.getBezeichnung());
                        }
                    }
                };
            }
        };
        role.setButtonCell(cellFactory.call(null));
        role.setCellFactory(cellFactory);
        role.getItems().setAll(new RolleStDao(Main.configuration).findAll());
    }

    private void setOp() {
        Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Operation> call(ListView<Operation> rolleListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Operation oper, boolean empty) {
                        super.updateItem(oper, empty);
                        if (oper == null || empty) {
                            setGraphic(null);
                        } else {
                            setText("OP: " + oper.getOpId() + ", Fall: " + oper.getFallId() + ", Datum: " + oper.getBeginn());
                        }
                    }
                };
            }
        };
        op.setButtonCell(cellFactory.call(null));
        op.setCellFactory(cellFactory);
        op.getItems().setAll(new OperationDao(Main.configuration).findAll());
    }

    private void setMitarbeiter() {
        Callback<ListView<MedPersonal>, ListCell<MedPersonal>> cellFactory = new Callback<>() {
            @Override
            public ListCell<MedPersonal> call(ListView<MedPersonal> rolleListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MedPersonal user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(user.getPersId() + " " + user.getNachnameVorname());
                        }
                    }
                };
            }
        };
        mitarbeiter.setButtonCell(cellFactory.call(null));
        mitarbeiter.setCellFactory(cellFactory);
        mitarbeiter.getItems().setAll(new MedPersonalDao(Main.configuration).findAll());
    }

    @FXML
    void createRole(ActionEvent event) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Fehlende Einträge!");
        if(mitarbeiter.getValue()==null){
            alert.setContentText("Es muss ein Mitarbeiter ausgewählt werden!");
            alert.showAndWait();
        }
        else if(op.getValue()==null){
            alert.setContentText("Es muss eine Op ausgewählt werden!");
            alert.showAndWait();
        }
        else if(role.getValue()==null){
            alert.setContentText("Es muss eine Rolle ausgewählt werden!");
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

