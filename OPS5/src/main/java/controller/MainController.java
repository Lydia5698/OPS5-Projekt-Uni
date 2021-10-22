package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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
	private ComboBox<Integer> employeeId;
	@FXML
	private Button btnLogout;
	// Controllers
	@FXML
	private AdmissionController admissionController;
	@FXML
	private PatientController patientController;
	@FXML
	private CaseController fallIdController;
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
	 * 
	 * @param instance
	 *            the instance to set
	 */
	public static void setInstance(MainController instance) {
		MainController.instance = instance;
	}

}
