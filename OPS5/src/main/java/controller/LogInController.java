package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jooq.tables.pojos.MedPersonal;

/**
 * Ensures that one person is always logged in to edit.
 */
public class LogInController {

    @FXML
    private ComboBox<MedPersonal> mitarbeiter;
    @FXML
    private Button login;

    @FXML
    public void initialize() {
        MainController.createEmployeeComboBox(mitarbeiter,1);
    }

    /**
     * Logs in the new selected user
     */
    public void loginNewEmployee() {
        MainController.setEmployee(mitarbeiter.getSelectionModel().getSelectedIndex());
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
    }
}