package ExternalFiles;

import jooq.tables.daos.MedPersonalDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.pojos.Patient;
import main.Main;

/**
 * class that has static methods for convert diffrent values
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
        else{return "d";}
    }
}
