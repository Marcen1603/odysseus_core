/**
 * 
 */
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.plangenerator.util.PlanGeneratorHelper;

/**
 * @author Merlin Wasmann
 *
 */
public class DynamicProgramming extends AbstractPruningPlanGenerationMethod  {
	
	public DynamicProgramming(ICostModel<ILogicalOperator> costModel) {
		this.costModel = costModel;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.odysseus.plangenerator.generator.methods.IPlanGenerationMethod#generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator, de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration)
	 */
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();
		
		initialize(plan);
		this.topKJoin = 2;
		this.topKFill = 2;
//		this.topKJoin = Integer.valueOf(config.getValue().get("topkjoin"));
//		this.topKFill = Integer.valueOf(config.getValue().get("topkfill"));
		
		// walk through the different join plans.
		this.nWayJoinList = new ArrayList<Collection<ILogicalOperator>>();
		for (int i = 2; i < this.maxJoinCount; i++) {
			createJoinPlans(i, this.nWayJoinList);
			List<ILogicalOperator> prunedPlans = prunePlans(this.nWayJoinList.get(i), this.topKJoin);
			this.nWayJoinList.remove(i);
			this.nWayJoinList.add(prunedPlans);
		}
		
		createJoinPlans(this.maxJoinCount, this.nWayJoinList);
		List<ILogicalOperator> prunedPlans = prunePlans(this.nWayJoinList.get(this.maxJoinCount), this.topKFill);

		for(ILogicalOperator joinPlan : prunedPlans) {
			plans.add(fillPlanWithRemainingOperators(joinPlan));
		}
		
		for(ILogicalOperator p : plans) {
			PlanGeneratorHelper.setNewOwnerForPlan(p, owner);
		}
		
		return plans;
	}

}
