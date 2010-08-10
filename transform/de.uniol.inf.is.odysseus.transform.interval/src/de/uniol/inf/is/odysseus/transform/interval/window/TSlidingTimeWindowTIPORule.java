package de.uniol.inf.is.odysseus.transform.interval.window;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSlidingTimeWindowTIPORule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void transform(WindowAO windowAO, TransformationConfiguration transformConfig) {
		SlidingTimeWindowTIPO windowPO = new SlidingTimeWindowTIPO(windowAO); 
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, windowPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		update(transformConfig);
		insert(windowPO);
		retract(windowAO);
		
	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
			if (operator.isAllPhysicalInputSet()) {
				if (operator.getWindowType() == WindowType.SLIDING_TIME_WINDOW) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> SlidingTimeWindowTIPO";
	}

}
