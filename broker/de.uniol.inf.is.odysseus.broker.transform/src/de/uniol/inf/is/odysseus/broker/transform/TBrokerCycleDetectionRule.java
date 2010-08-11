package de.uniol.inf.is.odysseus.broker.transform;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionDetector;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerCycleDetectionRule extends AbstractTransformationRule<BrokerPO<?>> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void transform(BrokerPO<?> operator, TransformationConfiguration transformConfig) {		
		getLogger().debug("Searching for cycles and reorganize broker transactions..."); 
		TransactionDetector.reorganizeTransactions(BrokerWrapperPlanFactory.getAllBrokerPOs());		
		getLogger().debug("Searching done"); 
		retract(operator);
	}

	@Override
	public boolean isExecutable(BrokerPO<?> operator, TransformationConfiguration transformConfig) {
		return (!BrokerWrapperPlanFactory.isEmpty());
	}

	@Override
	public String getName() {
		return "Detecting Cycles Rule";
	}

}
