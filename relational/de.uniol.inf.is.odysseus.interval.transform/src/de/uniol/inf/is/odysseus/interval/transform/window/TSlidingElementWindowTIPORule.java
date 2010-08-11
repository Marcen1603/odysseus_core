package de.uniol.inf.is.odysseus.interval.transform.window;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSlidingElementWindowTIPORule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(WindowAO windowAO, TransformationConfiguration transformConfig) {
		SlidingElementWindowTIPO windowPO = new SlidingElementWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, windowPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		insert(windowPO);
		retract(windowAO);
	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.toString())) {
			if (operator.isAllPhysicalInputSet()) {
				if ((operator.getWindowType() == WindowType.SLIDING_TUPLE_WINDOW || operator.getWindowType() == WindowType.JUMPING_TUPLE_WINDOW)) {
					if (!operator.isPartitioned()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> SlidingElementWindowTIPO";
	}

}
