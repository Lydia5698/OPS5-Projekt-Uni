package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import jooq.Tables;
import jooq.tables.daos.FallDao;
import jooq.tables.pojos.Fall;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Collections;

public class OPController{	
    
    @FXML
    private ComboBox<Fall> opCaseId;
	@FXML
    private DatePicker opDate;
    @FXML
    private ComboBox<String> opType;
    @FXML
    private ComboBox<String> opRoom;
    @FXML
    private ComboBox<String> narkose;
    @FXML
    private ComboBox<Integer> opSurgeonId;
    @FXML
    private Spinner<Integer> towelBefore = new Spinner<Integer>(0,100, 0);

    //TODO: statt opCaseID mit den PatIDs arbeiten!
    public ObservableList<Fall> getCases(Integer patId){
        FallDao fallDao = new FallDao(Main.configuration);
        List<Fall> cases = Collections.emptyList();
        if(patId != null) {
            cases = fallDao.fetchByPatId(patId);
        }
        return FXCollections.observableArrayList(cases);
    }

    public void updateCases(Integer patId){
        opCaseId.setItems(getCases(patId));
    }
	
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
        Result<Record1<String>> typeresult = Main.dslContext.select(Tables.OP_TYP_ST.BESCHREIBUNG.as("opbeschreibung"))
                .from(Tables.OP_TYP_ST)
                .orderBy(Tables.OP_TYP_ST.OP_TYP.asc())
                .fetch();
        List<String> optyp_list = typeresult.map(record -> record.getValue("opbeschreibung").toString());
        opType.getItems().setAll(optyp_list);

        Result<Record1<String>> roomResult = Main.dslContext.select(Tables.OP_SAAL_ST.BESCHREIBUNG.as("opsaal"))
                .from(Tables.OP_SAAL_ST)
                .orderBy(Tables.OP_SAAL_ST.BESCHREIBUNG.asc())
                .fetch();
        List<String> opsaallist = roomResult.map(record -> record.getValue("opsaal").toString());
        opRoom.getItems().setAll(opsaallist);

        Result<Record1<String>> narkoseTypResult = Main.dslContext.select(Tables.NARKOSE_ST.BESCHREIBUNG.as("narkose"))
                .from(Tables.NARKOSE_ST)
                .orderBy(Tables.NARKOSE_ST.BESCHREIBUNG.asc())
                .fetch();
        List<String> narkoseList = narkoseTypResult.map(record -> record.getValue("narkose").toString());
        narkose.getItems().setAll(narkoseList);

        opCaseId.setItems(getCases(null));
        System.out.println("Initialize OP-Tab!");
	}
    
    
}
