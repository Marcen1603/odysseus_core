package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;

public class CreateDefaultCode {
	
	
	public static String initOperator(ILogicalOperator operator){
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		StringBuilder code = new StringBuilder();
		//generate code for SDFSchema
		code.append(TransformSDFSchema.getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
				
	
		return code.toString();
	}
	
	
	public static String codeForAccessFramework(ILogicalOperator operator){
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		ITransportDirection direction = ITransportDirection.IN;
		
		String filename = "";
		String transportHandler =  "";
		String dataHandler =  "";
		String wrapper = "";
		String protocolHandler = "";
		
		if(operator instanceof CSVFileSink){
			 CSVFileSink csvFileSink = (CSVFileSink) operator;
		
			 filename = csvFileSink.getFilename();
			 transportHandler = csvFileSink.getTransportHandler();
			 dataHandler = csvFileSink.getDataHandler();
			 wrapper = csvFileSink.getWrapper();
			 protocolHandler = csvFileSink.getProtocolHandler();
			 direction = ITransportDirection.OUT;
		}
		
		if(operator instanceof CSVFileSource){
			CSVFileSource csvFileSource = (CSVFileSource) operator; 
			 
			 filename = csvFileSource.getFilename();
			 transportHandler = csvFileSource.getTransportHandler();
			 dataHandler = csvFileSource.getDataHandler();
			 wrapper = csvFileSource.getWrapper();
			 protocolHandler = csvFileSource.getProtocolHandler();
			 direction = ITransportDirection.IN;
		}
		
		

		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);	
		
		//generate code for options
		code.append(TransformCSVParameter.getCodeForParameterInfo(operator.getParameterInfos(),operatorVariable));
		
		//setup transportHandler
		code.append(TransformProtocolHandler.getCodeForProtocolHandler(protocolHandlerParameter, operatorVariable, direction));
	
		return code.toString();
	}

}
