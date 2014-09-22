package de.uniol.inf.is.odysseus.peer.console;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

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
