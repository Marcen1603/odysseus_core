package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.io.IOException;

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
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCStreamAORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;

public class CReceiverPORule extends AbstractCStreamAORule<StreamAO>{
	
	public CReceiverPORule() {
		super(CReceiverPORule.class.getName());
	}
	

	@Override
	public CodeFragmentInfo getCode(StreamAO operator) {
		
		CodeFragmentInfo receiverPO = new CodeFragmentInfo();
	
		String operatorVariable = JreCodegeneratorStatus.getInstance().getVariable(operator);
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(operator.getStreamname(),  null);

		AccessAO accessAO = (AccessAO)logicalPlan;
		

		String transportHandler = accessAO.getTransportHandler();
		String dataHandler = accessAO.getDataHandler();
		String wrapper = accessAO.getWrapper();
		String protocolHandler = accessAO.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateJreDefaultCode.getCodeForAccessFramework(protocolHandlerParameter, accessAO.getOptionsMap(),operator, direction);
		receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		StringTemplate receiverPOTemplate = new StringTemplate("operator","receiverPO");
		receiverPOTemplate.getSt().add("operatorVariable", operatorVariable);
		receiverPOTemplate.getSt().add("readMetaData", accessAO.getReadMetaData());
		
			
		receiverPO.addCode(receiverPOTemplate.getSt().render());
		
		//important add timestamp op
		Utils.createTimestampAO(operator, accessAO.getDateFormat());
		
		
		receiverPO.addImport(ReceiverPO.class.getName());
		receiverPO.addImport(IOException.class.getName());
		
		
		receiverPO.addImport(IMetaAttribute.class.getName());
		receiverPO.addImport(IMetadataInitializer.class.getName());
			
		return receiverPO;
	}


}
