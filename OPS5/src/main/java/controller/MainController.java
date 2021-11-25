package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jooq.Tables;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;

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
	private ComboBox<MedPersonal> employeeId;
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
	 * 
	 * @param instance
	 *            the instance to set
	 */
	public static void setInstance(MainController instance) {
		MainController.instance = instance;
	}
/**
	public static String getEmployeeId(){
		return employeeId.getValue().getPersId();
	}
*/
	private void setEmployeeId(){
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
		employeeId.setButtonCell(cellFactory.call(null));
		employeeId.setCellFactory(cellFactory);
		employeeId.getItems().setAll(new MedPersonalDao(Main.configuration).findAll());
		employeeId.getSelectionModel().selectFirst();
	}

	public static String getUserId(){
		return getInstance().employeeId.getValue().getPersId();
	}

}
