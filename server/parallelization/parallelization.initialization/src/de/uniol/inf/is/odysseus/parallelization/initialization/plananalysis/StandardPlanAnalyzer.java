/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class StandardPlanAnalyzer implements IPlanAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.
	 * IPlanAnalyzer#getParalisableOperators(de.uniol.inf.is.odysseus.core.
	 * planmanagement.query.ILogicalQuery)
	 */
	@Override
	public List<IParallelizationIndividualConfiguration> getParalisableOperators(ILogicalQuery query) {
		GenericGraphWalker<ILogicalOperator> graphWalker = new GenericGraphWalker<>();
		FindParallelizationPossibilitiesVisitor<ILogicalOperator> parallelizeVisitor = new FindParallelizationPossibilitiesVisitor<>(
				String.valueOf(query.getID()));
		graphWalker.prefixWalk(query.getLogicalPlan().getRoot(), parallelizeVisitor);
		return parallelizeVisitor.getResult();
	}

}
