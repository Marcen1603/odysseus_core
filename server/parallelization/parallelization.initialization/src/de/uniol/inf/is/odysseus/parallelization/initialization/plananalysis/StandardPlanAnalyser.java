/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis
 *
 */
public class StandardPlanAnalyser extends AbstractPlanAnalyser {
	
	private static Logger LOG = LoggerFactory.getLogger(StandardPlanAnalyser.class);

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.IPlanAnalyser#getParalisableOperators(de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery)
	 */
	@Override
	public List<IParallelizationIndividualConfiguration> getParalisableOperators(ILogicalQuery query) {
		GenericGraphWalker graphWalker = new GenericGraphWalker<>();
		FindParallelizationPossibilitiesVisitor<ILogicalOperator> parallelizeVisitor = new FindParallelizationPossibilitiesVisitor<>(String.valueOf(query.getID()));
		graphWalker.prefixWalk(query.getLogicalPlan(), parallelizeVisitor);
		return parallelizeVisitor.getResult();
	}

}
