package controller;

import ExternalFiles.DateTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import org.jooq.exception.DataAccessException;

import java.sql.Timestamp;

public class FallController {

    @FXML
    private ComboBox<Patient> patient;

    @FXML
    private ComboBox<FallTypSt> falltyp;

    @FXML
    private ComboBox<StationSt> station;

    @FXML
    private DateTimePicker aufnahmedatum;

    @FXML
    private DateTimePicker entlassungsdatum;

    /**
     * the comboboxes are filled with the entries of the database
     */
    @FXML
    public void initialize() {
        setPatient();
        setFallTyp();
        setStation();
        setAufnahmedatum();
        System.out.println("Initialize Fall-Tab!");
    }

    /**
     * but all patients into the combobox to select one of them
     * each patient is represented by its lastname and first name
     * can't be null for insert a new case!!
     */
    private void setPatient() {
        Callback<ListView<Patient>, ListCell<Patient>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Patient> call(ListView<Patient> patientListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Patient pat, boolean empty) {
                        super.updateItem(pat, empty);
                        if (pat == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(pat.getName() + ", " + pat.getVorname());
                        }
                    }
                };
            }
        };
        patient.setButtonCell(cellFactory.call(null));
        patient.setCellFactory(cellFactory);
        patient.getItems().setAll(new PatientDao(Main.configuration).findAll());
    }

    /**
     * but all casetypes into the combox to select one of them
     * each type is represented by its description
     * can be null
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
    }

    /**
     * but all stations into the combox to select one of them
     * each station is represented by its description
     * can be null
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
    }

    /**
     * set the aufnahmedatum to the current date and time
     */
    private void setAufnahmedatum(){
        //set default value to current time
        aufnahmedatum.setDateTimeValue(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
    }

    /**
     * inserts the case into the database and clears all Fields after
     * @param actionEvent is activated if the user pushes the button
     */
    public void createFall(ActionEvent actionEvent) {
        insertFall();
        clearFields();
        System.out.println("Create Fall");
    }

    /**
     * in this method each attribute is set with the input of the user in a new case which is inserted in the database
     * after that
     */
    private void insertFall(){
        try{
            FallDao fallDao = new FallDao(Main.configuration);
            Fall fall = new Fall();
            fall.setPatId(patient.getValue().getPatId());
            fall.setFallTyp(falltyp.getValue().getFallTypId());
            fall.setStationSt(station.getValue().getStation());
            fall.setAufnahmedatum(aufnahmedatum.getDateTimeValue());
            fall.setEntlassungsdatum(entlassungsdatum.getDateTimeValue());
            fall.setErsteller(MainController.getUserId());
            fall.setErstellZeit(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            fall.setStorniert(false);
            fallDao.insert(fall);
            System.out.println("Patient wurde eingefügt.");
        }
        catch(DataAccessException e){
            e.printStackTrace();
        }

    }

    /**
     * this method clears all fields so the user can insert a new case
     */
    private void clearFields(){
        patient.getSelectionModel().clearSelection();
        falltyp.getSelectionModel().clearSelection();
        station.getSelectionModel().clearSelection();
        setAufnahmedatum();
        entlassungsdatum.setValue(null);
    }
}
