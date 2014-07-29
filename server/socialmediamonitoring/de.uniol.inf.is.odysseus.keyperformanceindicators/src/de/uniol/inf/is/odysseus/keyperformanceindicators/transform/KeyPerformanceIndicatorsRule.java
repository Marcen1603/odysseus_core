package de.uniol.inf.is.odysseus.keyperformanceindicators.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator.KeyPerformanceIndicatorsAO;
import de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator.KeyPerformanceIndicatorsPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class KeyPerformanceIndicatorsRule extends AbstractTransformationRule<KeyPerformanceIndicatorsAO> {

	@Override
	public void execute(KeyPerformanceIndicatorsAO operator,
			TransformationConfiguration config) throws RuleException {

		CombinedMergeFunction<ITimeInterval> metaDataMerge = new CombinedMergeFunction<ITimeInterval>();
		TITransferArea<Tuple<ITimeInterval>, Tuple<ITimeInterval>> transferFunction = new TITransferArea<>();
		
		defaultExecute(
				operator,
				new KeyPerformanceIndicatorsPO<>(operator.getOutputPorts(),
											   operator.getDomain(),
											   operator.getKpiName(),
											   operator.getSubsetOfTerms(),
											   operator.getTotalQuantityOfTerms(),
											   operator.getIncomingText(),
											   operator.getThresholdValue(),
											   operator.getUserIDs(),
											   operator.getInputPorts(),
											   metaDataMerge,
											   transferFunction
											   ),
											  config, true, true		
						);
	}
	
	@Override
	public String getName() {
		return "KeyPerformanceIndicatorsAO -> KeyPerformanceIndicatorsPO";
	}

	@Override
	public boolean isExecutable(KeyPerformanceIndicatorsAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super KeyPerformanceIndicatorsAO> getConditionClass() {
		return KeyPerformanceIndicatorsAO.class;
	}

}
