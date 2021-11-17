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
        Result<Record1<String>> result = Main.dslContext.select(Tables.OP_SAAL_ST.BESCHREIBUNG.as("opsaal"))
                .from(Tables.OP_SAAL_ST)
                .orderBy(Tables.OP_SAAL_ST.BESCHREIBUNG.asc())
                .fetch();
        List<String> opsaallist = result.map(record -> record.getValue("opsaal").toString());
        opRoom.getItems().setAll(opsaallist);
    	System.out.println("Initialize OP-Tab!");
	}
    
    
}
