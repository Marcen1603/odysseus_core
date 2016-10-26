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
import de.uniol.inf.is.odysseus.parallelization.initialization.exception.ParallelizationTransormationException;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator.InterOperatorIndividualInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorIndividualInitializer;

/**
 * @author Dennis Nowak
 *
 */
public class Planrewriter {
	
	private final static Logger LOG = LoggerFactory.getLogger(Planrewriter.class);
	
	public void rewritePlan(ILogicalQuery query, QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters){
		//TODO
		try {
			InterOperatorIndividualInitializer.createInterIndividualConfiguration(config.values(), "test_inter", 4, 10000, "JoinTransformationStrategy", "HashFragmentAO");
		} catch (ParallelizationTransormationException e) {
			LOG.error(e.getMessage(), e);
		}
		IntraOperatorIndividualInitializer.createIndividualIntraOperatorConfiguration(config.values(), "test_intra", 4, 10000);
	}

}
