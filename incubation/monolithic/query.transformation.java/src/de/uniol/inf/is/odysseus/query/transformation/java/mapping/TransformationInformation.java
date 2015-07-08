package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

public class TransformationInformation {

	private static TransformationInformation instance = null;

	private Map<String, String> dataHandler = new HashMap<String, String>();
	private Map<String, String> mepFunctions = new HashMap<String, String>();
	private Map<String, String> protocolHandler = new HashMap<String, String>();
	private Map<String, String> transportHandler = new HashMap<String, String>();

	public static TransformationInformation getInstance() {
		if (instance == null) {
			instance = new TransformationInformation();
		}
		return instance;
	}

	
	public void addDataHandler(String datatype){
		String fullClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getName();
		String simpleClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getSimpleName();
		
		if (!dataHandler.containsKey(fullClassName)) {
			dataHandler.put(fullClassName, simpleClassName);
		}
	}
	


	public Map<String, String> getNeededDataHandler() {
		return dataHandler;
	}

	
	public void addMEPFunction(IExpression<?> mepExpression) {

	String fullClassName =	mepExpression.toFunction().getClass().getName();
	String simpleClassName = mepExpression.toFunction().getClass().getSimpleName();

		if (!mepFunctions.containsKey(fullClassName)) {
			mepFunctions.put(fullClassName, simpleClassName);
		}

	}
	

	public Map<String, String> getNeededMEPFunctions() {
		return mepFunctions;
	}
	

	public void addProtocolHandler(String protocolHandlerString) {
	
		String fullClassName = ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerString).getClass().getName();
		String simpleClassName = ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerString).getClass().getSimpleName();

		if (!protocolHandler.containsKey(fullClassName)) {
			protocolHandler.put(fullClassName, simpleClassName);
		}
	}
	


	public Map<String, String> getNeededProtocolHandler() {
		return protocolHandler;
	}

	
	
	public void addTransportHandler(String transportHandlerString) {
		
		String fullClassName = TransportHandlerRegistry.getITransportHandlerClass(transportHandlerString).getClass().getName();
		String simpleClassName = TransportHandlerRegistry.getITransportHandlerClass(transportHandlerString).getClass().getSimpleName();
		
		if (!transportHandler.containsKey(fullClassName)) {
			transportHandler.put(fullClassName, simpleClassName);
		}
		
	}
	

	public Map<String, String> getNeededTransportHandler() {
		return transportHandler;
	}

}
