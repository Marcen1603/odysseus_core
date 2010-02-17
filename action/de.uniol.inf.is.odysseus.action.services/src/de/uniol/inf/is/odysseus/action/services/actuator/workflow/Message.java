package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Class representing a message for a webservice call.
 * Holds a number of {@link MessagePart} 
 * @author Simon Flandergan
 *
 */
public class Message {
	private QName messageName;
	private List<MessagePart> parts;
	
	public Message(QName name){
		this.parts = new ArrayList<MessagePart>();
		this.messageName = name;
	}
	
	public void addMessagePart(MessagePart part){
		this.parts.add(part);
	}
	
	public QName getMessageName() {
		return messageName;
	}
	
	public List<MessagePart> getParts() {
		return parts;
	}
	
	

}
