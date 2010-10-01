package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalSlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalSlidingElementWindowTIPORule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(WindowAO windowAO, TransformationConfiguration transformConfig) {
		RelationalSlidingElementWindowTIPO windowPO = new RelationalSlidingElementWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, windowPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		update(transformConfig);
		insert(windowPO);
		retract(windowAO);
	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet())
				if (operator.getWindowType() == WindowType.SLIDING_TUPLE_WINDOW || operator.getWindowType() == WindowType.JUMPING_TUPLE_WINDOW) {
					if (operator.isPartitioned()) {
						return true;
					}
				}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> RelationalSlidingElementWindowTIPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
