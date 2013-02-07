package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticContinuousJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TContinuousEquiJoinAOSetDMRule
		extends
		AbstractTransformationRule<ProbabilisticContinuousJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(
			ProbabilisticContinuousJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator,
			TransformationConfiguration config) {
		// operator.setDataMerge();
		update(operator);
	}

	@Override
	public boolean isExecutable(
			ProbabilisticContinuousJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator,
			TransformationConfiguration config) {
		if (config.getDataTypes().contains(TransformUtil.DATATYPE)) {
			if (operator.getDataMerge() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Insert DataMergeFunction ProbabilisticContinuousJoinPO (Probabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super ProbabilisticContinuousJoinPO<?, ?>> getConditionClass() {
		return ProbabilisticContinuousJoinPO.class;
	}

}
