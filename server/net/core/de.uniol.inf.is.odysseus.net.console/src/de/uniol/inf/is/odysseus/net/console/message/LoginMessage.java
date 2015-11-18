package de.uniol.inf.is.odysseus.net.console.message;

import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class LoginMessage implements IMessage {

	private String username;
	private String password;

	public LoginMessage() {

	}

	public LoginMessage(String username, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "username must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "password must not be null or empty!");

		this.username = username;
		this.password = password;
	}

	@Override
	public byte[] toBytes() {
		byte[] usernameBytes = username.getBytes();
		byte[] passwordBytes = password.getBytes();
		
		ByteBuffer bb = ByteBuffer.allocate(usernameBytes.length + passwordBytes.length + 4);
		
		bb.putInt(usernameBytes.length);
		bb.put(usernameBytes);
		bb.put(passwordBytes);
		
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		int usernameLength = bb.getInt();
		byte[] usernameBytes = new byte[usernameLength];
		byte[] passwordBytes = new byte[data.length - usernameLength - 4];
		
		bb.get(usernameBytes);
		bb.get(passwordBytes);
		
		username = new String(usernameBytes);
		password = new String(passwordBytes);
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}
