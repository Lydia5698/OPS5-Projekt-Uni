package ExternalFiles;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import jooq.tables.daos.FallDao;
import jooq.tables.daos.MedPersonalDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.Fall;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.pojos.Operation;
import jooq.tables.pojos.Patient;
import main.Main;

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
        Integer patId = fall.getPatId();
        PatientDao patientDao = new PatientDao(Main.configuration);
        Patient patient = patientDao.findById(patId);
        return patient.getName() + ", " + patient.getVorname();
    }


    /**
     * Converts the mersonals id into its name
     * @param s String of the id from medpersonal
     * @return String of its first and last name
     */
    public static String medPersonalConverter(String s){
        if(s == null){return null;}
        else{
            MedPersonalDao medPersonalDao = new MedPersonalDao(Main.configuration);
            MedPersonal medPersonal = medPersonalDao.findById(s);
            return  medPersonal.getNachnameVorname();
        }
    }

    /**
     * Converts an optype to its integer to the description
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
        else if(i == 3){return "Intravenösnarkose";}
        else if(i == 4){return "keine Narkose";}
        else{return "örtliche Narkose";}
    }

    /**
     * Converts a diagnosis type to its string
     * @param i Integer from the diagnis
     * @return The string from the description of the diagnosis type
     */
    public static String diagnoseTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "präoperativ";}
        else{return "postoperativ";}
    }

    /**
     * Converts a sex char into the word
     * @param s S ist the char of the sex
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
        if(s == null){return null;}
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
     * @return The callback
     */
    public static Callback<ListView<Patient>, ListCell<Patient>> getPatient() {
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
        return cellFactory;
    }

    /**
     * Creates a callback from all Operations which prints only the OP-ID of each Operation
     * @return the Callback
     */
    public static Callback<ListView<Operation>, ListCell<Operation>> getOperation(){
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
                            setText(operation.getOpId().toString());
                        }
                    }
                };
            }
        };
        return cellFactory;
    }

}
