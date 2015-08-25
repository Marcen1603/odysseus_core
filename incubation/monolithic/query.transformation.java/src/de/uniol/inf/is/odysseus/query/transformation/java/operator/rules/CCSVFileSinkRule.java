package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCCSVFileSinkRule;

public class CCSVFileSinkRule extends AbstractCCSVFileSinkRule {
	
	public CCSVFileSinkRule() {
		super(CCSVFileSinkRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
	
		CSVFileSink csvFileSinkOP = (CSVFileSink) operator;
			
		String filename = csvFileSinkOP.getFilename();
		String transportHandler = csvFileSinkOP.getTransportHandler();
		String dataHandler = csvFileSinkOP.getDataHandler();
		String wrapper = csvFileSinkOP.getWrapper();
		String protocolHandler = csvFileSinkOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJavaDefaultCode.codeForAccessFramework(protocolHandlerParameter, csvFileSinkOP.getOptionsMap(),operator, direction);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		csvFileSink.addImport(SenderPO.class.getName());
		csvFileSink.addCode(senderPOTemplate.getSt().render());
		
		return csvFileSink;
	}
	

}
