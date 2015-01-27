package de.uniol.inf.is.odysseus.wrapper.snet.communication.util;

import java.nio.ByteBuffer;

public class WSNMessage {
	
	private byte[] WSNFrame;
	private byte[] message;
	private Command command;
	private byte[] payload;
	
	public enum Command {
		REGISTER_APPLICATION_REQ (0x0100),
		REGISTER_APPLICATION_CFM (0x0101),
		UNREGISTER_APPLICATION_REQ (0x0110),
		UNREGISTER_APPLICATION_CFM (0x0111),
		SUBSCRIBE_SERVICE_REQ (0x0200),
		SUBSCRIBE_SERVICE_CFM (0x0201),
		UNSUBSCRIBE_SERVICE_REQ (0x0210),
		UNSUBSCRIBE_SERVICE_CFM (0x0211),
		WSN_MESSAGE_REQ (0x0300),
		WSN_MESSAGE_CFM (0x0301),
		WSN_MESSAGE_IND (0x0302),
		SHUTDOWN_IND (0x0402);
		
		public final byte[] byteCode;
		Command (int code){
			ByteBuffer b = ByteBuffer.allocate(2);
			b.putInt(code);
			byteCode = b.array();
		}
	}
	
	public WSNMessage(Command command, byte[] payload){
		
		setCommand(command);
		setPayload(payload);
		// Create msg
		ByteBuffer msg = ByteBuffer.allocate(getCommand().length + getPayload().length);
		msg.put(getCommand());
		msg.put(getMessage());
		setMessage(msg.array());
		// Create frame
		ByteBuffer frm = ByteBuffer.allocate(getMessage().length + 2);
		frm.putInt(0x7E);
		frm.put(getMessage());
		frm.putInt(0x7E);
		setWSNFrame(frm.array());
	}

	public WSNMessage(byte[] frame) {
		// Extract message from frame
		setWSNFrame(frame);
		setMessage(new byte[frame.length-2]);
		if(frame[0] == 0x7E && frame[frame.length] == 0x7E){
			for(int i = 1; i < frame.length -1; i++){
				getMessage()[i-1] = frame[i];
			}
		}
		// Extract command and payload from message
		byte[] cmd = new byte[2];
		setPayload(new byte[getMessage().length - 2]);
		for(int i = 0; i < getMessage().length; i++){
			if(i <= 2){
				cmd[i] = getMessage()[i];
			} else {
				getPayload()[i - 2] = getMessage()[i];
			}
		}
		setCommand(cmd);
	}

	public byte[] getWSNFrame() {
		return WSNFrame;
	}

	private void setWSNFrame(byte[] wSNFrame) {
		WSNFrame = wSNFrame;
	}

	private byte[] getMessage() {
		return message;
	}

	private void setMessage(byte[] message) {
		this.message = message;
	}

	public byte[] getCommand() {
		return command.byteCode;
	}
	
	private void setCommand(Command cmd) {
		this.command = cmd;
	}

	private void setCommand(byte[] command) {
		for(Command cmd : Command.values()){
			if(command.equals(cmd.byteCode)){
				this.command = cmd;
			}
		}
	}

	public byte[] getPayload() {
		return payload;
	}

	private void setPayload(byte[] payload) {
		this.payload = payload;
	}
}
