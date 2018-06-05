package de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public interface IPlanAnalyzer {

	public List<IParallelizationIndividualConfiguration> getParalisableOperators(ILogicalQuery query);

}
