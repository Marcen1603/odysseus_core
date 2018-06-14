package de.uniol.inf.is.odysseus.recovery.convergencedetector.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator.AbstractConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator.EWConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator.TWConvergenceDetectorPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Transformation rule for {@link ConvergenceDetectorAO}. <br />
 * If the operator is after a time window, transform to
 * {@link TWConvergenceDetectorPO}, or after an element window, transform to
 * {@link EWConvergenceDetectorPO}.
 * 
 * @author Michael Brand
 *
 */
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
		try {
			AbstractWindowAO windowAO = (AbstractWindowAO) logical.getSubscribedToSource().iterator().next()
					.getSource();
			if (TimeWindowAO.class.isInstance(windowAO)) {
				physical = new TWConvergenceDetectorPO<>(logical.getWindowWidth(), logical.getWindowAdvance());
			} else if (ElementWindowAO.class.isInstance(windowAO)) {
				physical = new EWConvergenceDetectorPO<>(logical.getWindowWidth(), logical.getWindowAdvance());
			} else {
				// unknown window type
				cLog.warn("Unknown window type ({}). Transformation of convergence detector aborted.", windowAO);
				return;
			}
			defaultExecute(logical, physical, config, true, true);
		} catch (Throwable t) {
			final String errorMessage = "No time window or element window as only source for convergence detector!";
			cLog.error(errorMessage);
			throw new RuleException(errorMessage, t);
		}
	}

	@Override
	public boolean isExecutable(ConvergenceDetectorAO logical, TransformationConfiguration config) {
		// Logical all input set AND there is a window operator before it.
		// Additionally, meta types must match!
		if (!logical.isAllPhysicalInputSet() || logical.getSubscribedToSource().size() != 1) {
			return false;
		}

		ILogicalOperator source = logical.getSubscribedToSource().iterator().next().getSource();
		if (!TimeWindowAO.class.isInstance(source) && !ElementWindowAO.class.isInstance(source)) {
			return false;
		}

		SDFSchema schema = logical.getInputSchema();
		return schema.hasMetatype(ITimeInterval.class) && schema.hasMetatype(ITrust.class);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}