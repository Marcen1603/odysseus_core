package de.uniol.inf.is.odysseus.transform.interval.window;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnboundedWindowRule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(WindowAO windowAO, TransformationConfiguration transformConfig) {
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, windowAO.getPhysInputPOs().iterator().next());
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(windowAO);

	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
			if (operator.isAllPhysicalInputSet()) {
				if (operator.getWindowType() == WindowType.UNBOUNDED) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> Unbounded Window";
	}

}
