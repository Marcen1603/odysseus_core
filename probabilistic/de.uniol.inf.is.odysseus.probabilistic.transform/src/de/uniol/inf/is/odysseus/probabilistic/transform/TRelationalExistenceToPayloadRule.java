package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ExistenceToPayloadAO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.RelationalExistenceToPayloadPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TRelationalExistenceToPayloadRule extends AbstractTransformationRule<ExistenceToPayloadAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ExistenceToPayloadAO operator, TransformationConfiguration config) {
		defaultExecute(operator, new RelationalExistenceToPayloadPO(), config, true, true);
	}

	@Override
	public boolean isExecutable(ExistenceToPayloadAO operator, TransformationConfiguration config) {
		if (config.getDataTypes().contains(SchemaUtils.DATATYPE) && config.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ExistenceToPayloadAO --> RelationalExistenceToPayloadPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ExistenceToPayloadAO> getConditionClass() {
		return ExistenceToPayloadAO.class;
	}
}
