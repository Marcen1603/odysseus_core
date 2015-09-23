package de.uniol.inf.is.odysseus.recommendation.lod.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.lod.logicaloperator.LODEnrichAO;
import de.uniol.inf.is.odysseus.recommendation.lod.physicaloperator.LODEnrichPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christopher Schwarz
 */
public class TLODEnrichAORule extends AbstractTransformationRule<LODEnrichAO> {

	@Override
	public void execute(LODEnrichAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new LODEnrichPO<>(operator.getURL(),
												   operator.getType(),
												   operator.getPredicate(),
												   operator.getAttribute()), config, true, true);
	}

	@Override
	public boolean isExecutable(LODEnrichAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
