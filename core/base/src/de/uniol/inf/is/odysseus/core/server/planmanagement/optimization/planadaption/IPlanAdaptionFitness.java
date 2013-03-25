/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Interface for a fitness calculating component for the adaption.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionFitness {

	Pair<ILogicalOperator, ICost<ILogicalOperator>> pickFittestPlan(ILogicalQuery query);
}
