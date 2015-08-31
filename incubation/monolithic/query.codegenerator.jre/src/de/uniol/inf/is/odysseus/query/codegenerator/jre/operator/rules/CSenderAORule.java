package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSenderAORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;

public class CSenderAORule extends AbstractCSenderAORule{
	
	public CSenderAORule() {
		super(CSenderAORule.class.getName());
	}

	
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator logicalOperator) {
	CodeFragmentInfo senderPO = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(logicalOperator);
		SenderAO dummySenderAO = (SenderAO) logicalOperator;


		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getSinkForTransformation(dummySenderAO.getSinkname(), null);
		
		SenderAO senderAO = (SenderAO)logicalPlan;
	
	
		String transportHandler = senderAO.getTransportHandler();
		String dataHandler = senderAO.getDataHandler();
		String wrapper = senderAO.getWrapper();
		String protocolHandler = senderAO.getProtocolHandler();
	
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJavaDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, senderAO.getOptionsMap(),logicalOperator, direction);
		senderPO.addCodeFragmentInfo(codeAccessFramwork);
		
		
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		senderPO.addImport(SenderPO.class.getName());
		senderPO.addCode(senderPOTemplate.getSt().render());
	
		
		return senderPO;
	}



}
