package controller;

import ExternalFiles.Converter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jooq.tables.pojos.MedPersonal;
import main.Main;
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
    private PasswordField passwort;
    @FXML
    private Label message;
    @FXML
    private Button schliessen;

    @FXML
    public void initialize() {
        Converter.setMitarbeiter(mitarbeiter, true, 0);
    }

    /**
     * Logs in the new selected user
     */
    public void loginNewEmployee() {
        if (!passwort.getText().equals("OPS5")) {
            message.setText("Falsches Passwort");
            message.setTextFill(Color.rgb(210,39,30));
        }
        else{
            passwort.clear();
            message.setText("");
            MainController.setEmployee(mitarbeiter.getSelectionModel().getSelectedIndex());
            Stage stage = (Stage) login.getScene().getWindow();
            stage.close();
        }

    }

    /**
     * If the button was pressed, the application closes (System.exit() cause it is also called in the stop method from Main)
     * @param actionEvent
     */
    public void closeApplication(ActionEvent actionEvent) {
        System.exit(0);
    }
}