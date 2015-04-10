package de.uniol.inf.is.odysseus.peer.console;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class CommandOutputMessage implements IMessage {

	private String output;
	
	public CommandOutputMessage() {
		
	}
	
	public CommandOutputMessage( String output ) {
		this.output = output;
	}
	
	@Override
	public byte[] toBytes() {
		return output.getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		output = new String(data);
	}
	
	public String getOutput() {
		return output;
	}

}
