package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterExpressionEstimateUpdateAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterExpressionEstimateUpdatePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterExpressionEstimateAORule  extends AbstractTransformationRule<FilterExpressionEstimateUpdateAO> {

	@Override
	public int getPriority() {
		return 20;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FilterExpressionEstimateUpdateAO operator,TransformationConfiguration config) {
		System.out.print("CREATE Filter Expression Estimate PO...");
		FilterExpressionEstimateUpdatePO filterEstimateUpdatePO = new FilterExpressionEstimateUpdatePO();
		filterEstimateUpdatePO.setOutputSchema(operator.getOutputSchema());
		
		filterEstimateUpdatePO.setPredictedObjectListPath(operator.getOldObjListPath());
		filterEstimateUpdatePO.setScannedObjectListPath(operator.getNewObjListPath());
		filterEstimateUpdatePO.setExpressions(operator.getExpressions());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterEstimateUpdatePO);
		for (ILogicalOperator o:toUpdate) {
			update(o);
		}
		insert(filterEstimateUpdatePO);
		retract(operator);
	
		System.out.println("CREATE Filter Expression Estimate PO finished.");
	}

	@Override
	public boolean isExecutable(FilterExpressionEstimateUpdateAO operator,TransformationConfiguration config) {
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
		return "FilterExpressionEstimateUpdateAO -> FilterExpressionEstimateUpdatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
