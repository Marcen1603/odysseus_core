package de.uniol.inf.is.odysseus.spatial.grid.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.grid.logicaloperator.VisualGridSinkAO;
import de.uniol.inf.is.odysseus.spatial.grid.physicaloperator.VisualGridSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TVisualGridSinkAORule extends
		AbstractTransformationRule<VisualGridSinkAO> {
	private static Logger LOG = LoggerFactory
			.getLogger(TVisualGridSinkAORule.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final VisualGridSinkAO visualGridSinkAO,
			final TransformationConfiguration transformConfig) {
		try {
			final VisualGridSinkPO visualGridSinkPO = new VisualGridSinkPO(
					visualGridSinkAO.getOutputSchema());
			visualGridSinkPO
					.setOutputSchema(visualGridSinkAO.getOutputSchema());

			Collection<ILogicalOperator> toUpdate = transformConfig
					.getTransformationHelper().replace(visualGridSinkAO,
							visualGridSinkPO);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			retract(visualGridSinkAO);
		} catch (final Exception e) {
			TVisualGridSinkAORule.LOG.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "VisualGridSinkAO -> VisualGridSinkPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(VisualGridSinkAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("relational")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?> getConditionClass() {
		return VisualGridSinkAO.class;
	}
}
