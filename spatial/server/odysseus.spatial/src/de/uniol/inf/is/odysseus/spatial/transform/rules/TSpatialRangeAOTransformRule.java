package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialRangeAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.SpatialRangePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class TSpatialRangeAOTransformRule extends AbstractTransformationRule<SpatialRangeAO> {

	@Override
	public void execute(SpatialRangeAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SpatialRangePO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(SpatialRangeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
