package de.uniol.inf.is.odysseus.debsgc2016.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.debsgc2016.logicaloperator.TreeFlattenerAO;
import de.uniol.inf.is.odysseus.debsgc2016.physicaloperator.TreeFlattenerPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TreeFlatenerAOTransformationRule extends AbstractTransformationRule<TreeFlattenerAO> {

	@Override
	public void execute(TreeFlattenerAO operator, TransformationConfiguration config) throws RuleException {

		int rootNodeKeyPos = operator.getInputSchema(0).indexOfWithException(operator.getRootNodeKey());
		int nRootNodeKeyPos = operator.getInputSchema(1).indexOfWithException(operator.getnRootNodeKey());
		int nRootNodeNodeKeyPos = operator.getInputSchema(1).indexOfWithException(operator.getnRootRefToRoot());
		int nRootNodeRootKeyPos = operator.getInputSchema(1).indexOfWithException(operator.getnRootRefToNRoot());

		boolean keepAlive = operator.isKeepAlive();
		long cleanUpRate = operator.getCleanUpRate();
	
		defaultExecute(operator,
				new TreeFlattenerPO(rootNodeKeyPos, nRootNodeKeyPos, nRootNodeNodeKeyPos, nRootNodeRootKeyPos, keepAlive, cleanUpRate), config,
				true, true);
	}

	@Override
	public boolean isExecutable(TreeFlattenerAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
