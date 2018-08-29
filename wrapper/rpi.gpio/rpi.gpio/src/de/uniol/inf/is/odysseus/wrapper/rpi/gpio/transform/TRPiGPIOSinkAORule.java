package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.transform;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSinkAO;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access.RPiGPIOSinkPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class TRPiGPIOSinkAORule extends AbstractTransformationRule<RPiGPIOSinkAO> {
	private static Logger LOG = LoggerFactory.getLogger(TRPiGPIOSinkAORule.class);
	
	@Override
	public void execute(RPiGPIOSinkAO operator,
			TransformationConfiguration transformConfig) throws RuleException {
		try {
			defaultExecute(operator, new RPiGPIOSinkPO(operator, operator.getOutputSchema()), transformConfig, true, true);
		} catch (Exception e) {
			LOG.error("Exception in TRPiGPIOSinkAORule");
			throw new RuleException("Could not create RPiGPIOSinkPO", e);
		}
	}
	
	@Override
	public boolean isExecutable(RPiGPIOSinkAO operator,
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
		return "RPiGPIOSinkAO -> RPiGPIOSinkPO";
	}
}
