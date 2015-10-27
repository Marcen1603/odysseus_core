package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public abstract class AbstractCSenderAORule<T extends SenderAO> extends AbstractCOperatorRule<SenderAO> {

	public AbstractCSenderAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(SenderAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
			ITenant tenant = UserManagementProvider.getDefaultTenant();
			ISink<?> sinkPO = DataDictionaryProvider.getDataDictionary(tenant)
					.getSinkplan(logicalOperator.getSinkname());

			if (sinkPO != null) {
				return true;
			}else{
				return false;
			}
	
	}
	
	@Override
	public void analyseOperator(SenderAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {


		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getSinkForTransformation(
						logicalOperator.getSinkname(), null);

		SenderAO senderAO = (SenderAO) logicalPlan;

		String transportHandler = senderAO.getTransportHandler();
		String protocolHandler = senderAO.getProtocolHandler();
		String dataHandler = senderAO.getDataHandler();

		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);

	}
	
	@Override
	public void addOperatorConfiguration(SenderAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		Map<String, String> optionMap = new HashMap<String, String>();

		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getSinkForTransformation(
						logicalOperator.getSinkname(), null);

		SenderAO senderAO = (SenderAO) logicalPlan;
		optionMap = senderAO.getOptionsMap();
		
		transformationInformation.addOperatorConfiguration(logicalOperator,
				optionMap);
	}

}
