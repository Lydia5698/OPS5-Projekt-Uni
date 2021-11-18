package controller;

import java.time.LocalDateTime;
import main.Main;


public class OpObject {

    private Integer opID;
    private LocalDateTime beginn;
    private LocalDateTime ende;

    public OpObject(Integer opID, LocalDateTime beginn, LocalDateTime ende) {
        this.opID = opID;
        this.beginn = beginn;
        this.ende = ende;
    }
    public Integer getOpID() {
        return opID;
    }

    public void setOpID(Integer opID) {
        this.opID = opID;
    }

    public LocalDateTime getBeginn() {
        return beginn;
    }

    public void setBeginn(LocalDateTime beginn) {
        this.beginn = beginn;
    }

    public LocalDateTime getEnde() {
        return ende;
    }

    public void setEnde(LocalDateTime ende) {
        this.ende = ende;
    }



}