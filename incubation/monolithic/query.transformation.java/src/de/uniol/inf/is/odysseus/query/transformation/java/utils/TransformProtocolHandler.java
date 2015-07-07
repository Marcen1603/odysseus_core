package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.NeededDataHandler;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.NeededProtocolHandler;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.NeededTransportHandler;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;

public class TransformProtocolHandler {
	
	
	/*
	IProtocolHandler cSVProtocolHandlerNeu =  protocolHandlerRegistry.getInstance("SimpleCSV", ITransportDirection.IN, IAccessPattern.PULL, csvOptions,  dataHandlerRegistry.getDataHandler("Tuple", sourceSchema));
    ITransportHandler transportHandler = new FileHandler(cSVProtocolHandlerNeu, csvOptions);
    cSVProtocolHandlerNeu.setTransportHandler(transportHandler);
        transportHandler.processInOpen();
	 
	 */
	public static String getCodeForProtocolHandler(ProtocolHandlerParameter protocolHandlerParameter, String operatorVariable){
		String wrapper = "";
	
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
		}else{
			 wrapper = "IAccessPattern.PUSH";
		}
		
		NeededDataHandler.addDataHandler(DataHandlerRegistry.getIDataHandlerClass(protocolHandlerParameter.getDataHandler()).getClass().getName(),DataHandlerRegistry.getIDataHandlerClass(protocolHandlerParameter.getDataHandler()).getClass().getSimpleName());
		NeededTransportHandler.addTransportHandler(TransportHandlerRegistry.getITransportHandlerClass(protocolHandlerParameter.getTransportHandler()).getClass().getName(), TransportHandlerRegistry.getITransportHandlerClass(protocolHandlerParameter.getTransportHandler()).getClass().getSimpleName());
	
		
		NeededProtocolHandler.addProtocolHandler(ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerParameter.getProtocolHandler()).getClass().getName(), ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerParameter.getProtocolHandler()).getClass().getSimpleName());
		
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("IProtocolHandler "+operatorVariable+"ProtocolHandler =  protocolhandlerregistry.getInstance(\""+protocolHandlerParameter.getProtocolHandler()+"\", ITransportDirection.IN, "+wrapper+", "+operatorVariable+"ParameterInfo"+",  datahandlerregistry.getDataHandler(\""+protocolHandlerParameter.getDataHandler()+"\","+ operatorVariable+"SDFSchema));");
		code.append("\n");
		code.append("ITransportHandler "+operatorVariable+"TransportHandler = new FileHandler("+operatorVariable+"ProtocolHandler, "+operatorVariable+"ParameterInfo);");
		code.append("\n");
		code.append(operatorVariable+"ProtocolHandler.setTransportHandler("+operatorVariable+"TransportHandler);");
		code.append("\n");
		code.append(operatorVariable+"TransportHandler.processInOpen();");
		
		code.append("\n");
		code.append("\n");
	 
		return code.toString();
	}

}
