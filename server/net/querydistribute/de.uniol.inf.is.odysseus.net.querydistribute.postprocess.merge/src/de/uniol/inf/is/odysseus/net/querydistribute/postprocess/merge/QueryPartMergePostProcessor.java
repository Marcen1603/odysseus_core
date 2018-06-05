package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.merge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;

public class QueryPartMergePostProcessor implements IQueryDistributionPostProcessor {

	private static final long serialVersionUID = -2833201754249111649L;
	
	private static final Logger LOG = LoggerFactory.getLogger(QueryPartMergePostProcessor.class);

	@Override
	public void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters) {
		LOG.debug("Merging query parts with same node if possible");

		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(allocationMap.keySet());
		Map<LogicalSubscription, ILogicalOperator> subs = determineSubscriptionsAcrossQueryParts(queryPartAssignment);

		for (LogicalSubscription sub : subs.keySet()) {
			ILogicalOperator startOperator = subs.get(sub);
			ILogicalOperator targetOperator = sub.getSink();

			ILogicalQueryPart startQueryPart = queryPartAssignment.get(startOperator);
			ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);

			IOdysseusNode startNode = allocationMap.get(startQueryPart);
			IOdysseusNode targetNode = allocationMap.get(targetQueryPart);

			if (startNode.equals(targetNode) && !startQueryPart.equals(targetQueryPart)) {
				LOG.debug("Merging query parts {} and {}", startQueryPart, targetQueryPart);
				ILogicalQueryPart mergedPart = mergeQueryParts(startQueryPart, targetQueryPart);

				allocationMap.remove(startQueryPart);
				allocationMap.remove(targetQueryPart);
				allocationMap.put(mergedPart, startNode);

				for (ILogicalOperator op : mergedPart.getOperators()) {
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
				ILogicalOperator targetOperator = sinkSub.getSink();

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

	@Override
	public String getName() {
		return "Merge";
	}

}
