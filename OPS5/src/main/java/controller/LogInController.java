package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jooq.tables.pojos.MedPersonal;

public class LogInController {

    @FXML
    private ComboBox<MedPersonal> mitarbeiter;
    @FXML
    private Button login;

    @FXML
    public void initialize() {
        MainController.createEmployeeComboBox(mitarbeiter,1);
    }

    public void loginNewEmployee(ActionEvent actionEvent) {
        MainController.setEmployee(mitarbeiter.getSelectionModel().getSelectedIndex());
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
    }
}