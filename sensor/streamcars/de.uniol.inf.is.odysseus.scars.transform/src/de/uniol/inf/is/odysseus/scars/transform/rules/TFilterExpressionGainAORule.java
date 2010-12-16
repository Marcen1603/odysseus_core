package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterExpressionGainAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterExpressionGainPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterExpressionGainAORule extends AbstractTransformationRule<FilterExpressionGainAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}
	

	@Override
	public void execute(FilterExpressionGainAO operator, TransformationConfiguration config) {
		System.out.print("CREATE Filter Expression Gain PO...");
		
		FilterExpressionGainPO filterGainPO = new FilterExpressionGainPO();
		filterGainPO.setOutputSchema(operator.getOutputSchema());
		filterGainPO.setPredictedObjectListPath(operator.getPredictedListPath());
		filterGainPO.setScannedObjectListPath(operator.getScannedListPath());
//		filterGainPO.setNewObjListPath( operator.getScannedListPath());
//		filterGainPO.setOldObjListPath( operator.getPredictedListPath());
		
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterGainPO);
		for (ILogicalOperator o:toUpdate) {
			update(o);
		}
		insert(filterGainPO);
		retract(operator);
	
		System.out.println("CREATE Filter Expression Gain PO finished.");
	}

	@Override
	public boolean isExecutable(FilterExpressionGainAO operator, TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "FilterExpressionGainAO -> FilterExpressionGainPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
