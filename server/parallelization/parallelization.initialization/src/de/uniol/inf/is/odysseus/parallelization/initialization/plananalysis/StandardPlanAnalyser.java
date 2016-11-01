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
		// get logical sources
		FindSourcesLogicalVisitor<ILogicalOperator> sourceVisitor = new FindSourcesLogicalVisitor<>();
		GenericGraphWalker graphWalker = new GenericGraphWalker<>();
		graphWalker.prefixWalk(query.getLogicalPlan(), sourceVisitor);
		List<ILogicalOperator> sourcesList = sourceVisitor.getResult();
		LOG.debug("Number of sources in query " + query.getID() + ": " + sourcesList.size());
		for(int i=0;i<sourcesList.size();i++) {
			LOG.debug(sourcesList.get(i).getName());
		}
		//FIXME return list
		return null;
	}

}
