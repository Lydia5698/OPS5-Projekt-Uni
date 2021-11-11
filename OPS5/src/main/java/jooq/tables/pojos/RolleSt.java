/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RolleSt implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rolle;
    private String  bezeichnung;

    public RolleSt() {}

    public RolleSt(RolleSt value) {
        this.rolle = value.rolle;
        this.bezeichnung = value.bezeichnung;
    }

    public RolleSt(
        Integer rolle,
        String  bezeichnung
    ) {
        this.rolle = rolle;
        this.bezeichnung = bezeichnung;
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle_st.rolle</code>.
     */
    public Integer getRolle() {
        return this.rolle;
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle_st.rolle</code>.
     */
    public void setRolle(Integer rolle) {
        this.rolle = rolle;
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle_st.bezeichnung</code>.
     */
    public String getBezeichnung() {
        return this.bezeichnung;
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle_st.bezeichnung</code>.
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RolleSt (");

        sb.append(rolle);
        sb.append(", ").append(bezeichnung);

        sb.append(")");
        return sb.toString();
    }
}
