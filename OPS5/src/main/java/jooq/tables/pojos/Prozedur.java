/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Prozedur implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer       prozId;
    private String        anmerkung;
    private Boolean       storniert;
    private LocalDateTime erstellZeit;
    private LocalDateTime bearbeiterZeit;
    private Integer       opId;
    private String        opsCode;
    private String        bearbeiter;
    private String        ersteller;

    public Prozedur() {}

    public Prozedur(Prozedur value) {
        this.prozId = value.prozId;
        this.anmerkung = value.anmerkung;
        this.storniert = value.storniert;
        this.erstellZeit = value.erstellZeit;
        this.bearbeiterZeit = value.bearbeiterZeit;
        this.opId = value.opId;
        this.opsCode = value.opsCode;
        this.bearbeiter = value.bearbeiter;
        this.ersteller = value.ersteller;
    }

    public Prozedur(
        Integer       prozId,
        String        anmerkung,
        Boolean       storniert,
        LocalDateTime erstellZeit,
        LocalDateTime bearbeiterZeit,
        Integer       opId,
        String        opsCode,
        String        bearbeiter,
        String        ersteller
    ) {
        this.prozId = prozId;
        this.anmerkung = anmerkung;
        this.storniert = storniert;
        this.erstellZeit = erstellZeit;
        this.bearbeiterZeit = bearbeiterZeit;
        this.opId = opId;
        this.opsCode = opsCode;
        this.bearbeiter = bearbeiter;
        this.ersteller = ersteller;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.proz_id</code>.
     */
    public Integer getProzId() {
        return this.prozId;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.proz_id</code>.
     */
    public void setProzId(Integer prozId) {
        this.prozId = prozId;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.anmerkung</code>.
     */
    public String getAnmerkung() {
        return this.anmerkung;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.anmerkung</code>.
     */
    public void setAnmerkung(String anmerkung) {
        this.anmerkung = anmerkung;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.storniert</code>.
     */
    public Boolean getStorniert() {
        return this.storniert;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.storniert</code>.
     */
    public void setStorniert(Boolean storniert) {
        this.storniert = storniert;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return this.erstellZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime erstellZeit) {
        this.erstellZeit = erstellZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return this.bearbeiterZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime bearbeiterZeit) {
        this.bearbeiterZeit = bearbeiterZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.op_id</code>.
     */
    public Integer getOpId() {
        return this.opId;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.op_id</code>.
     */
    public void setOpId(Integer opId) {
        this.opId = opId;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.ops_code</code>.
     */
    public String getOpsCode() {
        return this.opsCode;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.ops_code</code>.
     */
    public void setOpsCode(String opsCode) {
        this.opsCode = opsCode;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return this.bearbeiter;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.bearbeiter</code>.
     */
    public void setBearbeiter(String bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    /**
     * Getter for <code>pmiw21g05_v01.prozedur.ersteller</code>.
     */
    public String getErsteller() {
        return this.ersteller;
    }

    /**
     * Setter for <code>pmiw21g05_v01.prozedur.ersteller</code>.
     */
    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Prozedur (");

        sb.append(prozId);
        sb.append(", ").append(anmerkung);
        sb.append(", ").append(storniert);
        sb.append(", ").append(erstellZeit);
        sb.append(", ").append(bearbeiterZeit);
        sb.append(", ").append(opId);
        sb.append(", ").append(opsCode);
        sb.append(", ").append(bearbeiter);
        sb.append(", ").append(ersteller);

        sb.append(")");
        return sb.toString();
    }
}
