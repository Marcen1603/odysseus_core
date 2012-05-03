package de.uniol.inf.is.odysseus.wrapper.protobuf;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

public class ProtobufTypeRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtobufTypeRegistry.class);

	static private HashMap<String, GeneratedMessage> typeRegistry = new HashMap<String, GeneratedMessage>();

	private ProtobufTypeRegistry(){
	}
	
	static void registerMessageType(String name,
			GeneratedMessage message) {
		logger.debug("Register Messagetype " + message
				+ " for Datatypes with name " + name);
		typeRegistry.put(name.toLowerCase(), message);
	}

	static void deregisterMessageType(String name,
			GeneratedMessage message) {
		logger.debug("Deregister Messagetype " + message
				+ " for Datatypes with name " + name);
		typeRegistry.remove(name.toLowerCase());
	}

	public static GeneratedMessage getMessageType(String name) {
		return typeRegistry.get(name.toLowerCase());
	}

}
