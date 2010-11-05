package de.uniol.inf.is.odysseus.broker.evaluation.rules;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAORule extends AbstractTransformationRule<BrokerAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(BrokerAO brokerAO, TransformationConfiguration trafo) {
		BrokerPO<?> brokerPO = BrokerWrapperPlanFactory.getPlan(brokerAO.getIdentifier());		
		if (brokerPO == null) {
			LoggerSystem.printlog(Accuracy.TRACE, "Creating new broker: " + brokerAO.getIdentifier());
			brokerPO = new BrokerPO(brokerAO.getIdentifier());
		} else {			
			LoggerSystem.printlog(Accuracy.TRACE, "Reusing existing broker: " + brokerAO.getIdentifier());
		}
		brokerPO.setOutputSchema(brokerAO.getOutputSchema());
		brokerPO.setQueueSchema(brokerAO.getQueueSchema());
		BrokerWrapperPlanFactory.putPlan(brokerAO.getIdentifier(), brokerPO);		
		
		replace(brokerAO, brokerPO, trafo);		
		retract(brokerAO);		
		
		//update(brokerPO);
		
		LoggerSystem.printlog(Accuracy.TRACE, "Broker Rule end.");

	}

	@Override
	public boolean isExecutable(BrokerAO operator, TransformationConfiguration transformConfig) {		
		//return operator.isAllPhysicalInputSet();
		return true;
	}

	@Override
	public String getName() {
		return "BrokerAO -> BrokerPO";
	}

	private Collection<ILogicalOperator> replace(BrokerAO logical, IPipe<?, ?> physical) {
		Collection<ILogicalOperator> ret = replace(logical, (ISink<?>) physical);
		ret.addAll(replace(logical, (ISource<?>) physical));
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<ILogicalOperator> replace(BrokerAO logical, ISink<?> physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {			
			physical.subscribeToSource((ISource)psub.getTarget(), psub.getSinkInPort(), psub.getSourceOutPort(), psub.getSchema());
			System.out.println(psub);
		}
		for (LogicalSubscription l : logical.getSubscriptions()) {
			ILogicalOperator target = l.getTarget();
			if (target instanceof TopAO) {
				((TopAO) target).setPhysicalInputPO(physical);
			}
		}
		ret.add(logical);
		return ret;
	}

	private Collection<ILogicalOperator> replace(BrokerAO logical, ISource<?> physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscriptions()) {			
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());			
			ret.add(l.getTarget());
		}
		return ret;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
