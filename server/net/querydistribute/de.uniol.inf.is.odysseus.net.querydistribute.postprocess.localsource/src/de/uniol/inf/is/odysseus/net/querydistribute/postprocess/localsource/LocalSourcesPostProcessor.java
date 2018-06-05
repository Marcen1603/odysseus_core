package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.localsource;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;

public class LocalSourcesPostProcessor implements IQueryDistributionPostProcessor {

	private static final long serialVersionUID = -3229847817618466296L;

	private static final Logger LOG = LoggerFactory.getLogger(LocalSourcesPostProcessor.class);
	
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
		return "localsources";
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters) throws QueryDistributionPostProcessorException {
		IOdysseusNode localNode = null;
		try {
			localNode = nodeManager.getLocalNode();
		} catch (OdysseusNetException e) {
			throw new QueryDistributionPostProcessorException("Could not set local sources", e);
		}
		
		LOG.debug("Forcing all source accesses to be executed locally");
		for(ILogicalQueryPart part : allocationMap.keySet().toArray(new ILogicalQueryPart[0])) {
			Collection<ILogicalOperator> sources = collectSources(part.getOperators());
			
			if(!sources.isEmpty() ) {
				LOG.debug("QueryPart {} has {} sources", part, sources.size());
				if(sources.size() == part.getOperators().size() ) {
					LOG.debug("Forcing query part {} to be executed locally", part);
					allocationMap.put(part, localNode);
				} else {
					LOG.debug("Splitting query part {} into local and non-local", part);
					part.removeOperators(sources);
					LOG.debug("Query part {} as the non-local one", part);
					ILogicalQueryPart localQueryPart = new LogicalQueryPart(sources);
					allocationMap.put(localQueryPart, localNode);
					LOG.debug("Query part {} as the local one", localQueryPart);
				}
			} else {
				LOG.debug("QueryPart {} has no sources", part);
			}
		}
	}

	private static Collection<ILogicalOperator> collectSources(Collection<ILogicalOperator> operators) {
		Preconditions.checkNotNull(operators, "Collection of operators must be not null!");
		
		Collection<ILogicalOperator> sources = Lists.newArrayList();
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof AbstractAccessAO || operator instanceof StreamAO ) {
				
				sources.add(operator);
				// What is this for? 
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					if(subToSink.getSink() instanceof RenameAO) {
						sources.add(subToSink.getSink());
					}
				}
				
			}
		}
		
		return sources;
	}
}
