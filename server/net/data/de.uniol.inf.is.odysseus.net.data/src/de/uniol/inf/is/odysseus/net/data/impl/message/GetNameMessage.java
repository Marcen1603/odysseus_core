package de.uniol.inf.is.odysseus.net.data.impl.message;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class GetNameMessage implements IMessage {

	private String name;

	public GetNameMessage() {
	}

	public GetNameMessage(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		this.name = name;
	}

	@Override
	public byte[] toBytes() {
		return name.getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		name = new String(data);
	}

	public String getName() {
		return name;
	}
}
