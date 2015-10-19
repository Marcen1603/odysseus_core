package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCAccessAORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CAccessPORule  extends AbstractCAccessAORule<StreamAO>{
	

	public CAccessPORule() {
		super(CAccessPORule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(StreamAO operator) {
		
		CodeFragmentInfo accessPO = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
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
		accessPO.addCodeFragmentInfo(codeAccessFramwork);
		 
		StringTemplate accessPOTemplate = new StringTemplate("operator","accessPO");
		accessPOTemplate.getSt().add("operatorVariable", operatorVariable);
		accessPOTemplate.getSt().add("getMaxTimeToWaitForNewEventMS", accessAO.getMaxTimeToWaitForNewEventMS());
		accessPOTemplate.getSt().add("readMetaData", accessAO.readMetaData());
		
		accessPO.addCode(accessPOTemplate.getSt().render());

		//important add timestamp op
		Utils.createTimestampAO(operator, accessAO.getDateFormat());
				
		accessPO.addFrameworkImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());	
		accessPO.addFrameworkImport(AccessPO.class.getName());
		accessPO.addFrameworkImport(IOException.class.getName());
		accessPO.addFrameworkImport(IMetaAttribute.class.getName());
		accessPO.addFrameworkImport(IMetadataInitializer.class.getName());
			
		return accessPO;
	}

}
