/*
 * This file is generated by jOOQ.
 */
package jooq.tables.daos;


import java.time.LocalDateTime;
import java.util.List;

import jooq.tables.Fall;
import jooq.tables.records.FallRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FallDao extends DAOImpl<FallRecord, jooq.tables.pojos.Fall, Integer> {

    /**
     * Create a new FallDao without any configuration
     */
    public FallDao() {
        super(Fall.FALL, jooq.tables.pojos.Fall.class);
    }

    /**
     * Create a new FallDao with an attached configuration
     */
    public FallDao(Configuration configuration) {
        super(Fall.FALL, jooq.tables.pojos.Fall.class, configuration);
    }

    @Override
    public Integer getId(jooq.tables.pojos.Fall object) {
        return object.getFallId();
    }

    /**
     * Fetch records that have <code>fall_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfFallId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Fall.FALL.FALL_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>fall_id IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByFallId(Integer... values) {
        return fetch(Fall.FALL.FALL_ID, values);
    }

    /**
     * Fetch a unique record that has <code>fall_id = value</code>
     */
    public jooq.tables.pojos.Fall fetchOneByFallId(Integer value) {
        return fetchOne(Fall.FALL.FALL_ID, value);
    }

    /**
     * Fetch records that have <code>aufnahmedatum BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfAufnahmedatum(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Fall.FALL.AUFNAHMEDATUM, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>aufnahmedatum IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByAufnahmedatum(LocalDateTime... values) {
        return fetch(Fall.FALL.AUFNAHMEDATUM, values);
    }

    /**
     * Fetch records that have <code>entlassungsdatum BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfEntlassungsdatum(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Fall.FALL.ENTLASSUNGSDATUM, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>entlassungsdatum IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByEntlassungsdatum(LocalDateTime... values) {
        return fetch(Fall.FALL.ENTLASSUNGSDATUM, values);
    }

    /**
     * Fetch records that have <code>erstell_zeit BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfErstellZeit(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Fall.FALL.ERSTELL_ZEIT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>erstell_zeit IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByErstellZeit(LocalDateTime... values) {
        return fetch(Fall.FALL.ERSTELL_ZEIT, values);
    }

    /**
     * Fetch records that have <code>bearbeiter_zeit BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfBearbeiterZeit(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Fall.FALL.BEARBEITER_ZEIT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>bearbeiter_zeit IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByBearbeiterZeit(LocalDateTime... values) {
        return fetch(Fall.FALL.BEARBEITER_ZEIT, values);
    }

    /**
     * Fetch records that have <code>storniert BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfStorniert(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Fall.FALL.STORNIERT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>storniert IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByStorniert(Boolean... values) {
        return fetch(Fall.FALL.STORNIERT, values);
    }

    /**
     * Fetch records that have <code>pat_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfPatId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Fall.FALL.PAT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>pat_id IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByPatId(Integer... values) {
        return fetch(Fall.FALL.PAT_ID, values);
    }

    /**
     * Fetch records that have <code>station_st BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfStationSt(String lowerInclusive, String upperInclusive) {
        return fetchRange(Fall.FALL.STATION_ST, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>station_st IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByStationSt(String... values) {
        return fetch(Fall.FALL.STATION_ST, values);
    }

    /**
     * Fetch records that have <code>ersteller BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfErsteller(String lowerInclusive, String upperInclusive) {
        return fetchRange(Fall.FALL.ERSTELLER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ersteller IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByErsteller(String... values) {
        return fetch(Fall.FALL.ERSTELLER, values);
    }

    /**
     * Fetch records that have <code>bearbeiter BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfBearbeiter(String lowerInclusive, String upperInclusive) {
        return fetchRange(Fall.FALL.BEARBEITER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>bearbeiter IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByBearbeiter(String... values) {
        return fetch(Fall.FALL.BEARBEITER, values);
    }

    /**
     * Fetch records that have <code>fall_typ BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<jooq.tables.pojos.Fall> fetchRangeOfFallTyp(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Fall.FALL.FALL_TYP, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>fall_typ IN (values)</code>
     */
    public List<jooq.tables.pojos.Fall> fetchByFallTyp(Integer... values) {
        return fetch(Fall.FALL.FALL_TYP, values);
    }
}
