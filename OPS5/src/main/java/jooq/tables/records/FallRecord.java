/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import java.time.LocalDateTime;

import jooq.tables.Fall;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FallRecord extends UpdatableRecordImpl<FallRecord> implements Record11<Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, String, String, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>pmiw21g05_v01.fall.fall_id</code>.
     */
    public void setFallId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.fall_id</code>.
     */
    public Integer getFallId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.aufnahmedatum</code>.
     */
    public void setAufnahmedatum(LocalDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.aufnahmedatum</code>.
     */
    public LocalDateTime getAufnahmedatum() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.entlassungsdatum</code>.
     */
    public void setEntlassungsdatum(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.entlassungsdatum</code>.
     */
    public LocalDateTime getEntlassungsdatum() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.storniert</code>.
     */
    public void setStorniert(Boolean value) {
        set(5, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.storniert</code>.
     */
    public Boolean getStorniert() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.pat_id</code>.
     */
    public void setPatId(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.pat_id</code>.
     */
    public Integer getPatId() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.station_st</code>.
     */
    public void setStationSt(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.station_st</code>.
     */
    public String getStationSt() {
        return (String) get(7);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.ersteller</code>.
     */
    public void setErsteller(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.ersteller</code>.
     */
    public String getErsteller() {
        return (String) get(8);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.bearbeiter</code>.
     */
    public void setBearbeiter(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return (String) get(9);
    }

    /**
     * Setter for <code>pmiw21g05_v01.fall.fall_typ</code>.
     */
    public void setFallTyp(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.fall.fall_typ</code>.
     */
    public Integer getFallTyp() {
        return (Integer) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row11<Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, String, String, String, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    @Override
    public Row11<Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, String, String, String, Integer> valuesRow() {
        return (Row11) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Fall.FALL.FALL_ID;
    }

    @Override
    public Field<LocalDateTime> field2() {
        return Fall.FALL.AUFNAHMEDATUM;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Fall.FALL.ENTLASSUNGSDATUM;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Fall.FALL.ERSTELL_ZEIT;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Fall.FALL.BEARBEITER_ZEIT;
    }

    @Override
    public Field<Boolean> field6() {
        return Fall.FALL.STORNIERT;
    }

    @Override
    public Field<Integer> field7() {
        return Fall.FALL.PAT_ID;
    }

    @Override
    public Field<String> field8() {
        return Fall.FALL.STATION_ST;
    }

    @Override
    public Field<String> field9() {
        return Fall.FALL.ERSTELLER;
    }

    @Override
    public Field<String> field10() {
        return Fall.FALL.BEARBEITER;
    }

    @Override
    public Field<Integer> field11() {
        return Fall.FALL.FALL_TYP;
    }

    @Override
    public Integer component1() {
        return getFallId();
    }

    @Override
    public LocalDateTime component2() {
        return getAufnahmedatum();
    }

    @Override
    public LocalDateTime component3() {
        return getEntlassungsdatum();
    }

    @Override
    public LocalDateTime component4() {
        return getErstellZeit();
    }

    @Override
    public LocalDateTime component5() {
        return getBearbeiterZeit();
    }

    @Override
    public Boolean component6() {
        return getStorniert();
    }

    @Override
    public Integer component7() {
        return getPatId();
    }

    @Override
    public String component8() {
        return getStationSt();
    }

    @Override
    public String component9() {
        return getErsteller();
    }

    @Override
    public String component10() {
        return getBearbeiter();
    }

    @Override
    public Integer component11() {
        return getFallTyp();
    }

    @Override
    public Integer value1() {
        return getFallId();
    }

    @Override
    public LocalDateTime value2() {
        return getAufnahmedatum();
    }

    @Override
    public LocalDateTime value3() {
        return getEntlassungsdatum();
    }

    @Override
    public LocalDateTime value4() {
        return getErstellZeit();
    }

    @Override
    public LocalDateTime value5() {
        return getBearbeiterZeit();
    }

    @Override
    public Boolean value6() {
        return getStorniert();
    }

    @Override
    public Integer value7() {
        return getPatId();
    }

    @Override
    public String value8() {
        return getStationSt();
    }

    @Override
    public String value9() {
        return getErsteller();
    }

    @Override
    public String value10() {
        return getBearbeiter();
    }

    @Override
    public Integer value11() {
        return getFallTyp();
    }

    @Override
    public FallRecord value1(Integer value) {
        setFallId(value);
        return this;
    }

    @Override
    public FallRecord value2(LocalDateTime value) {
        setAufnahmedatum(value);
        return this;
    }

    @Override
    public FallRecord value3(LocalDateTime value) {
        setEntlassungsdatum(value);
        return this;
    }

    @Override
    public FallRecord value4(LocalDateTime value) {
        setErstellZeit(value);
        return this;
    }

    @Override
    public FallRecord value5(LocalDateTime value) {
        setBearbeiterZeit(value);
        return this;
    }

    @Override
    public FallRecord value6(Boolean value) {
        setStorniert(value);
        return this;
    }

    @Override
    public FallRecord value7(Integer value) {
        setPatId(value);
        return this;
    }

    @Override
    public FallRecord value8(String value) {
        setStationSt(value);
        return this;
    }

    @Override
    public FallRecord value9(String value) {
        setErsteller(value);
        return this;
    }

    @Override
    public FallRecord value10(String value) {
        setBearbeiter(value);
        return this;
    }

    @Override
    public FallRecord value11(Integer value) {
        setFallTyp(value);
        return this;
    }

    @Override
    public FallRecord values(Integer value1, LocalDateTime value2, LocalDateTime value3, LocalDateTime value4, LocalDateTime value5, Boolean value6, Integer value7, String value8, String value9, String value10, Integer value11) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FallRecord
     */
    public FallRecord() {
        super(Fall.FALL);
    }

    /**
     * Create a detached, initialised FallRecord
     */
    public FallRecord(Integer fallId, LocalDateTime aufnahmedatum, LocalDateTime entlassungsdatum, LocalDateTime erstellZeit, LocalDateTime bearbeiterZeit, Boolean storniert, Integer patId, String stationSt, String ersteller, String bearbeiter, Integer fallTyp) {
        super(Fall.FALL);

        setFallId(fallId);
        setAufnahmedatum(aufnahmedatum);
        setEntlassungsdatum(entlassungsdatum);
        setErstellZeit(erstellZeit);
        setBearbeiterZeit(bearbeiterZeit);
        setStorniert(storniert);
        setPatId(patId);
        setStationSt(stationSt);
        setErsteller(ersteller);
        setBearbeiter(bearbeiter);
        setFallTyp(fallTyp);
    }

    /**
     * Create a detached, initialised FallRecord
     */
    public FallRecord(jooq.tables.pojos.Fall value) {
        super(Fall.FALL);

        if (value != null) {
            setFallId(value.getFallId());
            setAufnahmedatum(value.getAufnahmedatum());
            setEntlassungsdatum(value.getEntlassungsdatum());
            setErstellZeit(value.getErstellZeit());
            setBearbeiterZeit(value.getBearbeiterZeit());
            setStorniert(value.getStorniert());
            setPatId(value.getPatId());
            setStationSt(value.getStationSt());
            setErsteller(value.getErsteller());
            setBearbeiter(value.getBearbeiter());
            setFallTyp(value.getFallTyp());
        }
    }
}
