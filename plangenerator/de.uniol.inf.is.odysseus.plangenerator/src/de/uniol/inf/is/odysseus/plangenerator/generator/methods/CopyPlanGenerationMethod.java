/**
 * 
 */
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;

/**
 * This simply copies the given plan and returns the original and the copy as result.
 * 
 * @author Merlin Wasmann
 *
 */
public class CopyPlanGenerationMethod implements IPlanGenerationMethod {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.plangenerator.generator.methods.IPlanGenerationMethod#generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator, de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration, de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();
		CopyLogicalGraphVisitor<ILogicalOperator> visitor = new CopyLogicalGraphVisitor<ILogicalOperator>(owner);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
		plans.add(visitor.getResult());
		plans.add(plan);
		return plans;
	}

}
