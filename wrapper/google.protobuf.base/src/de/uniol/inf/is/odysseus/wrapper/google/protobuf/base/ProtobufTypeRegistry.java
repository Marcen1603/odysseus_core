package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class ProtobufTypeRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtobufTypeRegistry.class);
	
	static ProtobufTypeRegistry instance = new ProtobufTypeRegistry();

	private HashMap<String, GeneratedMessage> typeRegistry = new HashMap<String, GeneratedMessage>();
	
	private ProtobufTypeRegistry() {
		registerMessageType(CDataPoint.getDescriptor().getFullName(), CDataPoint.getDefaultInstance());
	}	
	
	public static ProtobufTypeRegistry getInstance(){
		return instance;
	}
	

	public void registerMessageType(String name, GeneratedMessage message) {
			logger.debug("Register Messagetype " + message
					+ " for Datatypes with name " + name);
			typeRegistry.put(name.toLowerCase(), message);
	}

	public GeneratedMessage getMessageType(String name) {
		return typeRegistry.get(name.toLowerCase());
	}

}
