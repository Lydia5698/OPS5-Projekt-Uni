/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import java.time.LocalDateTime;

import jooq.tables.Rolle;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RolleRecord extends UpdatableRecordImpl<RolleRecord> implements Record8<Integer, String, Integer, LocalDateTime, LocalDateTime, String, String, Byte> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>pmiw21g05_v01.rolle.op_id</code>.
     */
    public void setOpId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.op_id</code>.
     */
    public Integer getOpId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.bearbeiter</code>.
     */
    public void setBearbeiter(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return (String) get(1);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.rolle_st</code>.
     */
    public void setRolleSt(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.rolle_st</code>.
     */
    public Integer getRolleSt() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.ersteller</code>.
     */
    public void setErsteller(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.ersteller</code>.
     */
    public String getErsteller() {
        return (String) get(5);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.med_personal_pers_ID</code>.
     */
    public void setMedPersonalPersId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.med_personal_pers_ID</code>.
     */
    public String getMedPersonalPersId() {
        return (String) get(6);
    }

    /**
     * Setter for <code>pmiw21g05_v01.rolle.storniert</code>.
     */
    public void setStorniert(Byte value) {
        set(7, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.rolle.storniert</code>.
     */
    public Byte getStorniert() {
        return (Byte) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Integer, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, String, Integer, LocalDateTime, LocalDateTime, String, String, Byte> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Integer, String, Integer, LocalDateTime, LocalDateTime, String, String, Byte> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Rolle.ROLLE.OP_ID;
    }

    @Override
    public Field<String> field2() {
        return Rolle.ROLLE.BEARBEITER;
    }

    @Override
    public Field<Integer> field3() {
        return Rolle.ROLLE.ROLLE_ST;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Rolle.ROLLE.BEARBEITER_ZEIT;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Rolle.ROLLE.ERSTELL_ZEIT;
    }

    @Override
    public Field<String> field6() {
        return Rolle.ROLLE.ERSTELLER;
    }

    @Override
    public Field<String> field7() {
        return Rolle.ROLLE.MED_PERSONAL_PERS_ID;
    }

    @Override
    public Field<Byte> field8() {
        return Rolle.ROLLE.STORNIERT;
    }

    @Override
    public Integer component1() {
        return getOpId();
    }

    @Override
    public String component2() {
        return getBearbeiter();
    }

    @Override
    public Integer component3() {
        return getRolleSt();
    }

    @Override
    public LocalDateTime component4() {
        return getBearbeiterZeit();
    }

    @Override
    public LocalDateTime component5() {
        return getErstellZeit();
    }

    @Override
    public String component6() {
        return getErsteller();
    }

    @Override
    public String component7() {
        return getMedPersonalPersId();
    }

    @Override
    public Byte component8() {
        return getStorniert();
    }

    @Override
    public Integer value1() {
        return getOpId();
    }

    @Override
    public String value2() {
        return getBearbeiter();
    }

    @Override
    public Integer value3() {
        return getRolleSt();
    }

    @Override
    public LocalDateTime value4() {
        return getBearbeiterZeit();
    }

    @Override
    public LocalDateTime value5() {
        return getErstellZeit();
    }

    @Override
    public String value6() {
        return getErsteller();
    }

    @Override
    public String value7() {
        return getMedPersonalPersId();
    }

    @Override
    public Byte value8() {
        return getStorniert();
    }

    @Override
    public RolleRecord value1(Integer value) {
        setOpId(value);
        return this;
    }

    @Override
    public RolleRecord value2(String value) {
        setBearbeiter(value);
        return this;
    }

    @Override
    public RolleRecord value3(Integer value) {
        setRolleSt(value);
        return this;
    }

    @Override
    public RolleRecord value4(LocalDateTime value) {
        setBearbeiterZeit(value);
        return this;
    }

    @Override
    public RolleRecord value5(LocalDateTime value) {
        setErstellZeit(value);
        return this;
    }

    @Override
    public RolleRecord value6(String value) {
        setErsteller(value);
        return this;
    }

    @Override
    public RolleRecord value7(String value) {
        setMedPersonalPersId(value);
        return this;
    }

    @Override
    public RolleRecord value8(Byte value) {
        setStorniert(value);
        return this;
    }

    @Override
    public RolleRecord values(Integer value1, String value2, Integer value3, LocalDateTime value4, LocalDateTime value5, String value6, String value7, Byte value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RolleRecord
     */
    public RolleRecord() {
        super(Rolle.ROLLE);
    }

    /**
     * Create a detached, initialised RolleRecord
     */
    public RolleRecord(Integer opId, String bearbeiter, Integer rolleSt, LocalDateTime bearbeiterZeit, LocalDateTime erstellZeit, String ersteller, String medPersonalPersId, Byte storniert) {
        super(Rolle.ROLLE);

        setOpId(opId);
        setBearbeiter(bearbeiter);
        setRolleSt(rolleSt);
        setBearbeiterZeit(bearbeiterZeit);
        setErstellZeit(erstellZeit);
        setErsteller(ersteller);
        setMedPersonalPersId(medPersonalPersId);
        setStorniert(storniert);
    }

    /**
     * Create a detached, initialised RolleRecord
     */
    public RolleRecord(jooq.tables.pojos.Rolle value) {
        super(Rolle.ROLLE);

        if (value != null) {
            setOpId(value.getOpId());
            setBearbeiter(value.getBearbeiter());
            setRolleSt(value.getRolleSt());
            setBearbeiterZeit(value.getBearbeiterZeit());
            setErstellZeit(value.getErstellZeit());
            setErsteller(value.getErsteller());
            setMedPersonalPersId(value.getMedPersonalPersId());
            setStorniert(value.getStorniert());
        }
    }
}
