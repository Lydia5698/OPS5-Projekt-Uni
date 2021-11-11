/*
 * This file is generated by jOOQ.
 */
package jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StationSt implements Serializable {

    private static final long serialVersionUID = 1L;

    private String station;
    private String bezeichnung;
    private String bezeichnungLang;
    private String stationstyp;

    public StationSt() {}

    public StationSt(StationSt value) {
        this.station = value.station;
        this.bezeichnung = value.bezeichnung;
        this.bezeichnungLang = value.bezeichnungLang;
        this.stationstyp = value.stationstyp;
    }

    public StationSt(
        String station,
        String bezeichnung,
        String bezeichnungLang,
        String stationstyp
    ) {
        this.station = station;
        this.bezeichnung = bezeichnung;
        this.bezeichnungLang = bezeichnungLang;
        this.stationstyp = stationstyp;
    }

    /**
     * Getter for <code>pmiw21g05_v01.station_st.station</code>.
     */
    public String getStation() {
        return this.station;
    }

    /**
     * Setter for <code>pmiw21g05_v01.station_st.station</code>.
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     * Getter for <code>pmiw21g05_v01.station_st.bezeichnung</code>.
     */
    public String getBezeichnung() {
        return this.bezeichnung;
    }

    /**
     * Setter for <code>pmiw21g05_v01.station_st.bezeichnung</code>.
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /**
     * Getter for <code>pmiw21g05_v01.station_st.bezeichnung_lang</code>.
     */
    public String getBezeichnungLang() {
        return this.bezeichnungLang;
    }

    /**
     * Setter for <code>pmiw21g05_v01.station_st.bezeichnung_lang</code>.
     */
    public void setBezeichnungLang(String bezeichnungLang) {
        this.bezeichnungLang = bezeichnungLang;
    }

    /**
     * Getter for <code>pmiw21g05_v01.station_st.stationstyp</code>.
     */
    public String getStationstyp() {
        return this.stationstyp;
    }

    /**
     * Setter for <code>pmiw21g05_v01.station_st.stationstyp</code>.
     */
    public void setStationstyp(String stationstyp) {
        this.stationstyp = stationstyp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StationSt (");

        sb.append(station);
        sb.append(", ").append(bezeichnung);
        sb.append(", ").append(bezeichnungLang);
        sb.append(", ").append(stationstyp);

        sb.append(")");
        return sb.toString();
    }
}
