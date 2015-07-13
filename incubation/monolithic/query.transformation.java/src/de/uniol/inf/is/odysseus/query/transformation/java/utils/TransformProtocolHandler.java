package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class TransformProtocolHandler {
	
	
	/*
	IProtocolHandler cSVProtocolHandlerNeu =  protocolHandlerRegistry.getInstance("SimpleCSV", ITransportDirection.IN, IAccessPattern.PULL, csvOptions,  dataHandlerRegistry.getDataHandler("Tuple", sourceSchema));
    ITransportHandler transportHandler = new FileHandler(cSVProtocolHandlerNeu, csvOptions);
    cSVProtocolHandlerNeu.setTransportHandler(transportHandler);
        transportHandler.processInOpen();
	 
	 */


	public static CodeFragmentInfo getCodeForProtocolHandlerNeu(ProtocolHandlerParameter protocolHandlerParameter, String operatorVariable, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		Set<String> imports = new HashSet<String>();
		

		String wrapper = "";
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
			
		}else{
			 wrapper = "IAccessPattern.PUSH";
		}
		imports.add(IAccessPattern.class.getName());
		

		TransformationInformation.getInstance().addDataHandler(protocolHandlerParameter.getDataHandler());
		TransformationInformation.getInstance().addProtocolHandler(protocolHandlerParameter.getProtocolHandler());
		TransformationInformation.getInstance().addTransportHandler(protocolHandlerParameter.getTransportHandler());
		
		
		
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("IProtocolHandler "+operatorVariable+"ProtocolHandler =  protocolhandlerregistry.getInstance(\""+protocolHandlerParameter.getProtocolHandler()+"\", ITransportDirection."+direction.toString()+", "+wrapper+", "+operatorVariable+"ParameterInfo"+",  datahandlerregistry.getDataHandler(\""+protocolHandlerParameter.getDataHandler()+"\","+ operatorVariable+"SDFSchema));");
		code.append("\n");
		code.append("ITransportHandler "+ operatorVariable+"TransportHandler = transporthandlerregistry.getInstance(\""+protocolHandlerParameter.getTransportHandler()+"\","+operatorVariable+"ProtocolHandler," +operatorVariable+"ParameterInfo);");
		code.append("\n");
		code.append(operatorVariable+"ProtocolHandler.setTransportHandler("+operatorVariable+"TransportHandler);");
		code.append("\n");
		
		if(wrapper.equals("IAccessPattern.PULL")){
			code.append(operatorVariable+"TransportHandler.processInOpen();");
		}
		
		code.append("\n");
		code.append("\n");
		
		//add imports
		imports.add(IProtocolHandler.class.getName());
		imports.add(ITransportHandler.class.getName());
		imports.add(FileHandler.class.getName());
		
	 
		codeFragmentInfo.setCode(code.toString());
		codeFragmentInfo.setImports(imports);
		
		
		return codeFragmentInfo;
	}
	
}
