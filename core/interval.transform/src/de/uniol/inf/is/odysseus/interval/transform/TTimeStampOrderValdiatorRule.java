package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TimeStampOrderValidatorTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.TimeStampOrderValidatorAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTimeStampOrderValdiatorRule extends
		AbstractTransformationRule<TimeStampOrderValidatorAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TimeStampOrderValidatorAO operator,
			TransformationConfiguration config) {		
		TimeStampOrderValidatorTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>> po = new TimeStampOrderValidatorTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>>();
		po.setOutputSchema(po.getOutputSchema());
		replace(operator,po,config);
		retract(operator);
	}

	@Override
	public boolean isExecutable(TimeStampOrderValidatorAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TimeStampOrderValidatorAO --> TimeStampOrderValidatorTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return TimeStampOrderValidatorAO.class;
	}

}
