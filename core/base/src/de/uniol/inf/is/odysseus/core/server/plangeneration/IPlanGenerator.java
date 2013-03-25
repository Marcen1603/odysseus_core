/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.plangeneration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * @author Merlin Wasmann
 *
 */
public interface IPlanGenerator {
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan, PlanGenerationConfiguration planGenerationConfig, IOperatorOwner owner);
}
