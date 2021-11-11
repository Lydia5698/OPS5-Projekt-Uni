/*
 * This file is generated by jOOQ.
 */
package jooq;


import jooq.tables.Diagnose;
import jooq.tables.FachrichtungStation;
import jooq.tables.Fall;
import jooq.tables.MedPersonal;
import jooq.tables.Operation;
import jooq.tables.Patient;
import jooq.tables.Prozedur;
import jooq.tables.Rolle;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in pmiw21g05_v01.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index DIAGNOSE_FK_DIAGNOSE_DIAGNOSETYP_ST1_IDX = Internal.createIndex(DSL.name("fk_diagnose_diagnosetyp_st1_idx"), Diagnose.DIAGNOSE, new OrderField[] { Diagnose.DIAGNOSE.DIAGNOSETYP }, false);
    public static final Index DIAGNOSE_FK_DIAGNOSE_ICD10_CODE_ST1_IDX = Internal.createIndex(DSL.name("fk_diagnose_icd10_code_st1_idx"), Diagnose.DIAGNOSE, new OrderField[] { Diagnose.DIAGNOSE.ICD10_CODE }, false);
    public static final Index DIAGNOSE_FK_DIAGNOSE_MED_PERSONAL1_IDX = Internal.createIndex(DSL.name("fk_diagnose_med_personal1_idx"), Diagnose.DIAGNOSE, new OrderField[] { Diagnose.DIAGNOSE.ERSTELLER }, false);
    public static final Index DIAGNOSE_FK_DIAGNOSE_MED_PERSONAL2_IDX = Internal.createIndex(DSL.name("fk_diagnose_med_personal2_idx"), Diagnose.DIAGNOSE, new OrderField[] { Diagnose.DIAGNOSE.BEARBEITER }, false);
    public static final Index DIAGNOSE_FK_DIAGNOSE_OPERATION1_IDX = Internal.createIndex(DSL.name("fk_diagnose_operation1_idx"), Diagnose.DIAGNOSE, new OrderField[] { Diagnose.DIAGNOSE.OP_ID }, false);
    public static final Index FACHRICHTUNG_STATION_FK_FACHRICHTUNG_STATION_FACHRICHTUNG_ST1_IDX = Internal.createIndex(DSL.name("fk_fachrichtung_station_fachrichtung_st1_idx"), FachrichtungStation.FACHRICHTUNG_STATION, new OrderField[] { FachrichtungStation.FACHRICHTUNG_STATION.FACHRICHTUNG_ST }, false);
    public static final Index FACHRICHTUNG_STATION_FK_FACHRICHTUNG_STATION_STATION_ST1_IDX = Internal.createIndex(DSL.name("fk_fachrichtung_station_station_st1_idx"), FachrichtungStation.FACHRICHTUNG_STATION, new OrderField[] { FachrichtungStation.FACHRICHTUNG_STATION.STATION_ST }, false);
    public static final Index FALL_FK_FALL_MED_PERSONAL1_IDX = Internal.createIndex(DSL.name("fk_fall_med_personal1_idx"), Fall.FALL, new OrderField[] { Fall.FALL.ERSTELLER }, false);
    public static final Index FALL_FK_FALL_MED_PERSONAL2_IDX = Internal.createIndex(DSL.name("fk_fall_med_personal2_idx"), Fall.FALL, new OrderField[] { Fall.FALL.BEARBEITER }, false);
    public static final Index FALL_FK_FALL_PATIENT1_IDX = Internal.createIndex(DSL.name("fk_fall_patient1_idx"), Fall.FALL, new OrderField[] { Fall.FALL.PAT_ID }, false);
    public static final Index FALL_FK_FALL_STATION_ST1_IDX = Internal.createIndex(DSL.name("fk_fall_station_st1_idx"), Fall.FALL, new OrderField[] { Fall.FALL.STATION_ST }, false);
    public static final Index MED_PERSONAL_FK_MED_PERSONAL_FACHRICHTUNG_ST1_IDX = Internal.createIndex(DSL.name("fk_med_personal_fachrichtung_st1_idx"), MedPersonal.MED_PERSONAL, new OrderField[] { MedPersonal.MED_PERSONAL.FACHRICHTUNG }, false);
    public static final Index OPERATION_FK_OPERATION_FALL1_IDX = Internal.createIndex(DSL.name("fk_operation_fall1_idx"), Operation.OPERATION, new OrderField[] { Operation.OPERATION.FALL_ID }, false);
    public static final Index OPERATION_FK_OPERATION_MED_PERSONAL1_IDX = Internal.createIndex(DSL.name("fk_operation_med_personal1_idx"), Operation.OPERATION, new OrderField[] { Operation.OPERATION.ERSTELLER }, false);
    public static final Index OPERATION_FK_OPERATION_MED_PERSONAL2_IDX = Internal.createIndex(DSL.name("fk_operation_med_personal2_idx"), Operation.OPERATION, new OrderField[] { Operation.OPERATION.BEARBEITER }, false);
    public static final Index OPERATION_FK_OPERATION_NARKOSE_ST1_IDX = Internal.createIndex(DSL.name("fk_operation_narkose_st1_idx"), Operation.OPERATION, new OrderField[] { Operation.OPERATION.NARKOSE_ST }, false);
    public static final Index OPERATION_FK_OPERATION_OP_TYP_ST1_IDX = Internal.createIndex(DSL.name("fk_operation_op_typ_st1_idx"), Operation.OPERATION, new OrderField[] { Operation.OPERATION.OP_TYP_ST }, false);
    public static final Index PATIENT_FK_PATIENT_BLUTGRUPPE_ST1_IDX = Internal.createIndex(DSL.name("fk_patient_blutgruppe_st1_idx"), Patient.PATIENT, new OrderField[] { Patient.PATIENT.BLUTGRUPPE }, false);
    public static final Index PATIENT_FK_PATIENT_GESCHLECHT_ST1_IDX = Internal.createIndex(DSL.name("fk_patient_geschlecht_st1_idx"), Patient.PATIENT, new OrderField[] { Patient.PATIENT.GESCHLECHT }, false);
    public static final Index PROZEDUR_FK_PROZEDUR_MED_PERSONAL1_IDX = Internal.createIndex(DSL.name("fk_prozedur_med_personal1_idx"), Prozedur.PROZEDUR, new OrderField[] { Prozedur.PROZEDUR.BEARBEITER }, false);
    public static final Index PROZEDUR_FK_PROZEDUR_MED_PERSONAL2_IDX = Internal.createIndex(DSL.name("fk_prozedur_med_personal2_idx"), Prozedur.PROZEDUR, new OrderField[] { Prozedur.PROZEDUR.ERSTELLER }, false);
    public static final Index PROZEDUR_FK_PROZEDUR_OPERATION1_IDX = Internal.createIndex(DSL.name("fk_prozedur_operation1_idx"), Prozedur.PROZEDUR, new OrderField[] { Prozedur.PROZEDUR.OP_ID }, false);
    public static final Index PROZEDUR_FK_PROZEDUR_OPS_CODE_ST1_IDX = Internal.createIndex(DSL.name("fk_prozedur_ops_code_st1_idx"), Prozedur.PROZEDUR, new OrderField[] { Prozedur.PROZEDUR.OPS_CODE }, false);
    public static final Index ROLLE_FK_ROLLE_MED_PERSONAL1_IDX = Internal.createIndex(DSL.name("fk_rolle_med_personal1_idx"), Rolle.ROLLE, new OrderField[] { Rolle.ROLLE.BEARBEITER }, false);
    public static final Index ROLLE_FK_ROLLE_MED_PERSONAL2_IDX = Internal.createIndex(DSL.name("fk_rolle_med_personal2_idx"), Rolle.ROLLE, new OrderField[] { Rolle.ROLLE.ERSTELLER }, false);
    public static final Index ROLLE_FK_ROLLE_MED_PERSONAL3_IDX = Internal.createIndex(DSL.name("fk_rolle_med_personal3_idx"), Rolle.ROLLE, new OrderField[] { Rolle.ROLLE.MED_PERSONAL_PERS_ID }, false);
    public static final Index ROLLE_FK_ROLLE_OPERATION1_IDX = Internal.createIndex(DSL.name("fk_rolle_operation1_idx"), Rolle.ROLLE, new OrderField[] { Rolle.ROLLE.OP_ID }, false);
    public static final Index ROLLE_FK_ROLLE_ROLLE_ST1_IDX = Internal.createIndex(DSL.name("fk_rolle_rolle_st1_idx"), Rolle.ROLLE, new OrderField[] { Rolle.ROLLE.ROLLE_ST }, false);
}
