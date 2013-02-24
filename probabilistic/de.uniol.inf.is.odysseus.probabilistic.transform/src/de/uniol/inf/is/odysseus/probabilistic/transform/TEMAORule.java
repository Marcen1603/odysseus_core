package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.EMAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.EMPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TEMAORule extends AbstractTransformationRule<EMAO> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(EMAO operator, TransformationConfiguration config) {
		IPhysicalOperator emPO = new EMPO<ITimeInterval>();
		this.defaultExecute(operator, emPO, config, true, true);

	}

	@Override
	public boolean isExecutable(EMAO operator,
			TransformationConfiguration config) {
		if ((config.getDataTypes().contains(TransformUtil.DATATYPE))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "EMAO -> EMPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super EMAO> getConditionClass() {
		return EMAO.class;
	}

}
