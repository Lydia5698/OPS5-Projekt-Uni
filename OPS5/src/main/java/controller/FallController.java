package controller;

import ExternalFiles.Converter;
import ExternalFiles.CustomSelectionModel;
import ExternalFiles.DateTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.FallTypStDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.daos.StationStDao;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.FallTypSt;
import jooq.tables.pojos.Patient;
import jooq.tables.pojos.StationSt;
import main.Main;
import org.controlsfx.control.SearchableComboBox;
import org.jooq.exception.DataAccessException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Takes care of the newly created cases
 */
public class FallController {

    @FXML
    private SearchableComboBox<Patient> patient;

    @FXML
    private SearchableComboBox<FallTypSt> falltyp;

    @FXML
    private SearchableComboBox<StationSt> station;

    @FXML
    private DateTimePicker aufnahmedatum;

    @FXML
    private DateTimePicker entlassungsdatum;

    @FXML
    private Button speicherbutton;

    /**
     * The comboboxes are filled with the entries of the database
     */
    @FXML
    public void initialize() {
        setPatient();
        setFallTyp();
        setStation();
        entlassungsdatum.getEditor().clear();
        System.out.println("Initialize Fall-Tab!");
    }

    /**
     * But all patients into the combobox to select one of them
     * each patient is represented by its lastname and first name.
     * Can't be null for insert a new case!!
     */
    private void setPatient() {
        Callback<ListView<Patient>, ListCell<Patient>> cellFactory = Converter.getPatient();
        patient.setButtonCell(cellFactory.call(null));
        patient.setCellFactory(cellFactory);
        patient.getItems().setAll(new PatientDao(Main.configuration).findAll());
        patient.setSelectionModel(new CustomSelectionModel<>(patient));
    }

    /**
     * But all casetypes into the combox to select one of them
     * each type is represented by its description.
     * Can be null
     */
    private void setFallTyp(){
        Callback<ListView<FallTypSt>, ListCell<FallTypSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<FallTypSt> call(ListView<FallTypSt> falltypListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(FallTypSt f, boolean empty) {
                        super.updateItem(f, empty);
                        if (f == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(f.getBeschreibung());
                        }
                    }
                };
            }
        };
        falltyp.setButtonCell(cellFactory.call(null));
        falltyp.setCellFactory(cellFactory);
        falltyp.getItems().setAll(new FallTypStDao(Main.configuration).findAll());
        falltyp.setSelectionModel(new CustomSelectionModel<>(falltyp));
    }

    /**
     * But all stations into the combox to select one of them
     * each station is represented by its description.
     * Can be null
     */
    private void setStation(){
        Callback<ListView<StationSt>, ListCell<StationSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<StationSt> call(ListView<StationSt> stationListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(StationSt s, boolean empty) {
                        super.updateItem(s, empty);
                        if (s == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(s.getBezeichnung());
                        }
                    }
                };
            }
        };
        station.setButtonCell(cellFactory.call(null));
        station.setCellFactory(cellFactory);
        station.getItems().setAll(new StationStDao(Main.configuration).findAll());
        station.setSelectionModel(new CustomSelectionModel<>(station));
    }

    /**
     * After pressing the button, the entries are transferred to the attribute values and the
     * case is inserted into the database with the help of the DAO and the window is closed afterwards
     * @param actionEvent Is activated if the user pushes the button
     */
    public void createFall(ActionEvent actionEvent) {
        try{
            FallDao fallDao = new FallDao(Main.configuration);
            Fall fall = new Fall();
            if(patient.getValue() != null){fall.setPatId(patient.getValue().getPatId());}
            if(falltyp.getValue() != null){fall.setFallTyp(falltyp.getValue().getFallTypId());}
            if(station.getValue() != null){fall.setStationSt(station.getValue().getStation());}
            if(aufnahmedatum.getValue() != null){fall.setAufnahmedatum(aufnahmedatum.getDateTimeValue());}
            if(entlassungsdatum.getValue() != null){fall.setEntlassungsdatum(entlassungsdatum.getDateTimeValue());}
            fall.setErsteller(MainController.getUserId());
            fall.setErstellZeit(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            fall.setStorniert(false);

            //checking for values which can not be null (in this case it is only the patient)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fehlender Eintrag!");
            if (fall.getPatId() == null) {
                alert.setContentText("Es wurde kein Patient ausgewählt!");
                alert.showAndWait();
            }
            //checking for invalid entries concerning the dates
            //Entlassungsdatum ist vor dem Aufnahmedatum
            else if(fall.getEntlassungsdatum() != null && fall.getAufnahmedatum() == null && fall.getEntlassungsdatum().isBefore(LocalDateTime.now())){
                        alert.setHeaderText("Falscher Eintrag!");
                        alert.setContentText("Das gewählte Entlassungsdatum liegt vor dem Aufnahmedatum!");
                        alert.showAndWait();
            }
            //Entlassungsdatum ist vor dem Aufnahmedatum
            else if (fall.getEntlassungsdatum() != null && fall.getAufnahmedatum() != null && fall.getEntlassungsdatum().isBefore(fall.getAufnahmedatum())){
                alert.setHeaderText("Falscher Eintrag!");
                alert.setContentText("Das gewählte Entlassungsdatum liegt vor dem Aufnahmedatum!");
                alert.showAndWait();
            }
            else {
                //if the aufnahmedatum is null set it to the current date and time
                if (fall.getAufnahmedatum() == null) {
                    fall.setAufnahmedatum(LocalDateTime.now());
                }
                fallDao.insert(fall);
                System.out.println("Creating case!");

                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setContentText("Der Fall wurde in die Datenbank eingefügt.");
                confirm.showAndWait();

                //close the window
                Stage stage = (Stage) speicherbutton.getScene().getWindow();
                stage.close();
            }
        }
        catch(DataAccessException e){
            e.printStackTrace();
        }
    }
}
