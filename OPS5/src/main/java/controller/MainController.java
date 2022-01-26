package controller;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    /**
     * Singleton instance of MainController, access via getInstance() method.
     * Is always initialized by the Main classes init method if the application is started via that class (which it should always be).
     */
    private static MainController instance;

    // UI Elements
    @FXML
    private Label systemMessage;
    @FXML
    private SearchableComboBox<MedPersonal> employeeId;
    @FXML
    private Button btnLogout;
    // Controllers
    @FXML
    private AdmissionController admissionController;
    @FXML
    private PatientController patientController;

    @FXML
    private OPController opController;
    @FXML
    private OverviewController opListeIdController;
    @FXML
    private DiagnosisController diagnoseIdController;
    @FXML
    private ProcedureController prozedurIdController;
    @FXML
    private CommunicationsController commTabController;

    @FXML
    public void initialize() {
        setEmployeeId();
    }

    /**
     * Use this Method to access the current Instance of the Main Controller from
     * anywhere via MainController.getInstance(). This allows accessing all nested
     * Controllers and their fields!
     *
     * @return The instance of the MainController as initialized by the FXMLLoader
     * in main.
     */
    public static MainController getInstance() {
        return instance;
    }

    /**
     * Is called once from the Main class to initialize the Instance! Do not use
     * this Method unless you know exactly what you are doing!
     *
     * @param instance The instance to set
     */
    public static void setInstance(MainController instance) {
        MainController.instance = instance;
    }

    /**
     * Show all employees in a combobox by their Pers-ID and their name
     */
    private void setEmployeeId() {
        Converter.setMitarbeiter(employeeId, true, 0);
    }

    /**
     * Returns the person which is currently logged in
     *
     * @return Pers-ID from the user
     */
    public static String getUserId() {
        return getInstance().employeeId.getValue().getPersId();
    }

    /**
     * Clears the selection of the employee and opens a new window to log in
     *
     * @param actionEvent Of the logout Button
     */
    public void log_out(ActionEvent actionEvent) {
        employeeId.getSelectionModel().clearSelection();
        System.out.println("New Login Window!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/PaneLogIn.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * !!! Only used by LogInController when nobody is logged in!!!
     *
     * @param i Index of selected employee in log in window
     */
    public static void setEmployee(int i) {
        Converter.setMitarbeiter(instance.employeeId, true, i);
    }

    public AdmissionController getAdmissionController() {
        return admissionController;
    }

    public CommunicationsController getCommTabController() {
        return commTabController;
    }

}
