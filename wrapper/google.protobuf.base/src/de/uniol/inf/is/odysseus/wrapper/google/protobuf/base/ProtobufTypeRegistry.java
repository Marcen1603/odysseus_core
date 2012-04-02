package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

public class ProtobufTypeRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtobufTypeRegistry.class);

	private static HashMap<String, GeneratedMessage> typeRegistry = new HashMap<String, GeneratedMessage>();
	
	public ProtobufTypeRegistry() {
		logger.debug("Creating Protbuf Type Registry");
	}
	
	public static void registerMessageType(GeneratedMessage message) {
		String name = message.getDescriptorForType().getFullName();
		logger.debug("Register Messagetype " + message
				+ " for Datatypes with name " + name);
		typeRegistry.put(name.toLowerCase(), message);
	}
	
	public static GeneratedMessage getMessageType(String name) {
		return typeRegistry.get(name);
	}

}
