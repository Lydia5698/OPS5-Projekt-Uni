package controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import jooq.Tables;
import main.Main;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;

public class OPController{	
    
    @FXML
    private ComboBox<Integer> opCaseId;
	@FXML
    private DatePicker opDate;
    @FXML
    private ComboBox<String> opType;
    @FXML
    private ComboBox<String> opRoom;
    @FXML
    private ComboBox<Integer> opSurgeonId;
	
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

        Result<Record1<String>> result = Main.dslContext.select(Tables.OP_SAAL_ST.BESCHREIBUNG.as("opsaal"))
                .from(Tables.OP_SAAL_ST)
                .orderBy(Tables.OP_SAAL_ST.BESCHREIBUNG.asc())
                .fetch();
        List<String> opsaallist = result.map(record -> record.getValue("opsaal").toString());
        opRoom.getItems().setAll(opsaallist);
    	System.out.println("Initialize OP-Tab!");
	}
    
    
}
