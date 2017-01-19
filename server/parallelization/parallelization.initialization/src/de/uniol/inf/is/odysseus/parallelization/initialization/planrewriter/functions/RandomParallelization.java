/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.functions;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.IPlanRewriteFunction;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class RandomParallelization implements IPlanRewriteFunction {

	private final static Logger LOG = LoggerFactory.getLogger(RandomParallelization.class);

	public final static String NAME = "RandomParallelization";

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
		int random = (int) (Math.random() * possibleParallelizations.size());
		LOG.debug(String.valueOf(random));
		IParallelizationIndividualConfiguration config = possibleParallelizations.get(random);
		config.setParallelizationDegree(PerformanceDetectionHelper.getNumberOfCores());
		config.setBufferSize(10000);
		LOG.debug("Set parallelization parallelizationDegree of operator " + config.getOperator().getUniqueIdentifier() + " to "
				+ PerformanceDetectionHelper.getNumberOfCores() + ".");
		LOG.debug("Partitioning strategy: " + config.getClass());
		config.execute(settings);
	}

}
