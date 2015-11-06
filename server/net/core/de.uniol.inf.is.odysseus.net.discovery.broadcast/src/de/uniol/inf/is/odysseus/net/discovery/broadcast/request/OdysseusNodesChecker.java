package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;

public final class OdysseusNodesChecker {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodesChecker.class);

	private static final long LOST_NODE_TIME_MILLIS = 15 * 1000;
	private static final Map<IOdysseusNode, Long> TIMESTAMP_MAP = Maps.newHashMap();

	private OdysseusNodesChecker() {
		// no instance allowed
	}

	// called from broadcast discoverer
	public static void addAndRefreshFoundNode(IOdysseusNode newNode) {
		Preconditions.checkNotNull(newNode, "newNode must not be null!");

		IOdysseusNodeManager nodeManager = BroadcastDiscoveryPlugIn.getOdysseusNodeManager();
		if (!nodeManager.existsNode(newNode)) {
			nodeManager.addNode(newNode);
		}

		synchronized (TIMESTAMP_MAP) {
			TIMESTAMP_MAP.put(newNode, System.currentTimeMillis());
		}
	}

	// called from broadcast sender
	public static void detectLostNodes() {
		LOG.debug("Checking for lost nodes");

		long currentTime = System.currentTimeMillis();
		IOdysseusNodeManager nodeManager = BroadcastDiscoveryPlugIn.getOdysseusNodeManager();
		IOdysseusNodeConnectionManager connectionManager = BroadcastDiscoveryPlugIn.getOdysseusNodeConnectionManager();
		if( nodeManager == null || connectionManager == null ) {
			return;
		}
		
		synchronized (TIMESTAMP_MAP) {

			List<IOdysseusNode> nodesToRemove = Lists.newArrayList();

			for (IOdysseusNode node : TIMESTAMP_MAP.keySet()) {
				if (!connectionManager.isConnected(node)) {
					long timestamp = TIMESTAMP_MAP.get(node);

					if (currentTime - timestamp > LOST_NODE_TIME_MILLIS) {
						LOG.info("Had no broadcast answer at least since {} ms from node {}.", LOST_NODE_TIME_MILLIS, node);
						nodeManager.removeNode(node);
						nodesToRemove.add(node);
					}
				}
			}

			for (IOdysseusNode nodeToRemove : nodesToRemove) {
				TIMESTAMP_MAP.remove(nodeToRemove);
			}
		}
	}

}
