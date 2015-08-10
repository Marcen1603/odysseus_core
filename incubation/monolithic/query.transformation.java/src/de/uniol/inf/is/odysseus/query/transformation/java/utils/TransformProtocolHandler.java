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

		boolean openwrapper = false;
		
		String wrapper = "";
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
			 openwrapper = true;
			
		}else{
			 wrapper = "IAccessPattern.PUSH";
		}
		imports.add(IAccessPattern.class.getName());
		
		TransformationInformation.getInstance().addDataHandler(protocolHandlerParameter.getDataHandler());
		TransformationInformation.getInstance().addProtocolHandler(protocolHandlerParameter.getProtocolHandler());
		TransformationInformation.getInstance().addTransportHandler(protocolHandlerParameter.getTransportHandler());
	
		StringTemplate protocolHandlerTemplate = new StringTemplate("java","protocolHandler");
	
		protocolHandlerTemplate.getSt().add("operatorVariable", operatorVariable);
		protocolHandlerTemplate.getSt().add("protocolHandlerParameter", protocolHandlerParameter);
		protocolHandlerTemplate.getSt().add("wrapper", wrapper);
		protocolHandlerTemplate.getSt().add("openwrapper", openwrapper);
		protocolHandlerTemplate.getSt().add("direction", direction);
		
		//add imports
		imports.add(IProtocolHandler.class.getName());
		imports.add(ITransportHandler.class.getName());
		imports.add(FileHandler.class.getName());
	 
		codeFragmentInfo.setCode(protocolHandlerTemplate.getSt().render());
		codeFragmentInfo.setImports(imports);
		
		return codeFragmentInfo;
	}
	
}
