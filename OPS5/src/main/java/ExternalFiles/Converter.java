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
 * class that has static methods for convert different values
 */
public class Converter {

    /**
     * converts case type from int to string
     * @param i the integer from the case which string is needed
     * @return String of the case type
     */
    public static String fallTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "stationär";}
        else{return "ambulant";}
    }

    /**
     * converts a patient id to its name
     * @param i patient id
     * @return string of last and first name from the patient
     */
    public static String patientConverter(Integer i){
        PatientDao patientDao = new PatientDao(Main.configuration);
        Patient patient = patientDao.findById(i);
        return patient.getName() + ", " + patient.getVorname();
    }

    /**
     * This method converts a given fall id to the name of the patient of this case
     * @param i id from the given case
     * @return the last name and first name from the patient
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
     * converts the mersonals id into its name
     * @param s string of the id from medpersonal
     * @return string of its first and last name
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
     * converts an optype to its integer to the description
     * @param i integer from op type
     * @return string of the description
     */
    public static String opTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "minimalinvasiv";}
        else if(i == 2){return "nicht invasiv";}
        else{return "offen chirurgisch";}
    }

    /**
     * converts an integer to its description from narkose
     * @param i integer from the narkose
     * @return string with its description
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
     * converts a diagnosis type to its string
     * @param i integer from the diagnis
     * @return the string from the description of the diagnosis type
     */
    public static String diagnoseTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "präoperativ";}
        else{return "postoperativ";}
    }

    /**
     * converts a sex char into the word
     * @param s s ist the char of the sex
     * @return string of the sex
     */
    public static String geschlechtConverter(String s){
        if(s == null){return null;}
        else if(s.equals("w")){return "weiblich";}
        else if(s.equals("m")){return "männlich";}
        else{return "divers";}
    }

    /**
     * converts the sex into the iss standard for the hl7 message
     * @param s String from the sex of our database logic
     * @return the string of the sex for the iss standard
     */
    public static String IssSexConverter(String s){
        if(s == null){return null;}
        else if(s.equals("w")){return "F";}
        else if(s.equals("m")){return "M";}
        else{return "O";}
    }

    /**
     * Converts the iss standard of the sec into our logic
     * @param s the iss sex of the sent person
     * @return the string of the sex in our logic
     */
    public static String SexFromISSToOurConverter(String s){
        if(s == null){return null;}
        else if(s.equals("F")){return "w";}
        else if(s.equals("M")){return "m";}
        else{return "d";}
    }

    /**
     * Converts the key of the role to the given description.
     * @param i the int value of the role.
     * @return the String value of the role description.
     */
    public static String roleConverter(Integer i){
        if(i == null){return null;}
        switch (i){
            case 1:
                return "Assistent";
            case 2:
                return "Assistenzarzt";
            case 3:
                return "Diagnosesteller";
            case 4:
                return "leitender Chirurg";
            case 5:
                return "Operateur";
        }
        return "";
    }

    /**
     * creates a callback from all patients which prints only the last and first name of each patient
     * @return the callback
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
