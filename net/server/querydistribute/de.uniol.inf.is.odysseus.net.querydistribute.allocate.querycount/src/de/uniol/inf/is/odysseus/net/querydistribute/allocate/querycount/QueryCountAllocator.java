package de.uniol.inf.is.odysseus.net.querydistribute.allocate.querycount;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;

public class QueryCountAllocator implements IQueryPartAllocator {

	private static final long serialVersionUID = -6082815639172242805L;

	private static final Logger LOG = LoggerFactory.getLogger(QueryCountAllocator.class);
	
	private static IOdysseusNodeResourceUsageManager resourceUsageManager;

	// called by OSGi-DS
	public static void bindOdysseusNodeResourceUsageManager(IOdysseusNodeResourceUsageManager serv) {
		resourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeResourceUsageManager(IOdysseusNodeResourceUsageManager serv) {
		if (resourceUsageManager == serv) {
			resourceUsageManager = null;
		}
	}
	@Override
	public String getName() {
		return "querycount";
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		Collection<IOdysseusNode> consideredPeerIDs = Lists.newArrayList(knownRemoteNodes);
		if( consideredPeerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There is no node to allocate to");
		}
		
		NodeQueryCountMap queryCountMap = new NodeQueryCountMap(consideredPeerIDs, resourceUsageManager);
		Map<ILogicalQueryPart, IOdysseusNode> allocationMap = Maps.newHashMap();
		for( ILogicalQueryPart queryPart : queryParts ) {
			IOdysseusNode peerID = queryCountMap.getNodeWithLowestQueryCount();
			allocationMap.put(queryPart, peerID);
			LOG.info("Allocate peer {} to query part {}", peerID, queryPart);
			
			queryCountMap.incrementQueryCount(peerID);
		}
		
		return allocationMap;
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> reallocate(Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultyNodes, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// do all again
		return allocate(previousAllocationMap.keySet(), null, knownRemoteNodes, localNode, config, allocatorParameters);
	}

}
