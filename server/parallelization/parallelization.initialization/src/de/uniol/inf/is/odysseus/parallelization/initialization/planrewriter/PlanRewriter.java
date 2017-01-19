/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.IPlanAnalyzer;
import de.uniol.inf.is.odysseus.parallelization.initialization.plananalysis.StandardPlanAnalyzer;
import de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.functions.IncreasingParallelization;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.IParallelizationIndividualConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class PlanRewriter {

	private final static Logger LOG = LoggerFactory.getLogger(PlanRewriter.class);

	private final IPlanAnalyzer planAnalyzer = new StandardPlanAnalyzer();
	// TODO make rewrite function configurable
	private final IPlanRewriteFunction rewriteFunction = new IncreasingParallelization();

	public void rewritePlan(ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters) {

		List<IParallelizationIndividualConfiguration> possibleParallelizations = planAnalyzer
				.getParalisableOperators(query);
		LOG.debug("Parallizable operators / regions: " + possibleParallelizations.size());

		rewriteFunction.chooseAndExecuteParallelization(possibleParallelizations, config.values());

	}

}
