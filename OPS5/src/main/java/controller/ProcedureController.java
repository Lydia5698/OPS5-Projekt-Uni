package controller;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.OperationDao;
import jooq.tables.daos.OpsCodeStDao;
import jooq.tables.daos.ProzedurDao;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.OpsCodeSt;
import jooq.tables.pojos.Prozedur;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This Controller displays the Procedure. You can create a new one or edit an existent
 */
public class ProcedureController {

    boolean flagEditProzedure = false;
    @FXML
    private TableView<Prozedur> procedureTable;
    @FXML
    private TableColumn<Prozedur, Integer> prozCol;
    @FXML
    private TableColumn<Prozedur, String> anmerkungCol;
    @FXML
    private TableColumn<Prozedur, String> storniertCol;
    @FXML
    private TableColumn<Prozedur, String> erstelltzeitCol;
    @FXML
    private TableColumn<Prozedur, String> bearbeiterzeitCol;
    @FXML
    private TableColumn<Prozedur, Integer> opIDCol;
    @FXML
    private TableColumn<Prozedur, String> opsCol;
    @FXML
    private TableColumn<Prozedur, String> bearbeiterCol;
    @FXML
    private TableColumn<Prozedur, String> erstellerCol;
    @FXML
    private SearchableComboBox<Operation> procedureOpID;
    @FXML
    private SearchableComboBox<OpsCodeSt> procedureOpsCode;
    @FXML
    private TextField procedureAnmerkung;

    /**
     * This Methode initialize the TableView for the existing Procedures and shows the Op-IDs and the OPs codes
     * in the Combobox
     */
    @FXML
    public void initialize() {
        initializeColumns();
        setProcedureOpID();
        setProcedureOpsCode();
    }

    /**
     * Launches when the Button Speichern is pressed. It sets the flag true so that we know that the user wants to edit
     * a Procedure. If the User isn't missing any necessary Values the Procedure is edited and the Window closes
     *
     * @param event The event of pushing the Speichern Button
     */
    @FXML
    public void editProcedure(ActionEvent event) {
        flagEditProzedure = true;
        if (procedureTable.getSelectionModel().isEmpty() && flagEditProzedure) {
            Main.logger.info("Fehlende Prozedur: Bitte wählen Sie die zu bearbeitende Prozedur in der Tabelle aus.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlende Prozedur");
            alert.setContentText("Bitte wählen Sie die zu bearbeitende Prozedur in der Tabelle aus.");
            alert.showAndWait();
        }
        else if(procedureAnmerkung.getText() != null && procedureAnmerkung.getText().matches(Main.blockedCharsForHL7)){
                Main.logger.warning("Falscher Eintrag: Die Sonderzeichen sind für HL7 reserviert.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Falscher Eintrag");
                alert.setContentText("Es dürfen keine Sonderzeichen verwendet werden (&,^,\\,~)!");
                alert.show();
        }
        else if(procedureAnmerkung.getText() != null && procedureAnmerkung.getText().length() > 200){
            Main.logger.warning("Falscher Eintrag: Die Länge des Freitextes ist zu lang.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Falscher Eintrag");
            alert.setContentText("Der Eintrag des Freitextes ist zu lang");
            alert.show();
        }
        else {
            insertNewProcedure();
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
    }

    /**
     * Launches when the Button Neue Prozedur is pressed. It sets the flag false so that we know that the user wants to create
     * a new Procedure. If the User isn't missing any necessary Values the Procedure is saved and the Window closes
     *
     * @param event The event of pushing the Neue Diagnose Button
     */
    @FXML
    void createNewProcedure(ActionEvent event) {
        flagEditProzedure = false;
        if (noMissingStatement()) {
            insertNewProcedure();
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
    }

    /**
     * Collects all Procedures from the Database and saves them in a observable Array List from Type Prozedur pojo
     */
    public void prozedurView(Integer opID) {
        ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
        List<Prozedur> prozedur = prozedurDao.findAll();
        if (opID == 0) {
            Main.logger.info("Es werden zurzeit alle Prozeduren angezeigt. Bitte wähle eine Operation aus, um eine spezifische Prozedur zu sehen.");
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setTitle("Information");
            confirm.setHeaderText("Alle Prozeduren");
            confirm.setContentText("Es werden zurzeit alle Prozeduren angezeigt. Bitte wähle eine Operation aus, um eine spezifische Prozedur zu sehen.");
            confirm.showAndWait();
            procedureTable.setItems(FXCollections.observableArrayList(prozedur));
        } else {
            Predicate<Prozedur> byOpID = prozedur1 -> prozedur1.getOpId().equals(opID);
            var result = prozedur.stream().filter(byOpID)
                    .collect(Collectors.toList());
            procedureTable.setItems(FXCollections.observableArrayList(result));
        }
    }

    /**
     * Initializes all Columns from the Table View procedureTable
     */
    private void initializeColumns() {
        // create columns

        // columns Procedure
        prozCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getProzId()));
        anmerkungCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getAnmerkung()));
        storniertCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(booleanToString(features.getValue().getStorniert())));
        erstelltzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getErstellZeit(), true)));
        bearbeiterzeitCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.dateTimeConverter(features.getValue().getBearbeiterZeit(), true)));
        opIDCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpId()));
        opsCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getOpsCode()));
        bearbeiterCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getBearbeiter())));
        erstellerCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(Converter.medPersonalConverter(features.getValue().getErsteller())));
    }

    /**
     * Inserts or edits a Procedure in the Database.
     */
    private void insertNewProcedure() {
        Integer prozID = null;
        Integer opID = procedureOpID.getValue().getOpId();
        String opsCodeValue = procedureOpsCode.getValue().getOpsCode();
        String anmerkungText = procedureAnmerkung.getText();
        LocalDateTime erstellZeit;
        LocalDateTime bearbeiterZeit;
        String bearbeiter;
        String ersteller;

        // Edit Procedure
        if (flagEditProzedure) {
            prozID = onEditProzedur();
            bearbeiterZeit = LocalDateTime.now();
            bearbeiter = MainController.getUserId();
            Prozedur prozedur = new Prozedur(prozID, anmerkungText, false, null, bearbeiterZeit, opID, opsCodeValue, bearbeiter, null);
            ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
            prozedurDao.update(prozedur);
            Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setTitle("Information");
            confirm.setHeaderText("Erfolgreich geupdatet");
            confirm.setContentText("Der Datensatz wurde geupdatet.");
            confirm.showAndWait();

        }
        // new Procedure
        else {
            erstellZeit = LocalDateTime.now();
            ersteller = MainController.getUserId();
            Prozedur prozedur = new Prozedur(prozID, anmerkungText, false, erstellZeit, null, opID, opsCodeValue, null, ersteller);
            ProzedurDao prozedurDao = new ProzedurDao(Main.configuration);
            prozedurDao.insert(prozedur);
            Main.logger.info("Der Datensatz wurde in die Datenbank eingefügt.");
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setTitle("Information");
            confirm.setHeaderText("Erfolgreich eingefügt");
            confirm.setContentText("Der Datensatz wurde in die Datenbank eingefügt.");
            confirm.showAndWait();

        }
    }

    /**
     * Gets the Procedure ID from the selected Procedure in the Table View
     *
     * @return Procedure ID
     */
    public int onEditProzedur() {
        int id = 0;
        // check the table's selected item and get selected item
        if (procedureTable.getSelectionModel().getSelectedItem() != null) {
            Prozedur selectedItem = procedureTable.getSelectionModel().getSelectedItem();
            id = selectedItem.getProzId();
        }
        return id;

    }

    /**
     * Checks if all the necessary Values for the Procedure are selected
     *
     * @return Boolean if no Statement is missing
     */
    public boolean noMissingStatement() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (procedureOpID.getValue() == null) {
            Main.logger.warning("Fehlende OP-ID: Bitte wählen Sie eine Operations-ID aus.");
            alert.setHeaderText("Fehlende OP-ID");
            alert.setContentText("Bitte wählen Sie eine Operations-ID aus");
            alert.show();
            return false;
        }

        if (procedureOpsCode.getValue() == null) {
            Main.logger.warning("Fehlender OPS-Code: Bitte wählen Sie einen OPS-Code aus.");
            alert.setHeaderText("Fehlender OPS-Code");
            alert.setContentText("Bitte wählen Sie einen OPS-Code aus");
            alert.show();
            return false;
        }

        if (procedureTable.getSelectionModel().isEmpty() && flagEditProzedure) {
            Main.logger.warning("Fehlende Prozedur: Bitte wählen Sie die zu bearbeitende Prozedur in der Tabelle aus.");
            alert.setHeaderText("Fehlende Prozedur");
            alert.setContentText("Bitte wählen Sie die zu bearbeitende Prozedur in der Tabelle aus");
            alert.show();
            return false;
        }
        if(procedureAnmerkung.getText() != null && procedureAnmerkung.getText().matches(Main.blockedCharsForHL7)){
            Main.logger.warning("Falscher Eintrag: Die Sonderzeichen sind für HL7 reserviert.");
            alert.setHeaderText("Falscher Eintrag");
            alert.setContentText("Es dürfen keine Sonderzeichen verwendet werden (&,^,\\,~)!");
            alert.show();
            return false;
        }
        if(procedureAnmerkung.getText() != null && procedureAnmerkung.getText().length() > 200){
            Main.logger.warning("Falscher Eintrag: Die Länge des Freitextes ist zu lang.");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Falscher Eintrag");
            alert.setContentText("Der Eintrag des Freitextes ist zu lang");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Gets triggered when a Procedure in the TableView gets selected
     */
    @FXML
    void mouseEntered(MouseEvent event) {
        if (event.getClickCount() > 0 && !procedureTable.getItems().isEmpty() && procedureTable.getSelectionModel().getSelectedItem() != null) {
            Prozedur prozedur = procedureTable.getSelectionModel().getSelectedItem();
            OpsCodeSt opsCodeSt = new OpsCodeStDao(Main.configuration).fetchOneByOpsCode(prozedur.getOpsCode());
            Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(prozedur.getOpId());
            OpsCodeSt opsCodeSt1 = new OpsCodeSt(opsCodeSt) {
                @Override
                public String toString() {
                    return opsCodeSt.getOpsCode() + " " +
                            opsCodeSt.getBeschreibung();
                }
            };
            Operation operation1 = new Operation(operation) {
                @Override
                public String toString() {
                    return operation.getOpId().toString();
                }
            };
            procedureOpsCode.setValue(opsCodeSt1);
            procedureOpID.setValue(operation1);
            procedureAnmerkung.setText(prozedur.getAnmerkung());
        }

    }

    /**
     * Gets all the Operation IDs and saves them in the procedureOpID Combobox
     */
    private void setProcedureOpID() {
        Converter.setOperation(procedureOpID, "procedureController");
    }

    /**
     * Gets all the OPs codes and saves them in the procedureOpsCode Combobox
     */
    private void setProcedureOpsCode() {
        Callback<ListView<OpsCodeSt>, ListCell<OpsCodeSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<OpsCodeSt> call(ListView<OpsCodeSt> medPersonalListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(OpsCodeSt opsCodeSt, boolean empty) {
                        super.updateItem(opsCodeSt, empty);
                        if (opsCodeSt == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(opsCodeSt.getOpsCode() + " " + opsCodeSt.getBeschreibung());
                        }
                    }
                };
            }
        };
        procedureOpsCode.setButtonCell(cellFactory.call(null));
        procedureOpsCode.setCellFactory(cellFactory);
        procedureOpsCode.getItems().setAll(new OpsCodeStDao(Main.configuration).findAll());
        procedureOpsCode.setSelectionModel(new CustomSelectionModel<>(procedureOpsCode));
        procedureOpsCode.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                Platform.runLater(() -> procedureOpsCode.setValue(oldValue));
            }
        });

    }

    /**
     * Converts a boolean to a string so it is shown prettier in the overview
     * @param notfall boolean value
     * @return ja when the boolean is true, else false
     */
    public String booleanToString(Boolean notfall){
        if(notfall){
            return "ja";
        }
        else{
            return "nein";
        }
    }
}
