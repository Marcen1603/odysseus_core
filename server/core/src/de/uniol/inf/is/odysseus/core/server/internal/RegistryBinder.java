package de.uniol.inf.is.odysseus.core.server.internal;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class RegistryBinder {
	
	private static ITransportHandlerRegistry transportHandlerRegistry;
	static IProtocolHandlerRegistry protocolHandlerRegistry;
	static IDataHandlerRegistry dataHandlerRegistry;
	

	// Methods called by OSGi
	
	public void bindTransportHandlerRegistry(ITransportHandlerRegistry registry) {
		transportHandlerRegistry = registry;
	}	

	public void unbindTransportHandlerRegistry(ITransportHandlerRegistry registry) {
		transportHandlerRegistry = null;
	}

	public void bindProtocolHandlerRegistry(IProtocolHandlerRegistry registry) {
		protocolHandlerRegistry = registry;
	}

	public void unbindProtocolHandlerRegistry(IProtocolHandlerRegistry registry) {
		protocolHandlerRegistry = null;
	}
	
	public void bindDataHandlerRegistry(IDataHandlerRegistry registry) {
		dataHandlerRegistry = registry;
	}
	
	public void unbindDataHandlerRegistry(IDataHandlerRegistry registry) {
		dataHandlerRegistry = null;
	}
	
	// Get the Handlers	
	
	public static ITransportHandlerRegistry getTransportHandlerRegistry() {
		return transportHandlerRegistry;
	}
	
	public static IProtocolHandlerRegistry getProtocolHandlerRegistry() {
		return protocolHandlerRegistry;
	}

	public static IDataHandlerRegistry getDataHandlerRegistry() {
		return dataHandlerRegistry;
	}
	
	public static ITransportHandlerRegistry getTransportHandlerRegistry(ITenant tenant) {
		return transportHandlerRegistry;
	}
	
	public static IProtocolHandlerRegistry getProtocolHandlerRegistry(ITenant tenant) {
		return protocolHandlerRegistry;
	}

	public static IDataHandlerRegistry getDataHandlerRegistry(ITenant tenant) {
		return dataHandlerRegistry;
	}

}

