/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Alexander
 *
 */
public class TGPUJoinPORule extends AbstractTransformationRule<JoinAO> {

	@Override
	public int getPriority() {
		return 100; // TODO: muss geändert
	}
	
	@Override
	public void execute(JoinAO operator, TransformationConfiguration config)
			throws RuleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration config) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return null;
	}

}
