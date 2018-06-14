package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.localsink;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;

public class LocalSinkPostProcessor implements IQueryDistributionPostProcessor {

	private static final long serialVersionUID = 3619658181784800329L;

	private static final Logger LOG = LoggerFactory.getLogger(LocalSinkPostProcessor.class);

	private static IOdysseusNodeManager nodeManager;

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}

	@Override
	public String getName() {
		return "localsinks";
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters) throws QueryDistributionPostProcessorException {
		IOdysseusNode localNode = tryGetLocalNode();

		LOG.debug("Begin post processing");

		for (ILogicalQueryPart queryPart : allocationMap.keySet().toArray(new ILogicalQueryPart[0])) {
			LOG.debug("Determining real sinks from query part {} to insert operators", queryPart);

			Collection<ILogicalOperator> relativeSinks = getRelativeSinksOfLogicalQueryPart(queryPart);
			for (ILogicalOperator relativeSink : relativeSinks) {
				if (isRealSink(relativeSink)) {
					LOG.debug("Found real sink {}", relativeSink);
					allocationMap.put(queryPart, localNode);
				}
			}
		}
	}

	private IOdysseusNode tryGetLocalNode() throws QueryDistributionPostProcessorException {
		try {
			return nodeManager.getLocalNode();
		} catch (OdysseusNetException e) {
			throw new QueryDistributionPostProcessorException("Could not postprocess", e);
		}
	}

	private static boolean isRealSink(ILogicalOperator operator) {
		return (operator.isSinkOperator() && !operator.isSourceOperator()) || operator.getSubscriptions().isEmpty();
	}

	private static Collection<ILogicalOperator> getRelativeSinksOfLogicalQueryPart(final ILogicalQueryPart part) {
		Collection<ILogicalOperator> relativeSinks = Lists.newArrayList();

		ImmutableCollection<ILogicalOperator> operators = part.getOperators();
		for (ILogicalOperator operator : operators) {
			Collection<LogicalSubscription> subscriptions = operator.getSubscriptions();

			if (subscriptions.isEmpty()) {
				relativeSinks.add(operator);
				continue;
			}

			for (LogicalSubscription subToSink : subscriptions) {
				if (!operators.contains(subToSink.getSink())) {
					relativeSinks.add(operator);
					break;
				}
			}
		}

		return relativeSinks;
	}
}
