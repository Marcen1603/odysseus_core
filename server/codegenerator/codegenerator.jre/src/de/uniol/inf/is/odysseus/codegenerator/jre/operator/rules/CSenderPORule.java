package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSenderAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * This rule generate from a SenderAO the code for the 
 * SenderPO operator. 
 * 
 * template: operator/senderPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CSenderPORule extends AbstractCSenderAORule<SenderAO>{
	
	public CSenderPORule() {
		super(CSenderPORule.class.getName());
	}

	
	@Override
	public CodeFragmentInfo getCode(SenderAO logicalOperator) {
		CodeFragmentInfo senderPO = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(logicalOperator);
		
		SenderAO dummySenderAO = (SenderAO) logicalOperator;
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getSinkForTransformation(dummySenderAO.getSinkname(), null);
		
		SenderAO senderAO = (SenderAO)logicalPlan;
		
		//get needed handler from SenderAO
		String transportHandler = senderAO.getTransportHandler();
		String dataHandler = senderAO.getDataHandler();
		String wrapper = senderAO.getWrapper();
		String protocolHandler = senderAO.getProtocolHandler();
	
		ITransportDirection direction = ITransportDirection.OUT;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for the access framework
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, senderAO.getOptionsMap(),logicalOperator, direction);
		senderPO.addCodeFragmentInfo(codeAccessFramwork);
		
		//generate code for the senderPO
		StringTemplate senderPOTemplate = new StringTemplate("operator","senderPO");
		senderPOTemplate.getSt().add("operatorVariable", operatorVariable);

		//add framework imports
		senderPO.addFrameworkImport(SenderPO.class.getName());
		senderPO.addCode(senderPOTemplate.getSt().render());
	
		
		return senderPO;
	}



}
