package controller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jooq.tables.daos.DiagnoseDao;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.Icd10CodeStDao;
import jooq.tables.daos.ProzedurDao;
import jooq.tables.pojos.*;
import main.Main;

import javax.security.auth.callback.Callback;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DiagnosisController {
	
	@FXML
	private ComboBox<Integer> diagnosisOpId;
	@FXML
	private ComboBox<String> diagnosisIcdCode;
	@FXML
	private ComboBox<String> diagnosisType;
	@FXML
	private TextField diagnosisFreetext;	
	@FXML
	private TableView<Diagnose> diagnosisTable;
	@FXML
	private TableColumn<Diagnose, Integer> diagIDCol;

	@FXML
	private TableColumn<Diagnose, String> textCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> dateCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> erstellzeitCol;

	@FXML
	private TableColumn<Diagnose, LocalDateTime> bearbeiterzeitCol;

	@FXML
	private TableColumn<Diagnose, Byte> storniertCol;

	@FXML
	private TableColumn<Diagnose, Integer> opIDCol;

	@FXML
	private TableColumn<Diagnose, Integer> diagnosetypCol;

	@FXML
	private TableColumn<Diagnose, String> icdCol;

	@FXML
	private TableColumn<Diagnose, String> erstellerCol;

	@FXML
	private TableColumn<Diagnose, String> bearbeiterCol;

	private ObservableList<Integer> diagnoseID = FXCollections.observableArrayList();
	private ObservableList<String> icdCode = FXCollections.observableArrayList();


	@FXML
	public void initialize() {

		System.out.println("Initialize Diagnosis-Tab!");
		initializeColumns();

		diagnosisTable.setItems(diagnoseView());
		for (int i = 0; i < diagnoseView().size(); i++){
			diagnoseID.add(diagnoseView().get(i).getDiagnoseId());
		} // TODO: 22.11.21 besser darstellen for schleife schlecht
		/*for (int i = 0; i < icdView().size(); i++){
			icdCode.add(icdView().get(i).getIcd10Code());
		}*/ // TODO: 18.11.21 icd10 code
		diagnosisOpId.setItems(diagnoseID);
		diagnosisIcdCode.setItems(icdCode);


		/*Callback<ListView<Icd10CodeSt>, ListCell<Icd10CodeSt>> cellFactory = new Callback<>(){
			@Override
			public ListCell<Icd10CodeSt> call (ListView<Icd10CodeSt> param){
				return new ListCell<>(){

					@Override
					protected void updateItem(Icd10CodeSt item, boolean empty){
						super.updateItem(item, empty);
						if(item == null || empty){
							setGraphic(null);
						}
						else {
							setText(item.getIcd10Code());
						}
					}
				};
			}
		};*/




	}
	
	@FXML
	public void createDiagnosis(ActionEvent event){
		System.out.println("Create diagnosis!");
		insertNewDiagnose();
	}

	public static ObservableList<Diagnose> diagnoseView(){
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
		List<Diagnose> diagnose = diagnoseDao.findAll();
		return FXCollections.observableArrayList(diagnose);
	}

	public static ObservableList<jooq.tables.pojos.Icd10CodeSt> icdView(){
		Icd10CodeStDao icd10CodeStDao = new Icd10CodeStDao(Main.configuration);
		List<Icd10CodeSt> icd10CodeStList = icd10CodeStDao.findAll();
		return FXCollections.observableArrayList(icd10CodeStList);
	}

	private void initializeColumns() {
		// tabellencols werden erstellt
		// create columns

		// columns Case
		diagIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getDiagnoseId()));
		textCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getKlartextDiagnose()));
		dateCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getDatum()));
		erstellzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErstellZeit()));
		bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiterZeit()));
		storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getStorniert()));
		opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
		diagnosetypCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getDiagnosetyp()));
		icdCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getIcd10Code()));
		erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getErsteller()));
		bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getBearbeiter()));
	}

	private void insertNewDiagnose() {
		int diagID; // automatisch generieren?
		diagID = onEditDiagnose();
		if(diagID == 0){
			diagID = 8; // hier automatisch generieren !!!
		}
		Byte storniert = null;
		int opID = diagnosisOpId.getValue();
		String icdCode = diagnosisIcdCode.getValue();
		//String diagTyp = diagnosisType.getValue(); // Stammdaten mit zahl ersetzten
		int diagTyp = 1;
		String freitext = diagnosisFreetext.getText();
		LocalDateTime datum = LocalDateTime.now(); // unterscheiden ob neu oder altes Datum nicht überschreiben
		LocalDateTime erstellZeit = LocalDateTime.now();
		LocalDateTime bearbeiterZeit = null;
		String bearbeiter = null; // eingeloggter Benutzer
		String ersteller = null;

		Diagnose diagnose = new Diagnose(diagID,freitext,datum,erstellZeit,bearbeiterZeit,storniert,opID,diagTyp,icdCode,ersteller,bearbeiter);
		DiagnoseDao diagnoseDao = new DiagnoseDao(Main.configuration);
		diagnoseDao.insert(diagnose);
		// update dao wenn Flag true

	}

	/*
	 boolean insertEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(Employee employee);
	 */

	public int onEditDiagnose() {
		int id = 0;
		// check the table's selected item and get selected item
		if (diagnosisTable.getSelectionModel().getSelectedItem() != null) {
			Diagnose selectedItem = diagnosisTable.getSelectionModel().getSelectedItem();
			id = selectedItem.getDiagnoseId();
		}
		return id;

	}


}
