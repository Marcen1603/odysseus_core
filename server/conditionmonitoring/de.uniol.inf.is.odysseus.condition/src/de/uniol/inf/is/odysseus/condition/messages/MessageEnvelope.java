package de.uniol.inf.is.odysseus.condition.messages;

public class MessageEnvelope {
	
	private MessageType messageType;
	private String messageContent;
	
	
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
