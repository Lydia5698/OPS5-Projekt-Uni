package controller;

import java.time.LocalDateTime;
import main.Main;


public class FallObject {

    private Integer fallID;
    private LocalDateTime aufnahmedatum;
    private Integer stationär;

    public FallObject(Integer fallID, LocalDateTime aufnahmedatum, Integer stationär) {
        this.fallID = fallID;
        this.aufnahmedatum = aufnahmedatum;
        this.stationär = stationär;
    }

    public Integer getFallID() {
        return fallID;
    }

    public void setFallID(Integer fallID) {
        this.fallID = fallID;
    }

    public LocalDateTime getAufnahmedatum() {
        return aufnahmedatum;
    }

    public void setAufnahmedatum(LocalDateTime aufnahmedatum) {
        this.aufnahmedatum = aufnahmedatum;
    }

    public Integer getStationär() {
        return stationär;
    }

    public void setStationär(Integer stationär) {
        this.stationär = stationär;
    }

}