package ExternalFiles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TableViewMessage {

    private StringProperty hl7Message;
    private ObjectProperty<LocalDate> dateOfMessage;
    private StringProperty ackMessage;

    /**
     * Creates a tableview object which can be inserted into the table view
     * @param hl7Message the hl7 message which is in the first column
     * @param date the date of the messagreceiving
     * @param ack "ja" if the message gots an ack and else "nein"
     */
    public TableViewMessage(String hl7Message, LocalDate date, String ack){
        this.hl7Message = new SimpleStringProperty(hl7Message);
        this.dateOfMessage = new SimpleObjectProperty<>(date);
        this.ackMessage = new SimpleStringProperty(ack);

    }

    public String getHl7Message() {
        return hl7Message.get();
    }

    public StringProperty hl7MessageProperty() {
        return hl7Message;
    }

    public void setHl7Message(String hl7Message) {
        this.hl7Message.set(hl7Message);
    }

    public LocalDate getDateOfMessage() {
        return dateOfMessage.get();
    }

    public ObjectProperty<LocalDate> dateOfMessageProperty() {
        return dateOfMessage;
    }

    public void setDateOfMessage(LocalDate dateOfMessage) {
        this.dateOfMessage.set(dateOfMessage);
    }

    public String getAckMessage() {
        return ackMessage.get();
    }

    public StringProperty ackMessageProperty() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage.set(ackMessage);
    }
}

