package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.metric.MetricMeasureAO;
import de.uniol.inf.is.odysseus.broker.metric.MetricMeasurePO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMetricAORule extends AbstractTransformationRule<MetricMeasureAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(MetricMeasureAO metricMeasureAO, TransformationConfiguration transformConfig) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Transform MetricMeasureAO"); 
		MetricMeasurePO<?> metricMeasurePO = new MetricMeasurePO(metricMeasureAO.getOnAttribute());
		metricMeasurePO.setOutputSchema(metricMeasureAO.getOutputSchema());						
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(metricMeasureAO,metricMeasurePO);							
		for (ILogicalOperator o:toUpdate){
			update(o);
		}				
		retract(metricMeasureAO);	
		
	}

	@Override
	public boolean isExecutable(MetricMeasureAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();		
	}

	@Override
	public String getName() {
		return "MetricMeasureAO -> MetricMeasurePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
