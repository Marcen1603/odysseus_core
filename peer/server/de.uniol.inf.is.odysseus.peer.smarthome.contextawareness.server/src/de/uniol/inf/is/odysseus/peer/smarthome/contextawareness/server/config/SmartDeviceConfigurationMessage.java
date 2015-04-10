package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.LookAheadObjectInputStream;

public abstract class SmartDeviceConfigurationMessage implements IMessage {
	protected static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		byte[] buffer = baos.toByteArray();
		oos.close();
		baos.close();
		return buffer;
	}

	protected static Object deserialize(byte[] buffer) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

		// We use LookAheadObjectInputStream instead of InputStream
		ObjectInputStream ois = new LookAheadObjectInputStream(bais);

		Object obj = ois.readObject();
		ois.close();
		bais.close();
		return obj;
	}
}
