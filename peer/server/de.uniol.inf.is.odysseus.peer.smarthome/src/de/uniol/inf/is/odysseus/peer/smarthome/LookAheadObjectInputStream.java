package de.uniol.inf.is.odysseus.peer.smarthome;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public class LookAheadObjectInputStream extends ObjectInputStream {

	public LookAheadObjectInputStream(InputStream inputStream)
			throws IOException {
		super(inputStream);
	}

	/**
	 * Only deserialize instances of our expected SmartDeviceConfig class
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		return super.resolveClass(desc);
		
		/*
		//
		if (desc.getName().equals(SmartDeviceConfig.class.getName()) || desc.getName().equals(SmartDevice.class.getName()) || desc.getName().equals(ArrayList.class.getName())) {
			return super.resolveClass(desc);
		}else{
			throw new InvalidClassException(
					"Unauthorized deserialization attempt", desc.getName());
		}
		*/
	}
}
