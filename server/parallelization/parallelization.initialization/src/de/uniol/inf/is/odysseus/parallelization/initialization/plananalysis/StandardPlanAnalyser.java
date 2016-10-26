/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis
 *
 */
public class StandardPlanAnalyser extends AbstractPlanAnalyser {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.IPlanAnalyser#getParalisableOperators(de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery)
	 */
	@Override
	public List<IParallelizationIndividualConfiguration> getParalisableOperators(ILogicalQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
