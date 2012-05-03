package de.uniol.inf.is.odysseus.wrapper.protobuf;

import com.google.protobuf.GeneratedMessage;

public interface IProtobufDatatypeProvider {

	String getName();
	GeneratedMessage getMessageType();
	
}
