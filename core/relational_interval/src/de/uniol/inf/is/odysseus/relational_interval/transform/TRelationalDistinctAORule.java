package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DistinctAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalDistinctPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalDistinctAORule extends AbstractTransformationRule<DistinctAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DistinctAO operator, TransformationConfiguration config) {
		RelationalDistinctPO<Tuple<? extends ITimeInterval>> po = new RelationalDistinctPO<>();
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(DistinctAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && config.getMetaTypes().contains(
				ITimeInterval.class.getCanonicalName());
	}

	@Override
	public String getName() {
		return "DistinctAO --> RelationalDistinctAO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super DistinctAO> getConditionClass() {
		return DistinctAO.class;
	}

}
