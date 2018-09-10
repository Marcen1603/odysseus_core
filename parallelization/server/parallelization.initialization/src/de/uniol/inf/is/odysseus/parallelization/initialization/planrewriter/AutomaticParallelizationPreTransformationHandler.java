/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Dennis Nowak
 *
 */
public class AutomaticParallelizationPreTransformationHandler implements IPreTransformationHandler {

	private final static Logger LOG = LoggerFactory.getLogger(AutomaticParallelizationPreTransformationHandler.class);

	private final static PlanRewriter rewriter = new PlanRewriter();
	
	public final static String HANDLER_NAME = "AutomaticParallelizationPreTransformatinHandler";

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		LOG.info("Starting automatic configuration of parallelization.");
		rewriter.rewritePlan(query, config, handlerParameters);
	}

}
