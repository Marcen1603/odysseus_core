package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCAccessAORule<T extends StreamAO> extends AbstractCOperatorRule<StreamAO> { 
	
	
	public AbstractCAccessAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(StreamAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
			ITenant tenant = UserManagementProvider.getDefaultTenant();
			ILogicalOperator op = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(logicalOperator.getStreamname(),  null);
			
			AccessAO accessAO = (AccessAO) op;
			
			if (Constants.GENERIC_PULL.equalsIgnoreCase(accessAO.getWrapper())) {
				return true;
			} else {
				return false;
			}
	}

	@Override
	public void analyseOperator(StreamAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {


		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getStreamForTransformation(
						logicalOperator.getStreamname(), null);

		AccessAO accessAO = (AccessAO) logicalPlan;

		String transportHandler = accessAO.getTransportHandler();
		String protocolHandler = accessAO.getProtocolHandler();
		String dataHandler = accessAO.getDataHandler();

		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);
		
		transformationInformation.addIterableSource(logicalOperator);
	}
	
	@Override
	public void addOperatorConfiguration(StreamAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getStreamForTransformation(
						logicalOperator.getStreamname(), null);

		AccessAO accessAO = (AccessAO) logicalPlan;
		
		Map<String, String> optionMap = new HashMap<String, String>();
		optionMap = accessAO.getOptionsMap();

		transformationInformation.addOperatorConfiguration(logicalOperator,optionMap);
	}
}
