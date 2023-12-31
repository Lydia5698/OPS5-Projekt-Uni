package ExternalFiles;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import jooq.tables.daos.*;
import jooq.tables.pojos.*;
import main.Main;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that has static methods for convert different values
 */
public class Converter {

    /**
     * Converts case type from int to string
     * @param i The integer from the case which string is needed
     * @return String of the case type
     */
    public static String fallTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "stationär";}
        else{return "ambulant";}
    }

    /**
     * Converts a patient id to its name
     * @param i Patient id
     * @return String of last and first name from the patient
     */
    public static String patientConverter(Integer i){
        PatientDao patientDao = new PatientDao(Main.configuration);
        Patient patient = patientDao.findById(i);
        assert patient != null;
        return patient.getName() + ", " + patient.getVorname();
    }

    /**
     * This method converts a given fall id to the name of the patient of this case
     * @param i Id from the given case
     * @return The last name and first name from the patient
     */
    public static String fallIdToPatientsNameConverter(Integer i){
        FallDao fallDao = new FallDao(Main.configuration);
        Fall fall = fallDao.findById(i);
        assert fall != null;
        Integer patId = fall.getPatId();
        PatientDao patientDao = new PatientDao(Main.configuration);
        Patient patient = patientDao.findById(patId);
        assert patient != null;
        return patient.getName() + ", " + patient.getVorname();
    }


    /**
     * Converts the med Personals id into its name
     * @param s String of the id from med Personal
     * @return String of its first and last name
     */
    public static String medPersonalConverter(String s){
        if(s == null){return null;}
        else{
            MedPersonalDao medPersonalDao = new MedPersonalDao(Main.configuration);
            MedPersonal medPersonal = medPersonalDao.findById(s);
            assert medPersonal != null;
            return  medPersonal.getNachnameVorname();
        }
    }

    public static String dateTimeConverter(LocalDateTime localDateTime, boolean isOverview){
        if(localDateTime == null && isOverview){return null;}
        else if(localDateTime == null){return "nicht bekannt";}
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            return localDateTime.format(formatter);
        }
    }

    /**
     * Converts an op type to its integer to the description
     * @param i Integer from op type
     * @return String of the description
     */
    public static String opTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "minimalinvasiv";}
        else if(i == 2){return "nicht invasiv";}
        else{return "offen chirurgisch";}
    }

    /**
     * Converts an integer to its description from narkose
     * @param i Integer from the narkose
     * @return String with its description
     */
    public static String narkoseConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "balancierte Narkose";}
        else if(i == 2){return "Inhalationsnarkose";}
        else if(i == 3){return "Intravenös Narkose";}
        else if(i == 4){return "keine Narkose";}
        else{return "örtliche Narkose";}
    }

    /**
     * Converts a diagnosis type to its string
     * @param i Integer from the diagnosis
     * @return The string from the description of the diagnosis type
     */
    public static String diagnoseTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "präoperativ";}
        else{return "postoperativ";}
    }

    /**
     * Converts a sex char into the word
     * @param s S is the char of the sex
     * @return String of the sex
     */
    public static String geschlechtConverter(String s){
        if(s == null){return null;}
        else if(s.equals("w")){return "weiblich";}
        else if(s.equals("m")){return "männlich";}
        else{return "divers";}
    }

    /**
     * Converts the sex into the iss standard for the hl7 message
     * @param s String from the sex of our database logic
     * @return The string of the sex for the iss standard
     */
    public static String IssSexConverter(String s){
        if(s == null){return "U";}
        else if(s.equals("w")){return "F";}
        else if(s.equals("m")){return "M";}
        else{return "O";}
    }

    /**
     * Converts the iss standard of the sec into our logic
     * @param s The iss sex of the sent person
     * @return The string of the sex in our logic
     */
    public static String SexFromISSToOurConverter(String s){
        if(s == null){return null;}
        else if(s.equals("F")){return "w";}
        else if(s.equals("M")){return "m";}
        else{return "d";}
    }

    /**
     * Converts the key of the role to the given description.
     * @param i The int value of the role.
     * @return The String value of the role description.
     */
    public static String roleConverter(Integer i){
        if(i == null){return null;}
        switch (i){
            case 1:
                return "Assistent";
            case 2:
                return "Assistenzarzt";
            case 3:
                return "leitender Chirurg";
            case 4:
                return "Operateur";
        }
        return "";
    }

    /**
     * Creates a callback from all patients which prints only the last and first name of each patient
     */
    public static void setPatient(SearchableComboBox<Patient> patient, boolean withChangelistener) {
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
                            setText(pat.getName() + ", " + pat.getVorname() + " (" + pat.getPatId() + ")");
                        }
                    }
                };
            }
        };
        patient.setButtonCell(cellFactory.call(null));
        patient.setCellFactory(cellFactory);
        patient.getItems().setAll(new PatientDao(Main.configuration).findAll());
        patient.setSelectionModel(new CustomSelectionModel<>(patient));
        if(withChangelistener){
            patient.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> patient.setValue(oldValue));
            }
            });
        }
    }

    /**
     * Creates a callback from all Operations which prints only the OP-ID of each Operation
     */
    public static void setOperation(SearchableComboBox<Operation> op, String type){
        Callback<ListView<Operation>, ListCell<Operation>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Operation> call(ListView<Operation> medPersonalListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Operation operation, boolean empty) {
                        super.updateItem(operation, empty);
                        if (operation == null || empty) {
                            setGraphic(null);
                        } else {
                            switch (type) {
                                case "diagnosisController":
                                case "procedureController":
                                    setText(operation.getOpId().toString());
                                    break;
                                case "role":
                                    setText("OP: " + operation.getOpId() + ", Fall: " + operation.getFallId() + ", Datum: " + dateTimeConverter(operation.getBeginn(), false));
                                    break;
                                case "communication":
                                    setText("OP-ID: " + operation.getOpId() + ", " + "Fall-ID: " + operation.getFallId() + "(" + Converter.fallIdToPatientsNameConverter(operation.getFallId()) + ")");
                                    break;
                            }
                        }
                    }
                };
            }
        };
        op.setButtonCell(cellFactory.call(null));
        op.setCellFactory(cellFactory);
        op.getItems().setAll(new OperationDao(Main.configuration).findAll());
        op.setSelectionModel(new CustomSelectionModel<>(op));
        op.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> op.setValue(oldValue));
            }
        });
    }

    /**
     * sets the values for the med personal
     * @param mitarbeiter the combobox of the med personal
     * @param select true if a specific value has to be selected
     * @param i the index of the selected item
     */
    public static void setMitarbeiter(SearchableComboBox<MedPersonal> mitarbeiter, boolean select, int i){
        Callback<ListView<MedPersonal>, ListCell<MedPersonal>> cellFactory = new Callback<>() {
            @Override
            public ListCell<MedPersonal> call(ListView<MedPersonal> userListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MedPersonal user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(user.getPersId() + " : " + user.getNachnameVorname());
                        }
                    }
                };
            }
        };
        mitarbeiter.setButtonCell(cellFactory.call(null));
        mitarbeiter.setCellFactory(cellFactory);
        List<MedPersonal> medPersonalList = new MedPersonalDao(Main.configuration).findAll();
        medPersonalList.sort(Comparator.comparing(MedPersonal::getNachnameVorname));
        var result = medPersonalList.stream().filter(medPersonal -> !medPersonal.getPersId().equals("00000000")) //KIS rausfiltern
                .collect(Collectors.toList());
        mitarbeiter.getItems().setAll(result);
        mitarbeiter.setSelectionModel(new CustomSelectionModel<>(mitarbeiter));
        if(select){mitarbeiter.getSelectionModel().select(i);}
        mitarbeiter.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> mitarbeiter.setValue(oldValue));
            }
        });
    }

    /**
     * Sets the values for the role so only the bezeichnung is shown
     * @param role the combobox of the role
     */
    public static void setRolle(SearchableComboBox<RolleSt> role){
        Callback<ListView<RolleSt>, ListCell<RolleSt>> cellFactory = new Callback<>() {
            @Override
            public ListCell<RolleSt> call(ListView<RolleSt> rolleListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(RolleSt ro, boolean empty) {
                        super.updateItem(ro, empty);
                        if (ro == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(ro.getBezeichnung());
                        }
                    }
                };
            }
        };
        role.setButtonCell(cellFactory.call(null));
        role.setCellFactory(cellFactory);
        role.getItems().setAll(new RolleStDao(Main.configuration).findAll());
        role.setSelectionModel(new CustomSelectionModel<>(role));
        role.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                Platform.runLater(()-> role.setValue(oldValue));
            }
        });
    }

}
