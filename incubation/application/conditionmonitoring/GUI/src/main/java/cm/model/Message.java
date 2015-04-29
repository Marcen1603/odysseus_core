package cm.model;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class Message {

    private MessageType messageType;
    private String messageContent;

    public Message(MessageType messageType, String messageContent) {
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
