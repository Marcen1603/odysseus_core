package de.uniol.inf.is.odysseus.transform.interval.window;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingPeriodicWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSlidingPeriodicWindowTIPORule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void transform(WindowAO windowAO, TransformationConfiguration transformConfig) {
		SlidingPeriodicWindowTIPO windowPO = new SlidingPeriodicWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, windowPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(windowPO);
		retract(windowAO);	
	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
			if (operator.isAllPhysicalInputSet()) {
				if ((operator.getWindowType() == WindowType.PERIODIC_TIME_WINDOW || operator.getWindowType() == WindowType.PERIODIC_TUPLE_WINDOW)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> SlidingPeriodicWindowTIPO";
	}

}
