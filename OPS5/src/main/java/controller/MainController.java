package controller;

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
import javafx.util.Callback;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import main.Main;

import java.io.IOException;

public class MainController {

	private Parent root;

	/**
	 * Singleton instance of MainController, access via getInstance() method.
	 * Is always initialized by the Main classes init method if the application is started via that class (which it should always be).
	 */
	private static MainController instance;

	// UI Elements
	@FXML
	private Label systemMessage;
	@FXML
	private ComboBox<MedPersonal> employeeId;
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
	public void initialize(){
		setEmployeeId();
	}

	/**
	 * Use this Method to access the current Instance of the Main Controller from
	 * anywhere via MainController.getInstance(). This allows accessing all nested
	 * Controllers and their fields!
	 *
	 * @return the instance of the MainController as initialized by the FXMLLoader
	 *         in main.
	 */
	public static MainController getInstance() {
		return instance;
	}

	/**
	 * Is called once from the Main class to initialize the Instance! Do not use
	 * this Method unless you know exactly what you are doing!
	 * @param instance
	 *            the instance to set
	 */
	public static void setInstance(MainController instance) {
		MainController.instance = instance;
	}

	/**
	 * show all employees in a combobox by their persid and their name
	 */
	private void setEmployeeId(){
		createEmployeeComboBox(employeeId,0);
	}

	/**
	 * returns the person which is currently logged in
	 * @return persid from the user
	 */
	public static String getUserId(){
		return getInstance().employeeId.getValue().getPersId();
	}

	/**
	 * clears the selection of the employee and opens a new window to log in
	 * @param actionEvent
	 */
	public void log_out(ActionEvent actionEvent) {
		employeeId.getSelectionModel().clearSelection();
		System.out.println("New Login Window!");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/fxml/PaneLogIn.fxml"));
			root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Login");
			stage.setScene(new Scene(root));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(
					((Node)actionEvent.getSource()).getScene().getWindow() );
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * sets all employees into a combobox
	 * @param employee the combobox
	 * @param i the index of the selected employee
	 */
	public static void createEmployeeComboBox(ComboBox<MedPersonal> employee, int i){
		Callback<ListView<MedPersonal>, ListCell<MedPersonal>> cellFactory = new Callback<>() {
			@Override
			public ListCell<MedPersonal> call(ListView<MedPersonal> medPersonalListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(MedPersonal med, boolean empty) {
						super.updateItem(med, empty);
						if (med == null || empty) {
							setGraphic(null);
						} else {
							setText(med.getPersId() + " : " + med.getNachnameVorname());
						}
					}
				};
			}
		};
		employee.setButtonCell(cellFactory.call(null));
		employee.setCellFactory(cellFactory);
		employee.getItems().setAll(new MedPersonalDao(Main.configuration).findAll());
		employee.getSelectionModel().select(i);
	}

	/**
	 * !!! only used by LogOutController when nobody is logedin!!!
	 * @param i index of selected employee in log in window
	 */
	public static void setEmployee(int i){
		createEmployeeComboBox(instance.employeeId, i);
	}
}
