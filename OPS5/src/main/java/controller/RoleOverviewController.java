package controller;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import jooq.tables.daos.RolleDao;
import jooq.tables.daos.RolleStDao;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.pojos.RolleSt;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Rolle;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.sql.Timestamp;

/**
 * This Controller displays the Roles. You can create a new one or edit an existing.
 */
public class RoleOverviewController {

	@FXML
	private TableView<Rolle> roleTable;
	@FXML
	private TableColumn<Rolle, String> userCol;
	@FXML
	private TableColumn<Rolle, String> roleCol;
	@FXML
	private TableColumn<Rolle, Integer> opCol;
	@FXML
	private TableColumn<Rolle, LocalDateTime> erstellzeitCol;
	@FXML
	private TableColumn<Rolle, LocalDateTime> bearbeiterzeitCol;
	@FXML
	private TableColumn<Rolle, Boolean> storniertCol;
	@FXML
	private TableColumn<Rolle, String> erstellerCol;
	@FXML
	private TableColumn<Rolle, String> bearbeiterCol;
	@FXML
	private SearchableComboBox<MedPersonal> mitarbeiter;
	@FXML
	private SearchableComboBox<RolleSt> role;
	@FXML
	private SearchableComboBox<Operation> op;


	/**
	 * This Methode initialize the TableView for the existing Roles and shows the roles, Op-IDs and medical Users.
	 */
	@FXML
	public void initialize() {
		System.out.println("Initialize Rolle-Tab!");
		initializeColumns();
		roleTable.setItems(roleView());
		setRole();
		setOp();
		setMitarbeiter();
	}


	/**
	 * Initializes all Columns from the TableView roleTable
	 */
	private void initializeColumns() {
		// create columns
		opCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
		roleCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.roleConverter(features.getValue().getRolleSt())));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
		erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
		userCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getMedPersonalPersId()));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
	}


	/**
	 * Launches when the Button Neue Rolle is pressed. It creates a new Rolle with the selected values.
	 * @param event The event of pushing the Neue Rolle Button
	 */
	@FXML
	void createNewRole(ActionEvent event) {
		System.out.println("Create new role!");
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Fehlende Einträge!");
		if(mitarbeiter.getValue()==null){
			alert.setContentText("Es muss ein Mitarbeiter ausgewählt werden!");
			alert.showAndWait();
		}
		else if(op.getValue()==null){
			alert.setContentText("Es muss eine Op ausgewählt werden!");
			alert.showAndWait();
		}
		else if(role.getValue()==null){
			alert.setContentText("Es muss eine Rolle ausgewählt werden!");
			alert.showAndWait();
		}
		else{
			Rolle insertRole = new Rolle(
					op.getSelectionModel().getSelectedItem().getOpId(), //opId
					null, //bearbeiter
					role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
					null, //bearbeiterZeit
					new Timestamp(System.currentTimeMillis()).toLocalDateTime(), //erstellZeit
					MainController.getUserId(), //ersteller
					mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonalPersId
					false //storniert
			);
			RolleDao roleDao = new RolleDao(Main.configuration);
			roleDao.insert(insertRole);
			Alert confirm = new Alert(AlertType.INFORMATION);
			confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
			confirm.showAndWait();
		}
	}


	/**
	 * Launches when the Button Speichern is pressed. It updates the selected role to the values that the user selects.
	 * If no role is selected, an alert is shown that you need to select a role before you can edit one.
	 * @param event The event of pushing the Speichern Button
	 */
	@FXML
	public void createRole(ActionEvent event){
		System.out.println("Create role!");
		if (roleTable.getSelectionModel().getSelectedItem() != null) {
			Rolle selectedRole = roleTable.getSelectionModel().getSelectedItem();
			Rolle updateRole = new Rolle(
					op.getSelectionModel().getSelectedItem().getOpId(), //opId
					MainController.getUserId(), //bearbeiter
					role.getSelectionModel().getSelectedItem().getRolle(), //rolleSt
					LocalDateTime.now(), //bearbeiterZeit
					selectedRole.getErstellZeit(), //erstellZeit
					selectedRole.getErsteller(), //ersteller
					mitarbeiter.getSelectionModel().getSelectedItem().getPersId(), //medPersonalPersId
					selectedRole.getStorniert() //storniert
			);
			RolleDao roleDao = new RolleDao(Main.configuration);
			roleDao.update(updateRole);
			Alert confirm = new Alert(AlertType.INFORMATION);
			confirm.setContentText("Der Datensatz wurde geupdated.");
			confirm.showAndWait();
		}
		else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fehlende Auswahl!");
			alert.setContentText("Es muss eine Rolle zum Bearbeiten ausgewählt werden!");
			alert.showAndWait();
		}
	}


	/**
	 * Collects all Roles from the Database and saves them in a observable Array List from Type Role pojo
	 * @return All Roles
	 */
	public static ObservableList<Rolle> roleView(){
		RolleDao roleDao = new RolleDao(Main.configuration);
		List<Rolle> role = roleDao.findAll();
		return FXCollections.observableArrayList(role);
	}


	/**
	 * Gets triggered when a Role in the TableView gets selected.
	 * It sets the values of the selected role as values of the ComboBoxes.
	 */
	@FXML
	void roleClicked() {
		roleTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() > 0) {
				if (roleTable.getSelectionModel().getSelectedItem() != null) {
					System.out.println("Role clicked!");
					Rolle editRole = roleTable.getSelectionModel().getSelectedItem();
					MedPersonal medPersX = new MedPersonalDao(Main.configuration).fetchOneByPersId(editRole.getMedPersonalPersId());
					MedPersonal medPers = new MedPersonal(medPersX) {
						@Override
						public String toString() {
							StringBuilder sb = new StringBuilder(medPersX.getNachnameVorname());
							sb.append(" ");
							sb.append(medPersX.getPersId());
							return sb.toString();
						}
					};
					Operation operationX = new OperationDao(Main.configuration).fetchOneByOpId(editRole.getOpId());
					Operation operation = new Operation(operationX) {
						@Override
						public String toString() {
							StringBuilder sb = new StringBuilder("OP: ");
							sb.append(operationX.getOpId()).append(", Fall: ").append(operationX.getFallId());
							sb.append(", Datum: ").append(operationX.getBeginn());
							return sb.toString();
						}
					};
					RolleSt roleStX = new RolleStDao(Main.configuration).fetchOneByRolle(editRole.getRolleSt());
					RolleSt roleSt = new RolleSt(roleStX) {
						@Override
						public String toString() {
							return roleStX.getBezeichnung();
						}
					};
					mitarbeiter.setValue(medPers);
					op.setValue(operation);
					role.setValue(roleSt);
				}
			}
		});
	}


	/**
	 * This method is called when initialising the window.
	 * It sets all role types of the database as choosing options of the combobox.
	 */
	private void setRole() {
		Callback<ListView<RolleSt>, ListCell<RolleSt>> cellFactory = new Callback<>() {
			@Override
			public ListCell<RolleSt> call(ListView<RolleSt> rolleListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(RolleSt ro, boolean empty) {
						super.updateItem(ro, empty);
						if (ro == null || empty) {
							setGraphic(null);
						} else {
							setText(ro.getBezeichnung());
						}
					}
				};
			}
		};
		role.setButtonCell(cellFactory.call(null));
		role.setCellFactory(cellFactory);
		role.getItems().setAll(new RolleStDao(Main.configuration).findAll());
		role.setSelectionModel(new CustomSelectionModel<>(role));
		role.valueProperty().addListener(new ChangeListener<RolleSt>() {
			@Override
			public void changed(ObservableValue<? extends RolleSt> observable, RolleSt oldValue, RolleSt newValue) {
				if(newValue == null){
					Platform.runLater(()->{
						role.setValue(oldValue);
					});
				}
			}
		});
	}

	/**
	 * This method is called when initialising the window.
	 * It sets all operations of the database as choosing options of the combobox.
	 */
	private void setOp() {
		Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
			@Override
			public ListCell<Operation> call(ListView<Operation> opListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Operation oper, boolean empty) {
						super.updateItem(oper, empty);
						if (oper == null || empty) {
							setGraphic(null);
						} else {
							setText("OP: " + oper.getOpId() + ", Fall: " + oper.getFallId() + ", Datum: " + oper.getBeginn());
						}
					}
				};
			}
		};
		op.setButtonCell(cellFactory.call(null));
		op.setCellFactory(cellFactory);
		op.getItems().setAll(new OperationDao(Main.configuration).findAll());
		op.setSelectionModel(new CustomSelectionModel<>(op));
		op.valueProperty().addListener(new ChangeListener<Operation>() {
			@Override
			public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
				if(newValue == null){
					Platform.runLater(()->{
						op.setValue(oldValue);
					});
				}
			}
		});
	}

	/**
	 * This method is called when initialising the window.
	 * It sets all medical users of the database as choosing options of the combobox.
	 */
	private void setMitarbeiter() {
		Callback<ListView<MedPersonal>, ListCell<MedPersonal>> cellFactory = new Callback<>() {
			@Override
			public ListCell<MedPersonal> call(ListView<MedPersonal> userListView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(MedPersonal user, boolean empty) {
						super.updateItem(user, empty);
						if (user == null || empty) {
							setGraphic(null);
						} else {
							setText(user.getNachnameVorname() + " " + user.getPersId());
						}
					}
				};
			}
		};
		mitarbeiter.setButtonCell(cellFactory.call(null));
		mitarbeiter.setCellFactory(cellFactory);
		List<MedPersonal> medPersonalList = new MedPersonalDao(Main.configuration).findAll();
		medPersonalList.sort(Comparator.comparing(MedPersonal::getNachnameVorname));
		var result = medPersonalList.stream().filter(medPersonal -> !medPersonal.getPersId().equals("00000000")) //KIS rausfiltern
				.collect(Collectors.toList());
		mitarbeiter.getItems().setAll(result);
		mitarbeiter.setSelectionModel(new CustomSelectionModel<>(mitarbeiter));
		mitarbeiter.valueProperty().addListener(new ChangeListener<MedPersonal>() {
			@Override
			public void changed(ObservableValue<? extends MedPersonal> observable, MedPersonal oldValue, MedPersonal newValue) {
				if(newValue == null){
					Platform.runLater(()->{
						mitarbeiter.setValue(oldValue);
					});
				}
			}
		});
	}

}
