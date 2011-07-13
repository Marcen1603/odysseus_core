package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.TimestampToPayloadAO;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampToPayloadPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TRelationalTimestampToPayloadRule extends AbstractTransformationRule<TimestampToPayloadAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TimestampToPayloadAO operator,
			TransformationConfiguration config) {
		RelationalTimestampToPayloadPO po = new RelationalTimestampToPayloadPO();
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(operator);
	}

	@Override
	public boolean isExecutable(TimestampToPayloadAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
					return true;				
			}
		}
		return false;
	}
	

	@Override
	public String getName() {
		return "TimestampToPayloadAO --> RelationalTimestampToPayloadPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return TimestampToPayloadAO.class;
	}
}
