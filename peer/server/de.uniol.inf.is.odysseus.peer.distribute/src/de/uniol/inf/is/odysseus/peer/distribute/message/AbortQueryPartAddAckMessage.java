package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class AbortQueryPartAddAckMessage implements IMessage {

	private ID sharedQueryID;
	
	public AbortQueryPartAddAckMessage(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}
	
	public AbortQueryPartAddAckMessage() {
		
	}

	@Override
	public byte[] toBytes() {
		return sharedQueryID.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		String idString = new String(data);
		sharedQueryID = toID(idString);
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}
	
	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}
}
