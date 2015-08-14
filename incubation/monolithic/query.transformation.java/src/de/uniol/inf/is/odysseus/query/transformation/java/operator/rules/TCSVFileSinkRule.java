package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TCSVFileSinkRule extends AbstractRule {
	
	public TCSVFileSinkRule() {
		super(TCSVFileSinkRule.class.getName(), "Java");
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractSenderAO){
			
			AbstractSenderAO operator = (AbstractSenderAO) logicalOperator;
			
				if (operator.getWrapper() != null) {
						if (Constants.GENERIC_PULL.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
						if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
				}
	
			return false;
			
		}
		
		return false;
	}


	@Override
	public Class<?> getConditionClass() {
		return AbstractSenderAO.class;
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
	CodeFragmentInfo csvFileSink = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
	
		CSVFileSink csvFileSinkOP = (CSVFileSink) operator;
			
		String filename = csvFileSinkOP.getFilename();
		String transportHandler = csvFileSinkOP.getTransportHandler();
		String dataHandler = csvFileSinkOP.getDataHandler();
		String wrapper = csvFileSinkOP.getWrapper();
		String protocolHandler = csvFileSinkOP.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(filename,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateDefaultCode.codeForAccessFramework(protocolHandlerParameter, csvFileSinkOP.getOptionsMap(),operator, direction);
		csvFileSink.addCodeFragmentInfo(codeAccessFramwork);
		
		
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		csvFileSink.addImport(SenderPO.class.getName());
		csvFileSink.addCode(senderPOTemplate.getSt().render());
		
		return csvFileSink;
	}


}
