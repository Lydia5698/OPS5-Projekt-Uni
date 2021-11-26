package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import main.Main;

public class LogInController {

    @FXML
    private ComboBox<MedPersonal> mitarbeiter;
    @FXML
    private Button login;

    @FXML
    public void initialize() {
        MainController.createEmployeeComboBox(mitarbeiter,0);
    }

    public void loginNewEmployee(ActionEvent actionEvent) {
        MainController.setEmployee(mitarbeiter.getSelectionModel().getSelectedIndex());
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
    }
}