/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import jooq.tables.BlutgruppeSt;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BlutgruppeStRecord extends UpdatableRecordImpl<BlutgruppeStRecord> implements Record1<String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>pmiw21g05_v01.blutgruppe_st.blutgruppe</code>.
     */
    public void setBlutgruppe(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.blutgruppe_st.blutgruppe</code>.
     */
    public String getBlutgruppe() {
        return (String) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    public Row1<String> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return BlutgruppeSt.BLUTGRUPPE_ST.BLUTGRUPPE;
    }

    @Override
    public String component1() {
        return getBlutgruppe();
    }

    @Override
    public String value1() {
        return getBlutgruppe();
    }

    @Override
    public BlutgruppeStRecord value1(String value) {
        setBlutgruppe(value);
        return this;
    }

    @Override
    public BlutgruppeStRecord values(String value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BlutgruppeStRecord
     */
    public BlutgruppeStRecord() {
        super(BlutgruppeSt.BLUTGRUPPE_ST);
    }

    /**
     * Create a detached, initialised BlutgruppeStRecord
     */
    public BlutgruppeStRecord(String blutgruppe) {
        super(BlutgruppeSt.BLUTGRUPPE_ST);

        setBlutgruppe(blutgruppe);
    }

    /**
     * Create a detached, initialised BlutgruppeStRecord
     */
    public BlutgruppeStRecord(jooq.tables.pojos.BlutgruppeSt value) {
        super(BlutgruppeSt.BLUTGRUPPE_ST);

        if (value != null) {
            setBlutgruppe(value.getBlutgruppe());
        }
    }
}
