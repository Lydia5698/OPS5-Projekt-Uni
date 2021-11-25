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
//import jooq.tables.daos.NarkoseStDao;
import jooq.tables.pojos.NarkoseSt;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;


public class OPController{	
    
    @FXML
    private ComboBox<Fall> opCaseId;
	@FXML
    private DatePicker opDate;
    @FXML
    private ComboBox<OpTypSt> opType;
    @FXML
    private ComboBox<OpSaalSt> opRoom;
    @FXML
    private ComboBox<NarkoseSt> narkose;
    @FXML
    private ComboBox<Integer> opSurgeonId;
    @FXML
    private Spinner<Integer> towelBefore = new Spinner<Integer>(0,100, 0);

    //TODO: statt opCaseID mit den PatIDs arbeiten!
    public void setCases(Integer patId){
        FallDao fallDao = new FallDao(Main.configuration);
        List<Fall> cases = Collections.emptyList();
        if(patId != null) {
            cases = fallDao.fetchByPatId(patId);
        }
        opCaseId.getItems().setAll(FXCollections.observableArrayList(cases));
    }

    public void updateCases(Integer patId){
        opCaseId.setItems(setCases(patId));
    }

    private void setFallTyp() {
        Callback<ListView<OpTypSt>, ListCell<OpTypSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<OpTypSt> call(ListView<OpTypSt> rolleListView) {
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

    private void setOpSaal() {
        Callback<ListView<OpSaalSt>, ListCell<OpSaalSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<OpSaalSt> call(ListView<OpSaalSt> rolleListView) {
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

    /*vate void setNarkose() {
        Callback<ListView<NarkoseSt>, ListCell<NarkoseSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<NarkoseSt> call(ListView<NarkoseSt> rolleListView) {
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
*/
    @FXML
	public void initialize() {
        /**
        Result<Record1<Integer>> opcase = Main.dslContext.select(Tables.FALL.FALL_ID.as("op_case"))
                .from(Tables.FALL)
                .join(Tables.PATIENT)
                .on(Tables.FALL.PAT_ID.eq(Tables.PATIENT.PAT_ID))
                .where(Tables.PATIENT.PAT_ID.eq(2))
                .fetch();

        List<Integer> opcase_list = opcase.map(record -> record.getValue("op_case").);
        opType.getItems().setAll(opcase_list);
*/
        setFallTyp();
        setOpSaal();
        //setNarkose();
        setCases(null);

        Result<Record1<String>> narkoseTypResult = Main.dslContext.select(Tables.NARKOSE_ST.BESCHREIBUNG.as("narkose"))
                .from(Tables.NARKOSE_ST)
                .orderBy(Tables.NARKOSE_ST.BESCHREIBUNG.asc())
                .fetch();
        List<String> narkoseList = narkoseTypResult.map(record -> record.getValue("narkose").toString());
        narkose.getItems().setAll(narkoseList);

        System.out.println("Initialize OP-Tab!");
	}
    
    
}
