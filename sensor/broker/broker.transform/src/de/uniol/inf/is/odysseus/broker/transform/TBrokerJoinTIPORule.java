package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.physicaloperator.association.BrokerJoinTIPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.association.BrokerMetadataMergeFunction;
import de.uniol.inf.is.odysseus.broker.physicaloperator.association.LeftAfterRightTITransferArea;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerJoinTIPORule extends AbstractTransformationRule<JoinAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(JoinAO joinAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Using Broker Transfer Function");
		JoinTIPO joinPO = new BrokerJoinTIPO();
		IPredicate pred = joinAO.getPredicate();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred.clone());
		joinPO.setTransferFunction(new LeftAfterRightTITransferArea());
		joinPO.setMetadataMerge(new BrokerMetadataMergeFunction());
		joinPO.setOutputSchema(joinAO.getOutputSchema() == null?null:joinAO.getOutputSchema().clone());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(joinAO, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(joinPO);
		retract(joinAO);
		
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
				if(transformConfig.getOption("IBrokerInterval")!=null){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinAO -> JoinTIPO (Broker)";
	}

	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
