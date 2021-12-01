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
import java.time.LocalDateTime;
import java.sql.Timestamp;

import static jooq.Tables.*;

import jooq.tables.pojos.RolleSt;
import jooq.tables.daos.RolleStDao;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Operation;
import jooq.tables.daos.RolleDao;
import jooq.tables.pojos.Rolle;
import jooq.Tables;

/**
 * The RoleController is responsible for creating a new role.
 */
public class RoleController{

    @FXML
    private ComboBox<MedPersonal> mitarbeiter;
    @FXML
    private ComboBox<RolleSt> role;
    @FXML
    private ComboBox<Operation> op;

    /**
     * This method is called when the window is created.
     * It calls the methods for setting the values of the comboboxes
     */
    @FXML
    public void initialize() {
        System.out.println("Initialize Role-Tab!");

        setRole();
        setOp();
        setMitarbeiter();

    }

    /**
     * This method is called when initialising the window.
     * It sets all role types of the database as choosing options of the combobox.
     */
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

    /**
     * This method is called when initialising the window.
     * It sets all operaitions of the database as choosing options of the combobox.
     */
    private void setOp() {
        Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Operation> call(ListView<Operation> opListView) {
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

    /**
     * This method is called when initialising the window.
     * It sets all medical users of the database as choosing options of the combobox.
     */
    //TODO: Nach Namen sortieren.
    private void setMitarbeiter() {
        Callback<ListView<MedPersonal>, ListCell<MedPersonal>> cellFactory = new Callback<>() {
            @Override
            public ListCell<MedPersonal> call(ListView<MedPersonal> userListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MedPersonal user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(user.getNachnameVorname() + " " + user.getPersId());
                        }
                    }
                };
            }
        };
        mitarbeiter.setButtonCell(cellFactory.call(null));
        mitarbeiter.setCellFactory(cellFactory);
        mitarbeiter.getItems().setAll(new MedPersonalDao(Main.configuration).findAll());
    }

    /**
     * This method is called when the save-button is pushed.
     * It checks that every attribute is selected and created a new role that gets inserted into the database.
     */
    @FXML
    void createRole() {
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
        else{
            Rolle insertRole = new Rolle(
                    op.getSelectionModel().getSelectedItem().getOpId(), //opId
                    null, //bearbeiter
                    role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
                    null, //bearbeiterZeit
                    new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //erstellZeit
                    MainController.getUserId(), //ersteller
                    mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonalPersId
                    false //storniert
            );
            RolleDao roleDao = new RolleDao(Main.configuration);
            roleDao.insert(insertRole);
            Alert confirm = new Alert(AlertType.INFORMATION);
            confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
            confirm.showAndWait();
        }
        System.out.println("Creating role!");
    }

}
