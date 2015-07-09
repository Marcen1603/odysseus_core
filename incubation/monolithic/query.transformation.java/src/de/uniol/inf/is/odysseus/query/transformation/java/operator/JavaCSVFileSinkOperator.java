package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformCSVParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformProtocolHandler;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class JavaCSVFileSinkOperator extends AbstractTransformationOperator{
	
	private final String name =  "CSVFILESINK";
	private final String targetPlatform = "Java";
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();
		
		//code.append(CreateDefaultCode.initOperator(operator));
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		CSVFileSink csvFileSink = (CSVFileSink) operator;
		
		String filename = csvFileSink.getFilename();
		String transportHandler = csvFileSink.getTransportHandler();
		String dataHandler = csvFileSink.getDataHandler();
		String wrapper = csvFileSink.getWrapper();
		String protocolHandler = csvFileSink.getProtocolHandler();
		
	
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);	
		
		//generate code for options
		code.append(TransformCSVParameter.getCodeForParameterInfo(csvFileSink.getParameterInfos(),operatorVariable));
		
		//setup transportHandler
		code.append(TransformProtocolHandler.getCodeForProtocolHandler(protocolHandlerParameter, operatorVariable, ITransportDirection.OUT));
	
		//now create the SenderPO
		code.append("SenderPO "+operatorVariable+"PO = new SenderPO("+operatorVariable+"ProtocolHandler);");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}

	@Override
	public Set<String> getNeededImports() {
		Set<String> imoportList = new HashSet<String>();
		imoportList.add(SenderPO.class.getPackage().getName()+"."+SenderPO.class.getSimpleName());
		return imoportList;
	}
}


