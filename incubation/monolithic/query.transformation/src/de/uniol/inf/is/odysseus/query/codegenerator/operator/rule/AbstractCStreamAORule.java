package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCStreamAORule extends AbstractRule{
	
	public AbstractCStreamAORule(String name) {
		super(name );
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


	public void analyseOperator(ILogicalOperator logicalOperator,QueryAnalyseInformation transformationInformation){
		
		StreamAO streamAO = (StreamAO)logicalOperator;
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(streamAO.getStreamname(),  null);

		AccessAO accessAO = (AccessAO)logicalPlan;
		
		String transportHandler = accessAO.getTransportHandler();
		String protocolHandler = accessAO.getProtocolHandler();
		String dataHandler = accessAO.getDataHandler();
		
		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);
	}


}
