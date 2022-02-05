package ExternalFiles;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

/**
 * This represents a line in the overview in the communicationscontroller so it includes the hl7 message
 * the date and the ackmessage if the message was correctly handled or not
 */
public class TableViewMessage {

    private final StringProperty hl7Message;
    private final ObjectProperty<LocalDateTime> dateOfMessage;
    private final StringProperty ackMessage;

    /**
     * Creates a tableview object which can be inserted into the table view
     * @param hl7Message the hl7 message which is in the first column
     * @param date the date of the message receiving
     * @param ack "ja" if the message got an ack and else "nein"
     */
    public TableViewMessage(String hl7Message, LocalDateTime date, String ack){
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

    public LocalDateTime getDateOfMessage() {
        return dateOfMessage.get();
    }

    public ObjectProperty<LocalDateTime> dateOfMessageProperty() {
        return dateOfMessage;
    }

    public StringProperty ackMessageProperty() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage.set(ackMessage);
    }
}

