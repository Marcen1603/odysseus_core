package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TStreamAORule extends AbstractRule{
	
	public TStreamAORule() {
		super("TStreamAORule", "Java");
	}
	
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof StreamAO){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Class<?> getConditionClass() {
		return StreamAO.class;
	}


	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
	CodeFragmentInfo receiverPO = new CodeFragmentInfo();
		
		StreamAO streamAO = (StreamAO)operator;
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(streamAO.getStreamname(),  null);

		AccessAO accessAO = (AccessAO)logicalPlan;
		
		String transportHandler = accessAO.getTransportHandler();
		String dataHandler = accessAO.getDataHandler();
		String wrapper = accessAO.getWrapper();
		String protocolHandler = accessAO.getProtocolHandler();
		ITransportDirection direction = ITransportDirection.IN;
		 
		ProtocolHandlerParameter protocolHandlerParameter = new ProtocolHandlerParameter(null,transportHandler,dataHandler,wrapper,protocolHandler);
		
		CodeFragmentInfo codeAccessFramwork = CreateDefaultCode.codeForAccessFramework(protocolHandlerParameter, accessAO.getOptionsMap(),operator, direction);
		receiverPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		StringTemplate receiverPOTemplate = new StringTemplate("operator","receiverPO");
		receiverPOTemplate.getSt().add("operatorVariable", operatorVariable);
			
		receiverPO.addCode(receiverPOTemplate.getSt().render());
		receiverPO.addImport(ReceiverPO.class.getName());
		receiverPO.addImport(IOException.class.getName());
			
		return receiverPO;
	}


}
