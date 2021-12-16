/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer       patId;
    private String        name;
    private String        vorname;
    private LocalDate     geburtsdatum;
    private String        blutgruppe;
    private String        geschlecht;
    private String        bearbeiter;
    private LocalDateTime bearbeiterZeit;
    private String        ersteller;
    private LocalDateTime erstellZeit;
    private Boolean       storniert;
    private String        geburtsort;
    private String        strasse;
    private String        postleitzahl;
    private String        telefonnummer;

    public Patient() {}

    public Patient(Patient value) {
        this.patId = value.patId;
        this.name = value.name;
        this.vorname = value.vorname;
        this.geburtsdatum = value.geburtsdatum;
        this.blutgruppe = value.blutgruppe;
        this.geschlecht = value.geschlecht;
        this.bearbeiter = value.bearbeiter;
        this.bearbeiterZeit = value.bearbeiterZeit;
        this.ersteller = value.ersteller;
        this.erstellZeit = value.erstellZeit;
        this.storniert = value.storniert;
        this.geburtsort = value.geburtsort;
        this.strasse = value.strasse;
        this.postleitzahl = value.postleitzahl;
        this.telefonnummer = value.telefonnummer;
    }

    public Patient(
        Integer       patId,
        String        name,
        String        vorname,
        LocalDate     geburtsdatum,
        String        blutgruppe,
        String        geschlecht,
        String        bearbeiter,
        LocalDateTime bearbeiterZeit,
        String        ersteller,
        LocalDateTime erstellZeit,
        Boolean       storniert,
        String        geburtsort,
        String        strasse,
        String        postleitzahl,
        String        telefonnummer
    ) {
        this.patId = patId;
        this.name = name;
        this.vorname = vorname;
        this.geburtsdatum = geburtsdatum;
        this.blutgruppe = blutgruppe;
        this.geschlecht = geschlecht;
        this.bearbeiter = bearbeiter;
        this.bearbeiterZeit = bearbeiterZeit;
        this.ersteller = ersteller;
        this.erstellZeit = erstellZeit;
        this.storniert = storniert;
        this.geburtsort = geburtsort;
        this.strasse = strasse;
        this.postleitzahl = postleitzahl;
        this.telefonnummer = telefonnummer;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.pat_id</code>.
     */
    public Integer getPatId() {
        return this.patId;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.pat_id</code>.
     */
    public void setPatId(Integer patId) {
        this.patId = patId;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.name</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.vorname</code>.
     */
    public String getVorname() {
        return this.vorname;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.vorname</code>.
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.geburtsdatum</code>.
     */
    public LocalDate getGeburtsdatum() {
        return this.geburtsdatum;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.geburtsdatum</code>.
     */
    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.blutgruppe</code>.
     */
    public String getBlutgruppe() {
        return this.blutgruppe;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.blutgruppe</code>.
     */
    public void setBlutgruppe(String blutgruppe) {
        this.blutgruppe = blutgruppe;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.geschlecht</code>.
     */
    public String getGeschlecht() {
        return this.geschlecht;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.geschlecht</code>.
     */
    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return this.bearbeiter;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.bearbeiter</code>.
     */
    public void setBearbeiter(String bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return this.bearbeiterZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime bearbeiterZeit) {
        this.bearbeiterZeit = bearbeiterZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.ersteller</code>.
     */
    public String getErsteller() {
        return this.ersteller;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.ersteller</code>.
     */
    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return this.erstellZeit;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime erstellZeit) {
        this.erstellZeit = erstellZeit;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.storniert</code>.
     */
    public Boolean getStorniert() {
        return this.storniert;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.storniert</code>.
     */
    public void setStorniert(Boolean storniert) {
        this.storniert = storniert;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.geburtsort</code>.
     */
    public String getGeburtsort() {
        return this.geburtsort;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.geburtsort</code>.
     */
    public void setGeburtsort(String geburtsort) {
        this.geburtsort = geburtsort;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.strasse</code>.
     */
    public String getStrasse() {
        return this.strasse;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.strasse</code>.
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.postleitzahl</code>.
     */
    public String getPostleitzahl() {
        return this.postleitzahl;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.postleitzahl</code>.
     */
    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Getter for <code>pmiw21g05_v01.patient.telefonnummer</code>.
     */
    public String getTelefonnummer() {
        return this.telefonnummer;
    }

    /**
     * Setter for <code>pmiw21g05_v01.patient.telefonnummer</code>.
     */
    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Patient( ");

        sb.append(patId);
        sb.append(", ").append(name);
        sb.append(", ").append(vorname);
        sb.append(", ").append(geburtsdatum);
        sb.append(", ").append(blutgruppe);
        sb.append(", ").append(geschlecht);
        sb.append(", ").append(bearbeiter);
        sb.append(", ").append(bearbeiterZeit);
        sb.append(", ").append(ersteller);
        sb.append(", ").append(erstellZeit);
        sb.append(", ").append(storniert);
        sb.append(", ").append(geburtsort);
        sb.append(", ").append(strasse);
        sb.append(", ").append(postleitzahl);
        sb.append(", ").append(telefonnummer);

        sb.append(")");
        return sb.toString();
    }
}
