package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * abstract rule for the StreamAO (!PULL)
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCStreamAORule<T extends StreamAO> extends AbstractCOperatorRule<StreamAO> {

	public AbstractCStreamAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(StreamAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
			ITenant tenant = UserManagementProvider.getDefaultTenant();
			ILogicalOperator op = DataDictionaryProvider.getDataDictionary(tenant).getStreamForTransformation(logicalOperator.getStreamname(),  null);
			
			AccessAO accessAO = (AccessAO) op;
			
			if (Constants.GENERIC_PULL.equalsIgnoreCase(accessAO.getWrapper())) {
				return false;
			} else {
				return true;
			}
	}

	@Override
	public void analyseOperator(StreamAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		StreamAO streamAO = (StreamAO) logicalOperator;

		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ILogicalOperator logicalPlan = DataDictionaryProvider
				.getDataDictionary(tenant).getStreamForTransformation(
						streamAO.getStreamname(), null);

		AccessAO accessAO = (AccessAO) logicalPlan;
		
		String transportHandler = accessAO.getTransportHandler();
		String protocolHandler = accessAO.getProtocolHandler();
		String dataHandler = accessAO.getDataHandler();

		//add detected transportHandler, protocolHandler and dataHandler
		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);
	}

}
