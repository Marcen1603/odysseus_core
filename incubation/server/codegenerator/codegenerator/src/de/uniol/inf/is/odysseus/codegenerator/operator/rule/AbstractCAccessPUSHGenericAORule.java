package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;


public abstract class AbstractCAccessPUSHGenericAORule <T extends AccessAO> extends AbstractCOperatorRule<AccessAO>{
	
	public AbstractCAccessPUSHGenericAORule(String name) {
		super(name);
	}
	
	
	@Override
	public boolean isExecutable(AccessAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
			if (Constants.GENERIC_PUSH.equalsIgnoreCase(logicalOperator.getWrapper())) {
				return true;
			} else {
				return false;
			}
	}
	
	@Override
	public void analyseOperator(AccessAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		String transportHandler = logicalOperator.getTransportHandler();
		String protocolHandler = logicalOperator.getProtocolHandler();
		String dataHandler = logicalOperator.getDataHandler();

		transformationInformation.addTransportHandler(transportHandler);
		transformationInformation.addProtocolHandler(protocolHandler);
		transformationInformation.addDataHandler(dataHandler);
		
	}
}
