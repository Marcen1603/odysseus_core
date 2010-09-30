package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterGainAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterGainPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterGainAORule extends AbstractTransformationRule<FilterGainAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(FilterGainAO operator,
			TransformationConfiguration config) {
		
		System.out.print("CREATE Filter Gain PO...");

		FilterGainPO filterGainPO = new FilterGainPO();
		
		filterGainPO.setOutputSchema(operator.getOutputSchema());
		filterGainPO.setNewObjListPath( operator.getNewListName());
		filterGainPO.setOldObjListPath( operator.getOldListName());
		
		if (operator.getFunctionID().equals("KALMAN"))
			{
				System.out.print("  using KALMAN implementation");
				
				filterGainPO.setMetaDataCreationFunction(new KalmanGainFunction());
			}
			
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterGainPO);
			for (ILogicalOperator o:toUpdate)
			{
				update(o);
			}
			insert(filterGainPO);
			
		retract(operator);
		
		System.out.println("CREATE Filter Gain PO finished.");
		
	}

	@Override
	public boolean isExecutable(FilterGainAO operator,
			TransformationConfiguration config) {
		
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$filterGainAO : FilterGainAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "FilterGainAO -> FilterGainPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
