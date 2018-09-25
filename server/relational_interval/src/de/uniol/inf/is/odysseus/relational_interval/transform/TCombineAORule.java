/**
 * 
 */
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.CombineAO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.CombinePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation Rule for creating a physical instance of the combine operator
 * 
 * @author Dennis Nowak
 *
 */
public class TCombineAORule extends AbstractTransformationRule<CombineAO> {


	@SuppressWarnings("rawtypes")
	@Override
	public void execute(CombineAO combineAO, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(combineAO, new CombinePO(combineAO.getWaitForAllChanged(),combineAO.getOutputPorts() ,combineAO.getOutputTupleSize(), combineAO.getBufferNewInputElements(), combineAO.isOutputOnHeartsbeat()),config,true,true);
		
	}

	@Override
	public boolean isExecutable(CombineAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super CombineAO> getConditionClass() {	
		return CombineAO.class;
	}

}
