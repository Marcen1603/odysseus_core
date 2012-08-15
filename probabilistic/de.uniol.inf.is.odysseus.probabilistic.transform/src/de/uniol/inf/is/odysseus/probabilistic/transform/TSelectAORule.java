package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TSelectAORule  extends AbstractTransformationRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SelectAO selectAO, TransformationConfiguration transformConfig) {		
		ProbabilisticSelectPO<?> selectPO = new ProbabilisticSelectPO(selectAO.getPredicate());
		if (selectAO.getHeartbeatRate() > 0){
			selectPO.setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(
					selectAO.getHeartbeatRate()));
		}
		defaultExecute(selectAO, selectPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("probabilistic")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "SelectAO -> ProbabilisticSelectPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
