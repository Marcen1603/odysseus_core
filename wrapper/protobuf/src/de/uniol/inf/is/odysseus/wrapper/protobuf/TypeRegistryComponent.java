package de.uniol.inf.is.odysseus.wrapper.protobuf;

public class TypeRegistryComponent {
	
	public void bindType(IProtobufDatatypeProvider type){
		ProtobufTypeRegistry.registerMessageType(type.getName(), type.getMessageType());
	}
	
	public void unbindType(IProtobufDatatypeProvider type){
		ProtobufTypeRegistry.deregisterMessageType(type.getName(), type.getMessageType());
	}

}
