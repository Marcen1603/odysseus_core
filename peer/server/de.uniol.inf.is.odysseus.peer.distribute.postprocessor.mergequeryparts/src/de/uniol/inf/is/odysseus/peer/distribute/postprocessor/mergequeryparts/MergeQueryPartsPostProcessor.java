package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.mergequeryparts;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryDistributionPostProcessorException;

public class MergeQueryPartsPostProcessor implements IQueryDistributionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MergeQueryPartsPostProcessor.class);
	
	@Override
	public String getName() {
		return "merge";
	}

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters) throws QueryDistributionPostProcessorException {
		LOG.debug("Merging query parts with same peerid if possible");
		
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(allocationMap.keySet());
		Map<LogicalSubscription, ILogicalOperator> subs = determineSubscriptionsAcrossQueryParts(queryPartAssignment);
		
		for( LogicalSubscription sub : subs.keySet()) {
			ILogicalOperator startOperator = subs.get(sub);
			ILogicalOperator targetOperator = sub.getTarget();
			
			ILogicalQueryPart startQueryPart = queryPartAssignment.get(startOperator);
			ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
			
			PeerID startPeerID = allocationMap.get(startQueryPart);
			PeerID targetPeerID = allocationMap.get(targetQueryPart);
			
			if( startPeerID.equals(targetPeerID) && !startQueryPart.equals(targetQueryPart)) {
				LOG.debug("Merging query parts {} and {}", startQueryPart, targetQueryPart);
				ILogicalQueryPart mergedPart = mergeQueryParts(startQueryPart, targetQueryPart);
				
				allocationMap.remove(startQueryPart);
				allocationMap.remove(targetQueryPart);
				allocationMap.put(mergedPart, startPeerID);
				
				for(ILogicalOperator op : mergedPart.getOperators()) {
					queryPartAssignment.put(op, mergedPart);
				}
			}
		}
	}

	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for (ILogicalQueryPart part : queryParts) {
			for (ILogicalOperator operator : part.getOperators()) {
				map.put(operator, part);
			}
		}
		return map;
	}
	
	private static Map<LogicalSubscription, ILogicalOperator> determineSubscriptionsAcrossQueryParts(Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment) {
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(queryPartAssignment.keySet());

		Map<LogicalSubscription, ILogicalOperator> subsToReplace = Maps.newHashMap();
		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator currentOperator = operatorsToVisit.remove(0);

			Collection<LogicalSubscription> sinkSubs = currentOperator.getSubscriptions();
			for (LogicalSubscription sinkSub : sinkSubs) {
				ILogicalOperator targetOperator = sinkSub.getTarget();

				ILogicalQueryPart currentQueryPart = queryPartAssignment.get(currentOperator);
				ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
				if (!currentQueryPart.equals(targetQueryPart)) {
					subsToReplace.put(sinkSub, currentOperator);
				}
			}
		}
		return subsToReplace;
	}

	private static ILogicalQueryPart mergeQueryParts(ILogicalQueryPart startQueryPart, ILogicalQueryPart targetQueryPart) {
		Collection<ILogicalOperator> mergedOperators = Lists.newArrayList();
		mergedOperators.addAll(startQueryPart.getOperators());
		mergedOperators.addAll(targetQueryPart.getOperators());
		
		return new LogicalQueryPart(mergedOperators);
	}
}
