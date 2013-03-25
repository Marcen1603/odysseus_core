package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * Interface for a plan generation method.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanGenerationMethod {

	/**
	 * Generate a list of logical plans based on the given plan and configuration.
	 * 
	 * @param plan Logical plan on which the list of plans is based.
	 * @param config Configuration for the different plan generation methods.
	 * @return a list of logical plans.
	 */
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan, PlanGenerationConfiguration config, IOperatorOwner owner);
}
