package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.util.Callback;

import java.util.List;
import java.util.Collections;

import jooq.Tables;
import jooq.tables.daos.FallDao;
import jooq.tables.pojos.Fall;
import jooq.tables.daos.OpTypStDao;
import jooq.tables.pojos.OpTypSt;
import jooq.tables.daos.OpSaalStDao;
import jooq.tables.pojos.OpSaalSt;
import jooq.tables.daos.NarkoseStDao;
import jooq.tables.pojos.NarkoseSt;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import ExternalFiles.DateTimePicker;

/**
 * The OPController is visually a part of the Admission window.
 * It contains the fields needed for a new operation.
 */
public class OPController{	
    
    @FXML
    private ComboBox<Fall> opCaseId;
    @FXML
    private DateTimePicker opDateBegin;
    @FXML
    private DateTimePicker opDateEnd;
    @FXML
    private ComboBox<OpTypSt> opType;
    @FXML
    private ComboBox<OpSaalSt> opRoom;
    @FXML
    private ComboBox<NarkoseSt> narkose;
    @FXML
    private DateTimePicker cutTime;
    @FXML
    private DateTimePicker sewTime;
    @FXML
    private Spinner<Integer> towelsBefore = new Spinner<Integer>(0,100, 0);
    @FXML
    private Spinner<Integer> towelsAfter = new Spinner<Integer>(0,100,0);

    public Integer getOpCaseId() {
        if(opCaseId.getSelectionModel().getSelectedItem() != null){
            return opCaseId.getSelectionModel().getSelectedItem().getFallId();
        }
        return null;
    }

    /**
     * This method returns the time and date of the beginning of the operation.
     * @return an DateTimePicker object with the selected beginning time.
     */
    public DateTimePicker getOpDateBegin() {return opDateBegin;}

    /**
     * This method returns the time and date of the ending of the operation.
     * @return an DateTimePicker object with the selected ending time.
     */
    public DateTimePicker getOpDateEnd() {return opDateEnd;}

    /**
     * Getter for the operation type.
     * @return the int value of the OpTyp.
     */
    public Integer getOpType() {
        if(opType.getSelectionModel().getSelectedItem() != null){
            return opType.getSelectionModel().getSelectedItem().getOpTyp();
        }
        return null;
    }

    /**
     * Getter for the operation room.
     * @return the int value of the code of the selected OpRoom.
     */
    public Integer getOpRoomCode() {
        if(opRoom.getSelectionModel().getSelectedItem() != null){
            return opRoom.getSelectionModel().getSelectedItem().getCode();
        }
        return null;
    }

    /**
     * Getter for the type of Narkose.
     * @return the int value of Narkose.
     */
    public Integer getNarkose() {
        if(narkose.getSelectionModel().getSelectedItem() != null){
            return narkose.getSelectionModel().getSelectedItem().getNarkose();
        }
        return null;
    }

    /**
     * Getter for the cutting time.
     * @return an DateTimePicker object with the selected cutting time.
     */
    public DateTimePicker getCutTime() {return cutTime;}

    /**
     * Getter for the sewing time.
     * @return an DateTimePicker object with the selected sewing time.
     */
    public DateTimePicker getSewTime() {return sewTime;}

    /**
     * Getter for the amount of belly towels before the operation.
     * @return int value of belly towels before the operation.
     */
    public Integer getTowelBefore() {return (Integer) towelsBefore.getValue();}

    /**
     * Getter for the amount of belly towels after the operation.
     * @return int value of belly towels after the operation.
     */
    public Integer getTowelAfter() {return (Integer) towelsAfter.getValue();}

    /**
     * Sets the values for the combobox of the cases.
     * This method is called when the window gets initialized and every time, a new patient is selected.
     * It then displayes all cases of the selected patient.
     * @param patId of the selected patient.
     */
    public void setCase(Integer patId){
        Callback<ListView<Fall>, ListCell<Fall>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Fall> call(ListView<Fall> caseListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Fall fall, boolean empty) {
                        super.updateItem(fall, empty);
                        if (fall == null || empty){
                            setGraphic(null);
                        } else {
                            setText("FallID: " + fall.getFallId().toString() + " , Aufnahme: " + fall.getAufnahmedatum());
                        }
                    }
                };
            }
        };
        opCaseId.setButtonCell(cellFactory.call(null));
        opCaseId.setCellFactory(cellFactory);
        opCaseId.getItems().setAll(new FallDao(Main.configuration).fetchByPatId(patId));
        opCaseId.valueProperty().set(null);
    }

    /**
     * This method is called when initialising the window.
     * It sets all case types of the database as choosing options of the combobox.
     */
    private void setFallTyp() {
        Callback<ListView<OpTypSt>, ListCell<OpTypSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<OpTypSt> call(ListView<OpTypSt> caseTypListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(OpTypSt opTyp, boolean empty) {
                        super.updateItem(opTyp, empty);
                        if (opTyp == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(opTyp.getBeschreibung());
                        }
                    }
                };
            }
        };
        opType.setButtonCell(cellFactory.call(null));
        opType.setCellFactory(cellFactory);
        opType.getItems().setAll(new OpTypStDao(Main.configuration).findAll());
    }
    /**
     * This method is called when initialising the window.
     * It sets all operation rooms of the database as choosing options of the combobox.
     */
    private void setOpSaal() {
        Callback<ListView<OpSaalSt>, ListCell<OpSaalSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<OpSaalSt> call(ListView<OpSaalSt> opRoomListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(OpSaalSt opSaal, boolean empty) {
                        super.updateItem(opSaal, empty);
                        if (opSaal == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(opSaal.getCode() + ", " + opSaal.getBeschreibung());
                        }
                    }
                };
            }
        };
        opRoom.setButtonCell(cellFactory.call(null));
        opRoom.setCellFactory(cellFactory);
        opRoom.getItems().setAll(new OpSaalStDao(Main.configuration).findAll());
    }
    /**
     * This method is called when initialising the window.
     * It sets all Narkose types of the database as choosing options of the combobox.
     */
    private void setNarkose() {
        Callback<ListView<NarkoseSt>, ListCell<NarkoseSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<NarkoseSt> call(ListView<NarkoseSt> narkoseListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(NarkoseSt nark, boolean empty) {
                        super.updateItem(nark, empty);
                        if (nark == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(nark.getBeschreibung());
                        }
                    }
                };
            }
        };
        narkose.setButtonCell(cellFactory.call(null));
        narkose.setCellFactory(cellFactory);
        narkose.getItems().setAll(new NarkoseStDao(Main.configuration).findAll());
    }

    /**
     * This method is called when the window is initialized.
     * It calls the methods for initializing the comboboxes.
     */
    @FXML
	public void initialize() {

        setFallTyp();
        setOpSaal();
        setNarkose();
        setCase(null);

        System.out.println("Initialize OP-Tab!");
	}
    
    
}
