/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import java.time.LocalDateTime;

import jooq.tables.Operation;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record17;
import org.jooq.Row17;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OperationRecord extends UpdatableRecordImpl<OperationRecord> implements Record17<Integer, LocalDateTime, LocalDateTime, Integer, Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, Integer, Integer, Integer, String, String, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_id</code>.
     */
    public void setOpId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_id</code>.
     */
    public Integer getOpId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.beginn</code>.
     */
    public void setBeginn(LocalDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.beginn</code>.
     */
    public LocalDateTime getBeginn() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.ende</code>.
     */
    public void setEnde(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.ende</code>.
     */
    public LocalDateTime getEnde() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bauchtuecher_prae</code>.
     */
    public void setBauchtuecherPrae(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bauchtuecher_prae</code>.
     */
    public Integer getBauchtuecherPrae() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bauchtuecher_post</code>.
     */
    public void setBauchtuecherPost(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bauchtuecher_post</code>.
     */
    public Integer getBauchtuecherPost() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.schnittzeit</code>.
     */
    public void setSchnittzeit(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.schnittzeit</code>.
     */
    public LocalDateTime getSchnittzeit() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.nahtzeit</code>.
     */
    public void setNahtzeit(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.nahtzeit</code>.
     */
    public LocalDateTime getNahtzeit() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.erstell_zeit</code>.
     */
    public void setErstellZeit(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.erstell_zeit</code>.
     */
    public LocalDateTime getErstellZeit() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bearbeiter_zeit</code>.
     */
    public void setBearbeiterZeit(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bearbeiter_zeit</code>.
     */
    public LocalDateTime getBearbeiterZeit() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.storniert</code>.
     */
    public void setStorniert(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.storniert</code>.
     */
    public Boolean getStorniert() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.fall_id</code>.
     */
    public void setFallId(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.fall_id</code>.
     */
    public Integer getFallId() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_saal</code>.
     */
    public void setOpSaal(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_saal</code>.
     */
    public Integer getOpSaal() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.narkose_st</code>.
     */
    public void setNarkoseSt(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.narkose_st</code>.
     */
    public Integer getNarkoseSt() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.op_typ_st</code>.
     */
    public void setOpTypSt(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.op_typ_st</code>.
     */
    public Integer getOpTypSt() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.ersteller</code>.
     */
    public void setErsteller(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.ersteller</code>.
     */
    public String getErsteller() {
        return (String) get(14);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.bearbeiter</code>.
     */
    public void setBearbeiter(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.bearbeiter</code>.
     */
    public String getBearbeiter() {
        return (String) get(15);
    }

    /**
     * Setter for <code>pmiw21g05_v01.operation.geplant</code>.
     */
    public void setGeplant(Boolean value) {
        set(16, value);
    }

    /**
     * Getter for <code>pmiw21g05_v01.operation.geplant</code>.
     */
    public Boolean getGeplant() {
        return (Boolean) get(16);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record17 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row17<Integer, LocalDateTime, LocalDateTime, Integer, Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, Integer, Integer, Integer, String, String, Boolean> fieldsRow() {
        return (Row17) super.fieldsRow();
    }

    @Override
    public Row17<Integer, LocalDateTime, LocalDateTime, Integer, Integer, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime, Boolean, Integer, Integer, Integer, Integer, String, String, Boolean> valuesRow() {
        return (Row17) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Operation.OPERATION.OP_ID;
    }

    @Override
    public Field<LocalDateTime> field2() {
        return Operation.OPERATION.BEGINN;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Operation.OPERATION.ENDE;
    }

    @Override
    public Field<Integer> field4() {
        return Operation.OPERATION.BAUCHTUECHER_PRAE;
    }

    @Override
    public Field<Integer> field5() {
        return Operation.OPERATION.BAUCHTUECHER_POST;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Operation.OPERATION.SCHNITTZEIT;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Operation.OPERATION.NAHTZEIT;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Operation.OPERATION.ERSTELL_ZEIT;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return Operation.OPERATION.BEARBEITER_ZEIT;
    }

    @Override
    public Field<Boolean> field10() {
        return Operation.OPERATION.STORNIERT;
    }

    @Override
    public Field<Integer> field11() {
        return Operation.OPERATION.FALL_ID;
    }

    @Override
    public Field<Integer> field12() {
        return Operation.OPERATION.OP_SAAL;
    }

    @Override
    public Field<Integer> field13() {
        return Operation.OPERATION.NARKOSE_ST;
    }

    @Override
    public Field<Integer> field14() {
        return Operation.OPERATION.OP_TYP_ST;
    }

    @Override
    public Field<String> field15() {
        return Operation.OPERATION.ERSTELLER;
    }

    @Override
    public Field<String> field16() {
        return Operation.OPERATION.BEARBEITER;
    }

    @Override
    public Field<Boolean> field17() {
        return Operation.OPERATION.GEPLANT;
    }

    @Override
    public Integer component1() {
        return getOpId();
    }

    @Override
    public LocalDateTime component2() {
        return getBeginn();
    }

    @Override
    public LocalDateTime component3() {
        return getEnde();
    }

    @Override
    public Integer component4() {
        return getBauchtuecherPrae();
    }

    @Override
    public Integer component5() {
        return getBauchtuecherPost();
    }

    @Override
    public LocalDateTime component6() {
        return getSchnittzeit();
    }

    @Override
    public LocalDateTime component7() {
        return getNahtzeit();
    }

    @Override
    public LocalDateTime component8() {
        return getErstellZeit();
    }

    @Override
    public LocalDateTime component9() {
        return getBearbeiterZeit();
    }

    @Override
    public Boolean component10() {
        return getStorniert();
    }

    @Override
    public Integer component11() {
        return getFallId();
    }

    @Override
    public Integer component12() {
        return getOpSaal();
    }

    @Override
    public Integer component13() {
        return getNarkoseSt();
    }

    @Override
    public Integer component14() {
        return getOpTypSt();
    }

    @Override
    public String component15() {
        return getErsteller();
    }

    @Override
    public String component16() {
        return getBearbeiter();
    }

    @Override
    public Boolean component17() {
        return getGeplant();
    }

    @Override
    public Integer value1() {
        return getOpId();
    }

    @Override
    public LocalDateTime value2() {
        return getBeginn();
    }

    @Override
    public LocalDateTime value3() {
        return getEnde();
    }

    @Override
    public Integer value4() {
        return getBauchtuecherPrae();
    }

    @Override
    public Integer value5() {
        return getBauchtuecherPost();
    }

    @Override
    public LocalDateTime value6() {
        return getSchnittzeit();
    }

    @Override
    public LocalDateTime value7() {
        return getNahtzeit();
    }

    @Override
    public LocalDateTime value8() {
        return getErstellZeit();
    }

    @Override
    public LocalDateTime value9() {
        return getBearbeiterZeit();
    }

    @Override
    public Boolean value10() {
        return getStorniert();
    }

    @Override
    public Integer value11() {
        return getFallId();
    }

    @Override
    public Integer value12() {
        return getOpSaal();
    }

    @Override
    public Integer value13() {
        return getNarkoseSt();
    }

    @Override
    public Integer value14() {
        return getOpTypSt();
    }

    @Override
    public String value15() {
        return getErsteller();
    }

    @Override
    public String value16() {
        return getBearbeiter();
    }

    @Override
    public Boolean value17() {
        return getGeplant();
    }

    @Override
    public OperationRecord value1(Integer value) {
        setOpId(value);
        return this;
    }

    @Override
    public OperationRecord value2(LocalDateTime value) {
        setBeginn(value);
        return this;
    }

    @Override
    public OperationRecord value3(LocalDateTime value) {
        setEnde(value);
        return this;
    }

    @Override
    public OperationRecord value4(Integer value) {
        setBauchtuecherPrae(value);
        return this;
    }

    @Override
    public OperationRecord value5(Integer value) {
        setBauchtuecherPost(value);
        return this;
    }

    @Override
    public OperationRecord value6(LocalDateTime value) {
        setSchnittzeit(value);
        return this;
    }

    @Override
    public OperationRecord value7(LocalDateTime value) {
        setNahtzeit(value);
        return this;
    }

    @Override
    public OperationRecord value8(LocalDateTime value) {
        setErstellZeit(value);
        return this;
    }

    @Override
    public OperationRecord value9(LocalDateTime value) {
        setBearbeiterZeit(value);
        return this;
    }

    @Override
    public OperationRecord value10(Boolean value) {
        setStorniert(value);
        return this;
    }

    @Override
    public OperationRecord value11(Integer value) {
        setFallId(value);
        return this;
    }

    @Override
    public OperationRecord value12(Integer value) {
        setOpSaal(value);
        return this;
    }

    @Override
    public OperationRecord value13(Integer value) {
        setNarkoseSt(value);
        return this;
    }

    @Override
    public OperationRecord value14(Integer value) {
        setOpTypSt(value);
        return this;
    }

    @Override
    public OperationRecord value15(String value) {
        setErsteller(value);
        return this;
    }

    @Override
    public OperationRecord value16(String value) {
        setBearbeiter(value);
        return this;
    }

    @Override
    public OperationRecord value17(Boolean value) {
        setGeplant(value);
        return this;
    }

    @Override
    public OperationRecord values(Integer value1, LocalDateTime value2, LocalDateTime value3, Integer value4, Integer value5, LocalDateTime value6, LocalDateTime value7, LocalDateTime value8, LocalDateTime value9, Boolean value10, Integer value11, Integer value12, Integer value13, Integer value14, String value15, String value16, Boolean value17) {
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
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OperationRecord
     */
    public OperationRecord() {
        super(Operation.OPERATION);
    }

    /**
     * Create a detached, initialised OperationRecord
     */
    public OperationRecord(Integer opId, LocalDateTime beginn, LocalDateTime ende, Integer bauchtuecherPrae, Integer bauchtuecherPost, LocalDateTime schnittzeit, LocalDateTime nahtzeit, LocalDateTime erstellZeit, LocalDateTime bearbeiterZeit, Boolean storniert, Integer fallId, Integer opSaal, Integer narkoseSt, Integer opTypSt, String ersteller, String bearbeiter, Boolean geplant) {
        super(Operation.OPERATION);

        setOpId(opId);
        setBeginn(beginn);
        setEnde(ende);
        setBauchtuecherPrae(bauchtuecherPrae);
        setBauchtuecherPost(bauchtuecherPost);
        setSchnittzeit(schnittzeit);
        setNahtzeit(nahtzeit);
        setErstellZeit(erstellZeit);
        setBearbeiterZeit(bearbeiterZeit);
        setStorniert(storniert);
        setFallId(fallId);
        setOpSaal(opSaal);
        setNarkoseSt(narkoseSt);
        setOpTypSt(opTypSt);
        setErsteller(ersteller);
        setBearbeiter(bearbeiter);
        setGeplant(geplant);
    }

    /**
     * Create a detached, initialised OperationRecord
     */
    public OperationRecord(jooq.tables.pojos.Operation value) {
        super(Operation.OPERATION);

        if (value != null) {
            setOpId(value.getOpId());
            setBeginn(value.getBeginn());
            setEnde(value.getEnde());
            setBauchtuecherPrae(value.getBauchtuecherPrae());
            setBauchtuecherPost(value.getBauchtuecherPost());
            setSchnittzeit(value.getSchnittzeit());
            setNahtzeit(value.getNahtzeit());
            setErstellZeit(value.getErstellZeit());
            setBearbeiterZeit(value.getBearbeiterZeit());
            setStorniert(value.getStorniert());
            setFallId(value.getFallId());
            setOpSaal(value.getOpSaal());
            setNarkoseSt(value.getNarkoseSt());
            setOpTypSt(value.getOpTypSt());
            setErsteller(value.getErsteller());
            setBearbeiter(value.getBearbeiter());
            setGeplant(value.getGeplant());
        }
    }
}
