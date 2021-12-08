/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DiagnosetypSt implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer diagnosetyp;
    private String  beschreibung;

    public DiagnosetypSt() {}

    public DiagnosetypSt(DiagnosetypSt value) {
        this.diagnosetyp = value.diagnosetyp;
        this.beschreibung = value.beschreibung;
    }

    public DiagnosetypSt(
        Integer diagnosetyp,
        String  beschreibung
    ) {
        this.diagnosetyp = diagnosetyp;
        this.beschreibung = beschreibung;
    }

    /**
     * Getter for <code>pmiw21g05_v01.diagnosetyp_st.diagnosetyp</code>.
     */
    public Integer getDiagnosetyp() {
        return this.diagnosetyp;
    }

    /**
     * Setter for <code>pmiw21g05_v01.diagnosetyp_st.diagnosetyp</code>.
     */
    public void setDiagnosetyp(Integer diagnosetyp) {
        this.diagnosetyp = diagnosetyp;
    }

    /**
     * Getter for <code>pmiw21g05_v01.diagnosetyp_st.beschreibung</code>.
     */
    public String getBeschreibung() {
        return this.beschreibung;
    }

    /**
     * Setter for <code>pmiw21g05_v01.diagnosetyp_st.beschreibung</code>.
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); //"DiagnosetypSt ("

        sb.append(beschreibung);
        //sb.append(", ").append(diagnosetyp);

        //sb.append(")");
        return sb.toString();
    }
}
