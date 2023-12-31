/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OpTypSt implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer opTyp;
    private String  beschreibung;

    public OpTypSt() {}

    public OpTypSt(OpTypSt value) {
        this.opTyp = value.opTyp;
        this.beschreibung = value.beschreibung;
    }

    public OpTypSt(
        Integer opTyp,
        String  beschreibung
    ) {
        this.opTyp = opTyp;
        this.beschreibung = beschreibung;
    }

    /**
     * Getter for <code>pmiw21g05_v01.op_typ_st.op_typ</code>.
     */
    public Integer getOpTyp() {
        return this.opTyp;
    }

    /**
     * Setter for <code>pmiw21g05_v01.op_typ_st.op_typ</code>.
     */
    public void setOpTyp(Integer opTyp) {
        this.opTyp = opTyp;
    }

    /**
     * Getter for <code>pmiw21g05_v01.op_typ_st.beschreibung</code>.
     */
    public String getBeschreibung() {
        return this.beschreibung;
    }

    /**
     * Setter for <code>pmiw21g05_v01.op_typ_st.beschreibung</code>.
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("OpTypSt (");

        sb.append(opTyp);
        sb.append(", ").append(beschreibung);

        sb.append(")");
        return sb.toString();
    }
}
