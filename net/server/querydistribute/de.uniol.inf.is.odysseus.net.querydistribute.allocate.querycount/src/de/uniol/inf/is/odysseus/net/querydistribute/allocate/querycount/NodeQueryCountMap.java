package de.uniol.inf.is.odysseus.net.querydistribute.allocate.querycount;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;

public class NodeQueryCountMap {

	private static final Logger LOG = LoggerFactory.getLogger(NodeQueryCountMap.class);
	
	private final Map<IOdysseusNode, Integer> nodeQueryCountMap = Maps.newHashMap();
	
	public NodeQueryCountMap( Collection<IOdysseusNode> nodes, IOdysseusNodeResourceUsageManager manager ) {
		Preconditions.checkNotNull(nodes, "Collection of nodes must not be null!");
		Preconditions.checkArgument(!nodes.isEmpty(), "Collection of nodes must not be empty!");
		Preconditions.checkNotNull(manager, "OdysseusNodeResourceUsageManager must not be null!");
		
		for( IOdysseusNode node : nodes ) {
			try {
				Future<Optional<IResourceUsage>> remoteUsageFuture = manager.getRemoteResourceUsage(node, true);
				Optional<IResourceUsage> optRemoteUsage = remoteUsageFuture.get();
				if( optRemoteUsage.isPresent() ) {
					nodeQueryCountMap.put(node, optRemoteUsage.get().getRunningQueriesCount() + optRemoteUsage.get().getStoppedQueriesCount());
				} else {
					nodeQueryCountMap.put(node, 0);
				}
			} catch (InterruptedException | ExecutionException e) {
				LOG.warn("Could not determine resource usage", e);
				
				nodeQueryCountMap.put(node, 0);
			}
		}
		
		LOG.info("Current query counts of nodes");
		for( IOdysseusNode node : nodes ) {
			LOG.info("{}: {}", node.getName(), nodeQueryCountMap.get(node));
		}
	}
	
	public IOdysseusNode getNodeWithLowestQueryCount() {
		IOdysseusNode minNode = null;
		int minQueryCount = Integer.MAX_VALUE;
		
		for( IOdysseusNode node : nodeQueryCountMap.keySet() ) {
			Integer queryCount = nodeQueryCountMap.get(node);
			if( queryCount < minQueryCount ) {
				minQueryCount = queryCount;
				minNode = node;
			}
		}
		
		LOG.info("Current node with minimum query count of {}: {}", minQueryCount,minNode);
		return minNode;
	}

	public void incrementQueryCount(IOdysseusNode node) {
		if( nodeQueryCountMap.containsKey(node) ) {
			nodeQueryCountMap.put( node, nodeQueryCountMap.get(node) + 1);
			
			LOG.info("New query count for peer {}: {}", node, nodeQueryCountMap.get(node));
		}
	}
}
