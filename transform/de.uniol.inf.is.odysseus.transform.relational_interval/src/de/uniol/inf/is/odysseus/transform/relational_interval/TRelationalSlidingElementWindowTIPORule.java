package de.uniol.inf.is.odysseus.transform.relational_interval;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.relational_interval.RelationalSlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalSlidingElementWindowTIPORule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(WindowAO windowAO, TransformationConfiguration transformConfig) {
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
		if (transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
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

}
