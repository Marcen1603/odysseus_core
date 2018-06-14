package de.uniol.inf.is.odysseus.net.rcp.views;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class ChatMessage implements IMessage {

	private String text;
	private boolean isWhisper;
	
	public ChatMessage() {
	}
	
	public ChatMessage( String text, boolean isWhisper ) {
		this.text = text;
		this.isWhisper = isWhisper;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] textBytes = text.getBytes();
		byte[] data = new byte[1 + textBytes.length];
		data[0] = (byte) (isWhisper ? 1 : 0);
		System.arraycopy(textBytes, 0, data, 1, textBytes.length);
		return data;
	}

	@Override
	public void fromBytes(byte[] data) {
		isWhisper = data[0] == 1;
		byte[] textBytes = new byte[data.length - 1];
		System.arraycopy(data, 1, textBytes, 0, textBytes.length);
		
		text = new String(textBytes);
	}

	public String getText() {
		return text;
	}
	
	public boolean isWhisper() {
		return isWhisper;
	}
}
