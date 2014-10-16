package de.uniol.inf.is.odysseus.wrapper.temper1.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.temper1.logicaloperator.Temper1AccessAO;
import de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.Temper1AccessPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;


public class TTemper1AccessAORule extends AbstractTransformationRule<Temper1AccessAO> {
	private static Logger LOG = LoggerFactory.getLogger(TTemper1AccessAORule.class);
	
	@Override
	public void execute(Temper1AccessAO operator,
			TransformationConfiguration transformConfig) throws RuleException {
		try {
			defaultExecute(operator, new Temper1AccessPO(operator), transformConfig, true, true);
		} catch (Exception e) {
			LOG.error("Exception in TTemper1AccessAORule");
			throw new RuleException("Could not create Temper1AccessPO", e);
		}
	}

	@Override
	public boolean isExecutable(Temper1AccessAO operator,
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
		return "Temper1AccessAO -> Temper1AccessPO";
	}
}
