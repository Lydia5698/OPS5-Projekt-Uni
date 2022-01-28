package controller;

import ExternalFiles.Converter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jooq.tables.pojos.MedPersonal;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;

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
    private Button btnLogIn;
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
     * Show all employees in a combobox by their Personal-ID and their name
     */
    private void setEmployeeId() {
        Converter.setMitarbeiter(employeeId, true, 0);
    }

    /**
     * Returns the person which is currently logged in
     *
     * @return Personal-ID from the user
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
        Main.logger.info("New Login Window!");
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

    /**
     * Is only used once at the beginning of the application so a medpersonal have to log in first before he can
     * serve the gui
     */
    public void pressButton(){
        btnLogIn.fire();
    }

}
