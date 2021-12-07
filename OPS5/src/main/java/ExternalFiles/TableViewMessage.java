package ExternalFiles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TableViewMessage {

    private StringProperty hl7Message;

    public LocalDate getDateOfMessage() {
        return dateOfMessage.get();
    }

    public ObjectProperty<LocalDate> dateOfMessageProperty() {
        return dateOfMessage;
    }

    public void setDateOfMessage(LocalDate dateOfMessage) {
        this.dateOfMessage.set(dateOfMessage);
    }

    private ObjectProperty<LocalDate> dateOfMessage;

    public String getAckMessage() {
        return ackMessage.get();
    }

    public StringProperty ackMessageProperty() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage.set(ackMessage);
    }

    private StringProperty ackMessage;

    public String getHl7Message() {
        return hl7Message.get();
    }

    public StringProperty hl7MessageProperty() {
        return hl7Message;
    }

    public void setHl7Message(String hl7Message) {
        this.hl7Message.set(hl7Message);
    }


}

