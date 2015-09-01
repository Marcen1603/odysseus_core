package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCSenderAORule<T extends SenderAO> extends AbstractRule<SenderAO> {

	public AbstractCSenderAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(SenderAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if (logicalOperator instanceof SenderAO) {
			SenderAO senderAO = (SenderAO) logicalOperator;

			ITenant tenant = UserManagementProvider.getDefaultTenant();
			ISink<?> sinkPO = DataDictionaryProvider.getDataDictionary(tenant)
					.getSinkplan(senderAO.getSinkname());

			if (sinkPO != null) {
				return true;
			}

		} else {
			return false;
		}

		return false;
	}


	@Override
	public void analyseOperator(SenderAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		SenderAO dummySenderAO = (SenderAO) logicalOperator;

		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getSinkForTransformation(
						dummySenderAO.getSinkname(), null);

		SenderAO senderAO = (SenderAO) logicalPlan;

		String transportHandler = senderAO.getTransportHandler();
		String protocolHandler = senderAO.getProtocolHandler();
		String dataHandler = senderAO.getDataHandler();

		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);

	}

}