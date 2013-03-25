/**
 * 
 */
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * @author Merlin Wasmann
 *
 */
public class IterativeDynamicProgramming extends AbstractPruningPlanGenerationMethod  {
	
	public IterativeDynamicProgramming(ICostModel<ILogicalOperator> costModel) {
		this.costModel = costModel;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.odysseus.plangenerator.generator.methods.IPlanGenerationMethod#generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator, de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration)
	 */
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();
		
		// TODO: idp go!
		
		return plans;
	}
	
	

}
