package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

public class ProtobufTypeRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtobufTypeRegistry.class);

	static private HashMap<String, GeneratedMessage> typeRegistry = new HashMap<String, GeneratedMessage>();

	public ProtobufTypeRegistry() {

	}

	public void bindDataProvider(IProtobufDatatypeProvider provider) {
		registerMessageType(provider.getName(), provider.getMessageType());
	}

	public void unbindDataProvider(IProtobufDatatypeProvider provider) {
		deregisterMessageType(provider.getName(), provider.getMessageType());
	}

	private static void registerMessageType(String name,
			GeneratedMessage message) {
		logger.debug("Register Messagetype " + message
				+ " for Datatypes with name " + name);
		typeRegistry.put(name.toLowerCase(), message);
	}

	private static void deregisterMessageType(String name,
			GeneratedMessage message) {
		logger.debug("Deregister Messagetype " + message
				+ " for Datatypes with name " + name);
		typeRegistry.remove(name.toLowerCase());
	}

	public static GeneratedMessage getMessageType(String name) {
		return typeRegistry.get(name.toLowerCase());
	}

}
