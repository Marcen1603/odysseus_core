package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.SamplePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TSampleAORule extends AbstractTransformationRule<SampleAO> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final void execute(final SampleAO operator, final TransformationConfiguration config) {
		final IPhysicalOperator samplePO = new SamplePO<ITimeInterval>(operator.determineAttributesList(), operator.getSamples());
		this.defaultExecute(operator, samplePO, config, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final SampleAO operator, final TransformationConfiguration config) {
		if ((config.getDataTypes().contains(SchemaUtils.DATATYPE))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public final String getName() {
		return "SampleAO -> SamplePO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super SampleAO> getConditionClass() {
		return SampleAO.class;
	}

}
