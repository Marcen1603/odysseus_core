package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSourceAO;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access.RPiGPIOSourcePO;

public class TRPiGPIOSourceAORule extends AbstractTransformationRule<RPiGPIOSourceAO> {
	private static Logger LOG = LoggerFactory.getLogger(TRPiGPIOSourceAORule.class);
	
	@Override
	public void execute(RPiGPIOSourceAO operator,
			TransformationConfiguration config) throws RuleException {
		try {
			defaultExecute(operator, new RPiGPIOSourcePO(operator), config, true, true);
		} catch (Exception e) {
			LOG.error("Exception in TRPiGPIOSourceAORule");
			throw new RuleException("Could not create RPiGPIOSourcePO", e);
		}
	}

	@Override
	public boolean isExecutable(RPiGPIOSourceAO operator,
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
		return "RPiGPIOSourceAO -> RPiGPIOSourcePO";
	}
}
