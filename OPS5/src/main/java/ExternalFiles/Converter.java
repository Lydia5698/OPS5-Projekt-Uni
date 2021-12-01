package ExternalFiles;

import jooq.tables.daos.MedPersonalDao;
import jooq.tables.daos.PatientDao;
import jooq.tables.pojos.MedPersonal;
import jooq.tables.pojos.Patient;
import main.Main;


public class Converter {

    public static String fallTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "stationär";}
        else{return "ambulant";}
    }

    public static String patientConverter(Integer i){
        PatientDao patientDao = new PatientDao(Main.configuration);
        Patient patient = patientDao.findById(i);
        return patient.getName() + ", " + patient.getVorname();
    }

    public static String medPersonalConverter(String s){
        if(s == null){return null;}
        else{
            MedPersonalDao medPersonalDao = new MedPersonalDao(Main.configuration);
            MedPersonal medPersonal = medPersonalDao.findById(s);
            return  medPersonal.getNachnameVorname();
        }
    }

    public static String opTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "minimalinvasiv";}
        else if(i == 2){return "nicht invasiv";}
        else{return "offen chirurgisch";}
    }

    public static String narkoseConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "balancierte Narkose";}
        else if(i == 2){return "Inhalationsnarkose";}
        else if(i == 3){return "Intravenösnarkose";}
        else if(i == 4){return "keine Narkose";}
        else{return "örtliche Narkose";}
    }

    public static String diagnoseTypConverter(Integer i){
        if(i == null){return null;}
        else if(i == 1){return "praeoperativ";}
        else{return "postoperativ";}
    }
}
