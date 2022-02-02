package controller;

import ExternalFiles.Converter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import main.Main;

import java.sql.Timestamp;
import java.util.List;

import jooq.tables.pojos.RolleSt;
import jooq.tables.pojos.MedPersonal;
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
     * It calls the methods for setting the values of the combobox
     */
    @FXML
    public void initialize() {
        Main.logger.info("Initialize Rolle-Tab!");
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
     * It sets all operations of the database as choosing options of the combobox.
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
            Main.logger.warning("Fehlende Einträge: Es muss ein Mitarbeiter ausgewählt werden.");
            alert.setContentText("Es muss ein Mitarbeiter ausgewählt werden!");
            alert.showAndWait();
        }
        else if(op.getValue()==null){
            Main.logger.warning("Fehlende Einträge: Es muss eine Op ausgewählt werden.");
            alert.setContentText("Es muss eine Op ausgewählt werden!");
            alert.showAndWait();
        }
        else if(role.getValue()==null){
            Main.logger.warning("Fehlende Einträge: Es muss eine Rolle ausgewählt werden.");
            alert.setContentText("Es muss eine Rolle ausgewählt werden!");
            alert.showAndWait();
        }
        else{
            Rolle insertRole = new Rolle(
                    op.getSelectionModel().getSelectedItem().getOpId(), //opId
                    null, //bearbeiter
                    role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
                    null, //bearbeiterZeit
                    new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //ersteller Zeit
                    MainController.getUserId(), //ersteller
                    mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonal Personal-ID
                    false //storniert
            );
            RolleDao roleDao = new RolleDao(Main.configuration);
            // test if role with this operation and medical_user is already in the database
            List<Rolle> allRoles = roleDao.findAll();
            boolean roleExists = false;
            for (Rolle r : allRoles){
                if (r.getOpId() == insertRole.getOpId() && r.getMedPersonalPersId().equals(insertRole.getMedPersonalPersId())){
                    roleExists = true;
                }
            }
            if(roleExists){
                Main.logger.warning("Es existiert bereits ein Rollen-Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter.");
                alert.setHeaderText("Eintrag mit diesem Schlüssel bereits vorhanden.");
                alert.setContentText("Es existiert bereits ein Eintrag mit dieser Schlüsselkombination aus Op-Id und Mitarbeiter. " +
                        "Zum Bearbeiten der Rolle wechseln Sie bitte in den Übersichts-Tab.");
                alert.showAndWait();
            }
            else {
                roleDao.insert(insertRole);
                Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
                Alert confirm = new Alert(AlertType.INFORMATION);
                confirm.setTitle("Information");
                confirm.setHeaderText("Erfolgreich eingefügt");
                confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
                confirm.showAndWait();
            }
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
    }

}

