package de.uniol.inf.is.odysseus.keyperformanceindicators.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator.KeyPerformanceIndicatorsAO;
import de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator.KeyPerformanceIndicatorsPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyPerformanceIndicatorsRule extends AbstractTransformationRule<KeyPerformanceIndicatorsAO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(KeyPerformanceIndicatorsAO operator,
			TransformationConfiguration config) throws RuleException {

		IMetadataMergeFunction metaDataMerge = MetadataRegistry.getMergeFunction(operator.getInputSchema(0).getMetaAttributeNames());
		
		TITransferArea<Tuple<ITimeInterval>, Tuple<ITimeInterval>> transferFunction = new TITransferArea<>();		

		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		
		defaultExecute(
				operator,
				new KeyPerformanceIndicatorsPO<>(operator.getOutputPorts(),
											   operator.getKpiName(),
											   operator.getSubsetOfTerms(),
											   operator.getTotalQuantityOfTerms(),
											   operator.getIncomingText(),
											   operator.getThresholdValue(),
											   operator.getUserNames(),
											   operator.getInputPorts(),
											   metaDataMerge,
											   transferFunction, sa
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
