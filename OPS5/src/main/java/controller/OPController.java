package controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDateTime;

import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

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

    /**
     * This method returns the integer of the selected integer
     * @return the integer of the selected op case
     */
    public Integer getOpCaseId() {
        if(opCaseId.getSelectionModel().getSelectedItem() != null){
            return opCaseId.getSelectionModel().getSelectedItem().getFallId();
        }
        return null;
    }

    public void setOpCaseId(Fall fall){
        opCaseId.getSelectionModel().select(fall);
    }



    /**
     * This method returns the time and date of the beginning of the operation.
     * @return an DateTimePicker object with the selected beginning time.
     */
    public LocalDateTime getOpDateBegin() {
        if(opDateBegin.getDateTimeValue() != null){
            return opDateBegin.getDateTimeValue();
        }
        else{
            return null;
        }
    }


    /**
     * This method returns the time and date of the ending of the operation.
     * @return an DateTimePicker object with the selected ending time.
     */
    public LocalDateTime getOpDateEnd() {
        if(opDateEnd.getDateTimeValue() != null){
            return opDateEnd.getDateTimeValue();
        }
        else{
            return null;
        }
    }

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
    public LocalDateTime getCutTime() {
        if(cutTime.getDateTimeValue() != null){
            return cutTime.getDateTimeValue();
        }
        else{
            return null;
        }
    }

    /**
     * Getter for the sewing time.
     * @return an DateTimePicker object with the selected sewing time.
     */
    public LocalDateTime getSewTime() {
        if(sewTime.getDateTimeValue() != null){
            return sewTime.getDateTimeValue();
        }
        else{
            return null;
        }
    }

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
     * It sets all types of operations of the database as choosing options of the combobox.
     */
    private void setOpTyp() {
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
        setOpTyp();
        setOpSaal();
        setNarkose();
        setCase(null);
        opDateEnd.getEditor().clear();
        cutTime.getEditor().clear();
        sewTime.getEditor().clear();

        System.out.println("Initialize OP-Tab!");
	}

    /**
     * after the successfully insertion of an operation set all fields to default
     */
	public void clearFields(){
        opType.getSelectionModel().clearSelection();
        opRoom.getSelectionModel().clearSelection();
        narkose.getSelectionModel().clearSelection();
        opCaseId.getSelectionModel().clearSelection();
        opDateEnd.getEditor().clear();
        cutTime.getEditor().clear();
        sewTime.getEditor().clear();
        towelsBefore.decrement(towelsBefore.getValue());
        towelsAfter.decrement(towelsAfter.getValue());
    }

    /**
     * Sets the combo boxes in the Edit Op window to the values of the previously selected op to be edited.
     * @param opID the OpId to be processed
     */
    public void initializeDefaultComboboxen(int opID){
        Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(opID);
        Fall fall = new FallDao(Main.configuration).fetchOneByFallId(operation.getFallId());
        Fall fall1 = new Fall(fall){
            @Override
            public String toString(){
                String sb = "Fall-ID: " + fall.getFallId() + ", Aufnahmedatum: " +
                        fall.getAufnahmedatum();
                return sb;
            }
        };
        opCaseId.setValue(fall1);
	}
    

}
