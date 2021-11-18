package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FallController {

    @FXML
    private TextField patient;

    @FXML
    private TextField falltyp;

    @FXML
    private TextField station;

    @FXML
    private DatePicker aufnahmedatum;

    @FXML
    private DatePicker entlassungsdatum;

    @FXML
    private Button speicherbutton;


    @FXML
    public void initialize() {
        System.out.println("Initialize Fall-Tab!");
    }

    public void createFall(ActionEvent actionEvent) {
        System.out.println("Creating Fall!");
    }
}
