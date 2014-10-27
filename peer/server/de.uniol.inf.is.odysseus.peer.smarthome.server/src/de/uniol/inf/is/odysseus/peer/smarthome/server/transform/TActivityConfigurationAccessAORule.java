package de.uniol.inf.is.odysseus.peer.smarthome.server.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.smarthome.server.logicaloperator.ActivityConfigurationAccessAO;
import de.uniol.inf.is.odysseus.peer.smarthome.server.physicaloperator.ActivityConfigurationAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TActivityConfigurationAccessAORule extends AbstractTransformationRule<ActivityConfigurationAccessAO> {
	private static Logger LOG = LoggerFactory.getLogger(TActivityConfigurationAccessAORule.class);
	
	@Override
	public void execute(ActivityConfigurationAccessAO operator,
			TransformationConfiguration transformConfig) throws RuleException {
		try {
			defaultExecute(operator, new ActivityConfigurationAccessPO(operator), transformConfig, true, true);
		} catch (Exception e) {
			LOG.error("Exception in TTemper1AccessAORule");
			throw new RuleException("Could not create Temper1AccessPO", e);
		}
	}

	@Override
	public boolean isExecutable(ActivityConfigurationAccessAO operator,
			TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ActivityConfigurationAccessAO -> ActivityConfigurationAccessPO";
	}
}
