package de.uniol.inf.is.odysseus.recovery.gaprecovery.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator.AbstractConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator.EWConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator.TWConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings("nls")
public class TConvergenceDetectorAORule extends AbstractTransformationRule<ConvergenceDetectorAO> {

	/**
	 * The logger for all classes related to the Convergence Detector.
	 */
	private static final Logger cLog = LoggerFactory.getLogger("ConvergenceDetector");

	@Override
	public void execute(ConvergenceDetectorAO logical, TransformationConfiguration config) throws RuleException {
		// Get window operator before the convergence detector and check,
		// if it's a time window or element window
		// Assumption that this rule is executable
		AbstractConvergenceDetectorPO<?> physical = null;
		final String errorMessage = "No time window or element window as only source for convergence detector!";
		try {
			AbstractWindowAO windowAO = (AbstractWindowAO) logical.getSubscribedToSource().iterator().next()
					.getTarget();
			if (TimeWindowAO.class.isInstance(windowAO)) {
				physical = new TWConvergenceDetectorPO<>(logical.getWindowWidth(), logical.getWindowAdvance());
			} else {
				// element window
				physical = new EWConvergenceDetectorPO<>(logical.getWindowWidth(), logical.getWindowAdvance());
			}
			defaultExecute(logical, physical, config, true, true);
		} catch (Throwable t) {
			cLog.error(errorMessage);
			throw new RuleException(errorMessage, t);
		}
	}

	@Override
	public boolean isExecutable(ConvergenceDetectorAO logical, TransformationConfiguration config) {
		// Logical all input set AND there is a window operator before it
		if (!logical.isAllPhysicalInputSet() || logical.getSubscribedToSource().size() != 1) {
			return false;
		}
		ILogicalOperator source = logical.getSubscribedToSource().iterator().next().getTarget();
		return TimeWindowAO.class.isInstance(source) || ElementWindowAO.class.isInstance(source);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}