/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.functions;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.IPlanRewriteFunction;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class IncreasingParallelization implements IPlanRewriteFunction {

	private final static Logger LOG = LoggerFactory.getLogger(IncreasingParallelization.class);

	private final static String NAME = "IncreasingParallelization";

	private static int parallelizationDegree = 2;
	private static final int bufferSize = 100000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.
	 * IPlanRewriteFunction#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.
	 * IPlanRewriteFunction#chooseAndExecuteParallelization(java.util.List,
	 * java.util.Collection)
	 */
	@Override
	public void chooseAndExecuteParallelization(List<IParallelizationIndividualConfiguration> possibleParallelizations,
			Collection<IQueryBuildSetting<?>> settings) {
		for (IParallelizationIndividualConfiguration config : possibleParallelizations) {
			config.setParallelizationDegree(parallelizationDegree);
			config.setBufferSize(bufferSize);
			LOG.debug("Set parallelization degree of operator " + config.getOperator().getUniqueIdentifier() + " to "
					+ parallelizationDegree + ".");
			LOG.debug("Partitioning strategy: " + config.getClass());
			config.execute(settings);
		}
		parallelizationDegree++;

	}

}
