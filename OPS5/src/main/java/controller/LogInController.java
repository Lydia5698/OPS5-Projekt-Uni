package controller;

import ExternalFiles.Converter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jooq.tables.pojos.MedPersonal;
import org.controlsfx.control.SearchableComboBox;

/**
 * Ensures that one person is always logged in to edit.
 */
public class LogInController {

    @FXML
    private SearchableComboBox<MedPersonal> mitarbeiter;
    @FXML
    private Button login;

    @FXML
    public void initialize() {
        Converter.setMitarbeiter(mitarbeiter, true, 0);
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