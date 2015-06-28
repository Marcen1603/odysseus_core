package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
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
		
		int operatorId = OperatorToVariable.getVariable(operator);

		CSVFileSource csvFileSource = (CSVFileSource) operator;
		StringBuilder code = new StringBuilder();
		
		//generate code for SDFSchema
		code.append(TransformSDFSchema.getCodeForSDFSchema(csvFileSource.getOutputSchema()));
		
		String filename = csvFileSource.getFilename();
		String transportHandler = csvFileSource.getTransportHandler();
		String dataHandler = csvFileSource.getDataHandler();
		String wrapper = csvFileSource.getWrapper();
		String protocolHandler = csvFileSource.getProtocolHandler();
		
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for options
		code.append(TransformCSVParameter.getCodeForParameterInfo(csvFileSource.getParameterInfos()));
		
		
		//setup transportHandler
		code.append(TransformProtocolHandler.getCodeForProtocolHandler(protocolHandlerParameter));
	
		
		//now create the AccessPO
		// TODO Katalog für die verwendeten Variablen erstellen z.B. für testAccess und cSVProtocolHandlerNeu
		// teilweise werden diese in den anderen Operatoren benötigt
		code.append("AccessPO testAccess = new AccessPO(cSVProtocolHandlerNeu,0);");
		
		code.append("\n");
		code.append("\n");
		
		
		return code.toString();
	}
	@Override
	public Set<String> getNeededImports() {
		Set<String> importList = new HashSet<String>();
		importList.add("de.uniol.inf.is.odysseus.core.collection.OptionMap");
		importList.add("de.uniol.inf.is.odysseus.core.collection.Tuple");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection");
		importList.add("de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler");
		importList.add("de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO");

		
		return importList;
		
	}
	
	


}
