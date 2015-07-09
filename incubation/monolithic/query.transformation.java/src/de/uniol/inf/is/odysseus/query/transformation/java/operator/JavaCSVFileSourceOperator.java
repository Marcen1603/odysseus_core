package de.uniol.inf.is.odysseus.query.transformation.java.operator;


import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformCSVParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformProtocolHandler;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class JavaCSVFileSourceOperator extends AbstractTransformationOperator {
	

	private final String name =  "CSVFileSource";
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
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		CSVFileSource csvFileSource = (CSVFileSource) operator;
		StringBuilder code = new StringBuilder();
		
		//generate code for SDFSchema
		code.append(TransformSDFSchema.getCodeForSDFSchema(csvFileSource.getOutputSchema(),operatorVariable));
		
		String filename = csvFileSource.getFilename();
		String transportHandler = csvFileSource.getTransportHandler();
		String dataHandler = csvFileSource.getDataHandler();
		String wrapper = csvFileSource.getWrapper();
		String protocolHandler = csvFileSource.getProtocolHandler();
		
		
		
		
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for options
		code.append(TransformCSVParameter.getCodeForParameterInfo(csvFileSource.getParameterInfos(),operatorVariable));
		
		//setup transportHandler
		code.append(TransformProtocolHandler.getCodeForProtocolHandler(protocolHandlerParameter, operatorVariable));
	
		//now create the AccessPO
		code.append("AccessPO "+operatorVariable+"PO = new AccessPO("+operatorVariable+"ProtocolHandler,0);");
		code.append("\n");
		code.append(operatorVariable+"PO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("\n");
		

	
		return code.toString();
	}
	
	
	@Override
	public Set<String> getNeededImports() {
		Set<String> importList = new HashSet<String>();
		importList.add(AccessPO.class.getPackage().getName()+"."+AccessPO.class.getSimpleName());
		return importList;
	}
	
	


}
