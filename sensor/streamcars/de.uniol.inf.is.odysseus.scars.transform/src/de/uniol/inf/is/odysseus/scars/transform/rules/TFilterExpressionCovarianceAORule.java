package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterExpressionCovarianceUpdateAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterExpressionCovarianceUpdatePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterExpressionCovarianceAORule extends AbstractTransformationRule<FilterExpressionCovarianceUpdateAO> {

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public void execute(FilterExpressionCovarianceUpdateAO operator, TransformationConfiguration config) {
		
		System.out.print("CREATE Filter Expression Covariance PO...");
		FilterExpressionCovarianceUpdatePO filterCovarianceUpdatePO = new FilterExpressionCovarianceUpdatePO();
		filterCovarianceUpdatePO.setOutputSchema(operator.getOutputSchema());
		
		//TODO variablen getten und setten
		filterCovarianceUpdatePO.setPredictedObjectListPath(operator.getPredictedListPath());
		filterCovarianceUpdatePO.setScannedObjectListPath(operator.getScannedListPath());
		filterCovarianceUpdatePO.setExpressionString(operator.getExpressionString());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterCovarianceUpdatePO);
		for (ILogicalOperator o:toUpdate) {
			update(o);
		}
		insert(filterCovarianceUpdatePO);
		retract(operator);
		System.out.println("CREATE Filter Expression Covariance PO finished.");
	}

	@Override
	public boolean isExecutable(FilterExpressionCovarianceUpdateAO operator,TransformationConfiguration config) {
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
		return "FilterExpressionCovarianceUpdateAO -> FilterExpressionCovarianceUpdatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
