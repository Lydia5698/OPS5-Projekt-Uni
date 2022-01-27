package controller;
import ExternalFiles.CustomSelectionModel;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDateTime;

import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;

import ExternalFiles.DateTimePicker;
import org.controlsfx.control.SearchableComboBox;

/**
 * The OPController is visually a part of the Admission window.
 * It contains the fields needed for a new operation.
 */
public class OPController{	
    
    @FXML
    private SearchableComboBox<Fall> opCaseId;
    @FXML
    private DateTimePicker opDateBegin;
    @FXML
    private DateTimePicker opDateEnd;
    @FXML
    private SearchableComboBox<OpTypSt> opType;
    @FXML
    private SearchableComboBox<OpSaalSt> opRoom;
    @FXML
    private SearchableComboBox<NarkoseSt> narkose;
    @FXML
    private DateTimePicker cutTime;
    @FXML
    private DateTimePicker sewTime;
    @FXML
    private final Spinner<Integer> towelsBefore = new Spinner<>(0, 100, 0);
    @FXML
    private final Spinner<Integer> towelsAfter = new Spinner<>(0, 100, 0);

    /**
     * This method returns the integer of the selected integer
     * @return The integer of the selected op case
     */
    public Integer getOpCaseId() {
        if(opCaseId.getSelectionModel().getSelectedItem() != null){
            return opCaseId.getSelectionModel().getSelectedItem().getFallId();
        }
        return null;
    }

    /**
     * This method returns the time and date of the beginning of the operation.
     * @return An DateTimePicker object with the selected beginning time.
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
     * @return An DateTimePicker object with the selected ending time.
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
     * @return The int value of the OpTyp.
     */
    public Integer getOpType() {
        if(opType.getSelectionModel().getSelectedItem() != null){
            return opType.getSelectionModel().getSelectedItem().getOpTyp();
        }
        return null;
    }

    /**
     * Getter for the operation room.
     * @return The int value of the code of the selected OpRoom.
     */
    public Integer getOpRoomCode() {
        if(opRoom.getSelectionModel().getSelectedItem() != null){
            return opRoom.getSelectionModel().getSelectedItem().getCode();
        }
        return null;
    }

    /**
     * Getter for the type of Narkose.
     * @return The int value of Narkose.
     */
    public Integer getNarkose() {
        if(narkose.getSelectionModel().getSelectedItem() != null){
            return narkose.getSelectionModel().getSelectedItem().getNarkose();
        }
        return null;
    }

    /**
     * Getter for the cutting time.
     * @return An DateTimePicker object with the selected cutting time.
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
     * @return An DateTimePicker object with the selected sewing time.
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
     * @return Int value of belly towels before the operation.
     */
    public Integer getTowelBefore() {return towelsBefore.getValue();}

    /**
     * Getter for the amount of belly towels after the operation.
     * @return Int value of belly towels after the operation.
     */
    public Integer getTowelAfter() {return towelsAfter.getValue();}

    /**
     * Sets the values for the combobox of the cases.
     * This method is called when the window gets initialized and every time, a new patient is selected.
     * It then displays all cases of the selected patient.
     * @param patId Of the selected patient.
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
        opCaseId.setSelectionModel(new CustomSelectionModel<>(opCaseId));
        opCaseId.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> opCaseId.setValue(oldValue));
            }
        });
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
        opType.setSelectionModel(new CustomSelectionModel<>(opType));
        opType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> opType.setValue(oldValue));
            }
        });
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
                            setText(opSaal.getBeschreibung());
                        }
                    }
                };
            }
        };
        opRoom.setButtonCell(cellFactory.call(null));
        opRoom.setCellFactory(cellFactory);
        opRoom.getItems().setAll(new OpSaalStDao(Main.configuration).findAll());
        opRoom.setSelectionModel(new CustomSelectionModel<>(opRoom));
        opRoom.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> opRoom.setValue(oldValue));
            }
        });
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
        narkose.setSelectionModel(new CustomSelectionModel<>(narkose));
        narkose.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> narkose.setValue(oldValue));
            }
        });
    }

    /**
     * This method is called when the window is initialized.
     * It calls the methods for initializing the combobox.
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
     * After the successfully insertion of an operation set all fields to default
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
     * Sets the combobox in the Edit Op window to the values of the previously selected op to be edited.
     * @param opID The OpId to be processed
     */
    public void initializeDefaultCombobox(int opID){
        Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(opID);
        Fall fall = new FallDao(Main.configuration).fetchOneByFallId(operation.getFallId());
        Fall fall1 = new Fall(fall){
            @Override
            public String toString(){
                return "Fall-ID: " + fall.getFallId() + ", Aufnahmedatum: " +
                        fall.getAufnahmedatum();
            }
        };
        OpTypSt opTypSt = new OpTypStDao(Main.configuration).fetchOneByOpTyp(operation.getOpTypSt());
        OpTypSt opTypSt1 = new OpTypSt(opTypSt){
            @Override
            public String toString(){
                return opTypSt.getBeschreibung();
            }
        };
        OpSaalSt opSaalSt = new OpSaalStDao(Main.configuration).fetchOneByCode(operation.getOpSaal());
        OpSaalSt opSaalSt1 = new OpSaalSt(opSaalSt){
            @Override
            public String toString(){
                return opSaalSt.getBeschreibung();
            }
        };
        NarkoseSt narkoseSt = new NarkoseStDao(Main.configuration).fetchOneByNarkose(operation.getNarkoseSt());
        NarkoseSt narkoseSt1 = new NarkoseSt(narkoseSt){
            @Override
            public String toString(){
                return narkoseSt.getBeschreibung();
            }
        };
        opCaseId.setValue(fall1);
        opType.setValue(opTypSt1);
        opRoom.setValue(opSaalSt1);
        narkose.setValue(narkoseSt1);
	}

    /**
     * Sets the Date Time Picker and the Towels in the Edit Op window to the values of the previously selected op to be edited.
     * @param opID The OpId to be processed
     */
	public void initializeDefaultDateTimePicker(int opID){
        Operation operation = new OperationDao(Main.configuration).fetchOneByOpId(opID);
        opDateBegin.getEditor().clear();
        opDateBegin.setDateTimeValue(operation.getBeginn());
        opDateEnd.setDateTimeValue(operation.getEnde());
        cutTime.setDateTimeValue(operation.getSchnittzeit());
        sewTime.setDateTimeValue(operation.getNahtzeit());
        towelsBefore.increment(operation.getBauchtuecherPost());
        towelsAfter.increment(operation.getBauchtuecherPrae());
    }
    

}
