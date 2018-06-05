package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.PredicateWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingPeriodicWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.UnboundedWindowTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

@SuppressWarnings({ "rawtypes" })
public class TWindowAORule extends
		AbstractIntervalTransformationRule<AbstractWindowAO> {

	public void executePredicateWindowAO(AbstractWindowAO operator,
			TransformationConfiguration config) {
		PredicateWindowTIPO window = new PredicateWindowTIPO(operator);
		defaultExecute(operator, window, config, true, true);
	}

	public void executeSlidingAdvanceTimeWindow(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) {
		SlidingAdvanceTimeWindowTIPO windowPO = new SlidingAdvanceTimeWindowTIPO(
				windowAO);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
	}

	public void executeSlidingElementWindow(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) {
		SlidingElementWindowTIPO windowPO = new SlidingElementWindowTIPO(
				windowAO);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
	}

	public void executeSlidingPeriodicWindow(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) {
		SlidingPeriodicWindowTIPO windowPO = new SlidingPeriodicWindowTIPO(
				windowAO);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
	}

	public void executeSlidingTimeWindow(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) {
		SlidingTimeWindowTIPO windowPO = new SlidingTimeWindowTIPO(windowAO);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
	}

	public void executeUnboundedWindow(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) {
		UnboundedWindowTIPO window = new UnboundedWindowTIPO(windowAO);
		defaultExecute(windowAO, window, transformConfig, true, true);
	}

	@Override
	public void execute(AbstractWindowAO operator,
			TransformationConfiguration config) throws RuleException {
		if (super.isExecutable(operator, config)) {
			switch (operator.getWindowType()) {
			case PREDICATE:
				executePredicateWindowAO(operator, config);
				break;
			case TIME:
				if (operator.getWindowSlide() == null
						&& operator.getWindowAdvance() == null) {
					executeSlidingTimeWindow(operator, config);
				} else if (operator.getWindowSlide() == null) {
					executeSlidingAdvanceTimeWindow(operator, config);
				} else {
					executeSlidingPeriodicWindow(operator, config);
				}
				break;
			case TUPLE:
				executeSlidingElementWindow(operator, config);
				break;
			case UNBOUNDED:
				executeUnboundedWindow(operator, config);
				break;
			default:
				throw new RuleException("Unkown window type "
						+ operator.getWindowType() + " for interval approach");
			}
		}
	}

	@Override
	public boolean isExecutable(AbstractWindowAO operator,
			TransformationConfiguration config) {
		if (operator.getInputSchema().hasMetatype(
				ITimeInterval.class)) {
			return operator.isAllPhysicalInputSet();

		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AbstractWindowAO> getConditionClass() {
		return AbstractWindowAO.class;
	}

}
