package controller;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import main.Main;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;


import jooq.tables.pojos.RolleSt;
import jooq.tables.daos.RolleStDao;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.daos.OperationDao;
import jooq.tables.pojos.Operation;
import jooq.tables.daos.RolleDao;
import jooq.tables.pojos.Rolle;
import org.controlsfx.control.SearchableComboBox;

/**
 * The RoleController is responsible for creating a new role.
 */
public class RoleController{

    @FXML
    private SearchableComboBox<MedPersonal> mitarbeiter;
    @FXML
    private SearchableComboBox<RolleSt> role;
    @FXML
    private SearchableComboBox<Operation> op;

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
        Converter.setRolle(role);
    }

    /**
     * This method is called when initialising the window.
     * It sets all operaitions of the database as choosing options of the combobox.
     */
    private void setOp() {
        Converter.setOperation(op, "role");
    }

    /**
     * This method is called when initialising the window.
     * It sets all medical users of the database as choosing options of the combobox.
     */
    private void setMitarbeiter() {
        Converter.setMitarbeiter(mitarbeiter, false, 0);
    }

    /**
     * This method is called when the save-button is pushed.
     * It checks that every attribute is selected and created a new role that gets inserted into the database.
     */
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
            confirm.setTitle("Information");
            confirm.setHeaderText("Erfolgreich eingefügt");
            confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
            confirm.showAndWait();
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
        System.out.println("Creating role!");
    }

}

