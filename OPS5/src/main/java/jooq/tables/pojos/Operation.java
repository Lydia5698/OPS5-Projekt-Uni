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
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer       opId;
    private LocalDateTime beginn;
    private LocalDateTime ende;
    private Integer       bauchtuecherPrae;
    private Integer       bauchtuecherPost;
    private LocalDateTime schnittzeit;
    private LocalDateTime nahtzeit;
    private LocalDateTime erstellZeit;
    private LocalDateTime bearbeiterZeit;
    private Boolean       storniert;
    private Integer       fallId;
    private Integer       opSaal;
    private Integer       narkoseSt;
    private Integer       opTypSt;
    private String        ersteller;
    private String        bearbeiter;

    public Operation() {}

    public Operation(Operation value) {
        this.opId = value.opId;
        this.beginn = value.beginn;
        this.ende = value.ende;
        this.bauchtuecherPrae = value.bauchtuecherPrae;
        this.bauchtuecherPost = value.bauchtuecherPost;
        this.schnittzeit = value.schnittzeit;
        this.nahtzeit = value.nahtzeit;
        this.erstellZeit = value.erstellZeit;
        this.bearbeiterZeit = value.bearbeiterZeit;
        this.storniert = value.storniert;
        this.fallId = value.fallId;
        this.opSaal = value.opSaal;
        this.narkoseSt = value.narkoseSt;
        this.opTypSt = value.opTypSt;
        this.ersteller = value.ersteller;
        this.bearbeiter = value.bearbeiter;
    }

    public Operation(
        Integer       opId,
        LocalDateTime beginn,
        LocalDateTime ende,
        Integer       bauchtuecherPrae,
        Integer       bauchtuecherPost,
        LocalDateTime schnittzeit,
        LocalDateTime nahtzeit,
        LocalDateTime erstellZeit,
        LocalDateTime bearbeiterZeit,
        Boolean       storniert,
        Integer       fallId,
        Integer       opSaal,
        Integer       narkoseSt,
        Integer       opTypSt,
        String        ersteller,
        String        bearbeiter
    ) {
        this.opId = opId;
        this.beginn = beginn;
        this.ende = ende;
        this.bauchtuecherPrae = bauchtuecherPrae;
        this.bauchtuecherPost = bauchtuecherPost;
        this.schnittzeit = schnittzeit;
        this.nahtzeit = nahtzeit;
        this.erstellZeit = erstellZeit;
        this.bearbeiterZeit = bearbeiterZeit;
        this.storniert = storniert;
        this.fallId = fallId;
        this.opSaal = opSaal;
        this.narkoseSt = narkoseSt;
        this.opTypSt = opTypSt;
        this.ersteller = ersteller;
        this.bearbeiter = bearbeiter;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_id</code>.
     */
    public Integer getOpId() {
        return this.opId;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_id</code>.
     */
    public void setOpId(Integer opId) {
        this.opId = opId;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.beginn</code>.
     */
    public LocalDateTime getBeginn() {
        return this.beginn;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.beginn</code>.
     */
    public void setBeginn(LocalDateTime beginn) {
        this.beginn = beginn;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.ende</code>.
     */
    public LocalDateTime getEnde() {
        return this.ende;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.ende</code>.
     */
    public void setEnde(LocalDateTime ende) {
        this.ende = ende;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bauchtuecher_prae</code>.
     */
    public Integer getBauchtuecherPrae() {
        return this.bauchtuecherPrae;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bauchtuecher_prae</code>.
     */
    public void setBauchtuecherPrae(Integer bauchtuecherPrae) {
        this.bauchtuecherPrae = bauchtuecherPrae;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bauchtuecher_post</code>.
     */
    public Integer getBauchtuecherPost() {
        return this.bauchtuecherPost;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bauchtuecher_post</code>.
     */
    public void setBauchtuecherPost(Integer bauchtuecherPost) {
        this.bauchtuecherPost = bauchtuecherPost;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.schnittzeit</code>.
     */
    public LocalDateTime getSchnittzeit() {
        return this.schnittzeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.schnittzeit</code>.
     */
    public void setSchnittzeit(LocalDateTime schnittzeit) {
        this.schnittzeit = schnittzeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.nahtzeit</code>.
     */
    public LocalDateTime getNahtzeit() {
        return this.nahtzeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.nahtzeit</code>.
     */
    public void setNahtzeit(LocalDateTime nahtzeit) {
        this.nahtzeit = nahtzeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return this.erstellZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime erstellZeit) {
        this.erstellZeit = erstellZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return this.bearbeiterZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime bearbeiterZeit) {
        this.bearbeiterZeit = bearbeiterZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.storniert</code>.
     */
    public Boolean getStorniert() {
        return this.storniert;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.storniert</code>.
     */
    public void setStorniert(Boolean storniert) {
        this.storniert = storniert;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.fall_id</code>.
     */
    public Integer getFallId() {
        return this.fallId;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.fall_id</code>.
     */
    public void setFallId(Integer fallId) {
        this.fallId = fallId;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_saal</code>.
     */
    public Integer getOpSaal() {
        return this.opSaal;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_saal</code>.
     */
    public void setOpSaal(Integer opSaal) {
        this.opSaal = opSaal;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.narkose_st</code>.
     */
    public Integer getNarkoseSt() {
        return this.narkoseSt;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.narkose_st</code>.
     */
    public void setNarkoseSt(Integer narkoseSt) {
        this.narkoseSt = narkoseSt;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_typ_st</code>.
     */
    public Integer getOpTypSt() {
        return this.opTypSt;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_typ_st</code>.
     */
    public void setOpTypSt(Integer opTypSt) {
        this.opTypSt = opTypSt;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.ersteller</code>.
     */
    public String getErsteller() {
        return this.ersteller;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.ersteller</code>.
     */
    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return this.bearbeiter;
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bearbeiter</code>.
     */
    public void setBearbeiter(String bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Operation (");

        sb.append(opId);
        sb.append(", ").append(beginn);
        sb.append(", ").append(ende);
        sb.append(", ").append(bauchtuecherPrae);
        sb.append(", ").append(bauchtuecherPost);
        sb.append(", ").append(schnittzeit);
        sb.append(", ").append(nahtzeit);
        sb.append(", ").append(erstellZeit);
        sb.append(", ").append(bearbeiterZeit);
        sb.append(", ").append(storniert);
        sb.append(", ").append(fallId);
        sb.append(", ").append(opSaal);
        sb.append(", ").append(narkoseSt);
        sb.append(", ").append(opTypSt);
        sb.append(", ").append(ersteller);
        sb.append(", ").append(bearbeiter);

        sb.append(")");
        return sb.toString();
    }
}
