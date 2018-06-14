package de.uniol.inf.is.odysseus.net.console.message;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class CommandMessage implements IMessage {

	private String commandString;
	
	public CommandMessage() {
		
	}
	
	public CommandMessage( String commandString ) {
		this.commandString = commandString;
	}
	
	@Override
	public byte[] toBytes() {
		return commandString.getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		commandString = new String(data);
	}
	
	public String getCommandString() {
		return commandString;
	}
}
