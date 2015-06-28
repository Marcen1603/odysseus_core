package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;

public class TransformProtocolHandler {
	
	
	/*
	IProtocolHandler cSVProtocolHandlerNeu =  protocolHandlerRegistry.getInstance("SimpleCSV", ITransportDirection.IN, IAccessPattern.PULL, csvOptions,  dataHandlerRegistry.getDataHandler("Tuple", sourceSchema));
    ITransportHandler transportHandler = new FileHandler(cSVProtocolHandlerNeu, csvOptions);
    cSVProtocolHandlerNeu.setTransportHandler(transportHandler);
        transportHandler.processInOpen();
	 
	 */
	public static String getCodeForProtocolHandler(ProtocolHandlerParameter protocolHandlerParameter){
		String wrapper = "";
	
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
		}else{
			 wrapper = "IAccessPattern.PUSH";
			 
		}
	
		StringBuilder code = new StringBuilder();
	
		code.append("\n");
		code.append("IProtocolHandler cSVProtocolHandlerNeu =  protocolHandlerRegistry.getInstance(\""+protocolHandlerParameter.getProtocolHandler()+"\", ITransportDirection.IN, "+wrapper+", csvOptions,  dataHandlerRegistry.getDataHandler(\""+protocolHandlerParameter.getDataHandler()+"\", sourceSchema));");
		code.append("\n");
		code.append("ITransportHandler transportHandler = new FileHandler(cSVProtocolHandlerNeu, csvOptions);");
		code.append("\n");
		code.append("cSVProtocolHandlerNeu.setTransportHandler(transportHandler);");
		code.append("\n");
		code.append("transportHandler.processInOpen();");
		
		code.append("\n");
		code.append("\n");
	 
		return code.toString();
	}

}
