package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCStreamAORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * This rule generate from a StreamAO (!PULL) the code for the 
 * ReceiverPO operator. 
 * 
 * template: operator/receiverPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CReceiverPORule extends AbstractCStreamAORule<StreamAO>{
	
	public CReceiverPORule() {
		super(CReceiverPORule.class.getName());
	}
	

	@Override
	public CodeFragmentInfo getCode(StreamAO operator) {
		
		CodeFragmentInfo receiverPO = new CodeFragmentInfo();
	
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(operator.getStreamname(),  null);

		AccessAO accessAO = (AccessAO)logicalPlan;
		
		//get values for the access framework
		String transportHandler = accessAO.getTransportHandler();
		String dataHandler = accessAO.getDataHandler();
		String wrapper = accessAO.getWrapper();
		String protocolHandler = accessAO.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		//generate code for the access framework
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, accessAO.getOptionsMap(),operator, direction);
		receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		//generate code for the receiverPO
		StringTemplate receiverPOTemplate = new StringTemplate("operator","receiverPO");
		receiverPOTemplate.getSt().add("operatorVariable", operatorVariable);
		receiverPOTemplate.getSt().add("readMetaData", accessAO.getReadMetaData());
		
		//render template
		receiverPO.addCode(receiverPOTemplate.getSt().render());
		
		//important add timestamp op
		Utils.createTimestampAO(operator, accessAO.getDateFormat());
		
		//add framework imports
		receiverPO.addFrameworkImport(ReceiverPO.class.getName());
		receiverPO.addFrameworkImport(IOException.class.getName());
		receiverPO.addFrameworkImport(IMetaAttribute.class.getName());
		receiverPO.addFrameworkImport(IMetadataInitializer.class.getName());
			
		return receiverPO;
	}


}
