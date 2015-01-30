package de.uniol.inf.is.odysseus.peer.distribute.message;

import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class StopQueryMessage implements IMessage {

	private ID sharedQueryID;
	
	public StopQueryMessage() {
		
	}
	
	public StopQueryMessage( ID sharedQueryID ) {
		this.sharedQueryID = sharedQueryID;
	}
	
	@Override
	public byte[] toBytes() {
		return sharedQueryID.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		sharedQueryID = toID( new String(data));
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}
}
