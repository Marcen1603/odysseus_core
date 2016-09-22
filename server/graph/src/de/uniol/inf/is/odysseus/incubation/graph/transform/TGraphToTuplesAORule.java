package de.uniol.inf.is.odysseus.incubation.graph.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphToTuplesAO;
import de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.GraphToTuplesPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGraphToTuplesAORule extends AbstractTransformationRule<GraphToTuplesAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(GraphToTuplesAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new GraphToTuplesPO<IMetaAttribute>(), config, true, true);
	}

	@Override
	public boolean isExecutable(GraphToTuplesAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super GraphToTuplesAO> getConditionClass() {
		return GraphToTuplesAO.class;
	}

}
