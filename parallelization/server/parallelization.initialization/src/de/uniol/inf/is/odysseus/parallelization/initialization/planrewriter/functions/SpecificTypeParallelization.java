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
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class SpecificTypeParallelization implements IPlanRewriteFunction {

	private final static Logger LOG = LoggerFactory.getLogger(SpecificTypeParallelization.class);

	private final static String NAME = "SpecificTypeParallelization";

	private final Class<? extends IParallelizationIndividualConfiguration> specifiedClass = IntraOperatorParallelizationConfiguration.class;

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
			if (this.specifiedClass.isAssignableFrom(config.getClass())) {
				config.setParallelizationDegree(PerformanceDetectionHelper.getNumberOfCores());
				config.setBufferSize(10000);
				LOG.debug("Set parallelization parallelizationDegree of operator " + config.getOperator().getUniqueIdentifier()
						+ " to " + PerformanceDetectionHelper.getNumberOfCores() + ".");
				LOG.debug("Partitioning strategy: " + config.getClass());
				config.execute(settings);
			}
		}

	}

}
