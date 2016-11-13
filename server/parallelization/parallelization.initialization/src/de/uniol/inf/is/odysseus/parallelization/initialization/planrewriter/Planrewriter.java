/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.initialization.exception.ParallelizationTransormationException;
import de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.IPlanAnalyser;
import de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.StandardPlanAnalyser;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator.InterOperatorIndividualInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorIndividualInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class Planrewriter {

	private final static Logger LOG = LoggerFactory.getLogger(Planrewriter.class);

	public void rewritePlan(ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters) {
		// TODO remove test configs
		try {
			InterOperatorIndividualInitializer.createInterIndividualConfiguration(config.values(), "test_inter", 4,
					10000, "JoinTransformationStrategy", "HashFragmentAO");
		} catch (ParallelizationTransormationException e) {
			LOG.error(e.getMessage(), e);
		}
		IntraOperatorIndividualInitializer.createIndividualIntraOperatorConfiguration(config.values(), "test_intra", 4,
				10000);
		// TODO use List
		IPlanAnalyser planAnalyzer = new StandardPlanAnalyser();
		List<IParallelizationIndividualConfiguration> possibleParallelizations = planAnalyzer
				.getParalisableOperators(query);
		LOG.debug("Parallizable operators / regions: " + possibleParallelizations.size());
		chooseParallelizationRandom(possibleParallelizations, config.values());
	}

	private void chooseParallelizationRandom(List<IParallelizationIndividualConfiguration> possibleParallelizations, Collection<IQueryBuildSetting<?>> settings) {
		int random = (int) (Math.random() * possibleParallelizations.size());
		LOG.debug(String.valueOf(random));
		IParallelizationIndividualConfiguration config = possibleParallelizations.get(random);
		config.setParallelizationDegree(PerformanceDetectionHelper.getNumberOfCores());
		config.setBufferSize(10000);
		LOG.debug("Set parallelization degree of operator " + config.getOperator().getUniqueIdentifier() + " to "
				+ PerformanceDetectionHelper.getNumberOfCores() + ".");
		LOG.debug("Partitioning strategy: " + config.getClass());
		config.execute(settings);
	}

	private void chooseParallelizationIntra(List<IParallelizationIndividualConfiguration> possibleParallelizations, Collection<IQueryBuildSetting<?>> settings) {
		for (IParallelizationIndividualConfiguration config : possibleParallelizations) {
			if (config instanceof IntraOperatorParallelizationConfiguration) {
				config.setParallelizationDegree(PerformanceDetectionHelper.getNumberOfCores());
				config.setBufferSize(10000);
				LOG.debug("Set parallelization degree of operator " + config.getOperator().getUniqueIdentifier()
						+ " to " + PerformanceDetectionHelper.getNumberOfCores() + ".");
				LOG.debug("Partitioning strategy: " + config.getClass());
				config.execute(settings);
			}
		}
	}
	
	private void chooseParallelizationAllTwice(List<IParallelizationIndividualConfiguration> possibleParallelizations, Collection<IQueryBuildSetting<?>> settings) {
		for(IParallelizationIndividualConfiguration config: possibleParallelizations){
			config.setParallelizationDegree(2);
			config.setBufferSize(10000);
			LOG.debug("Set parallelization degree of operator " + config.getOperator().getUniqueIdentifier()
					+ " to " + PerformanceDetectionHelper.getNumberOfCores() + ".");
			LOG.debug("Partitioning strategy: " + config.getClass());
			config.execute(settings);
		}
	}

}
