package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphConnection;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphNode;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;

public class BackupInformationCalculator {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationCalculator.class);

	/**
	 * The calculated query part graphs mapped to the original queries.
	 */
	private Map<ILogicalQuery, QueryPartGraph> mGraphToQueryMap = Maps
			.newHashMap();

	public void calcQueryPartGraph(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		Preconditions
				.checkNotNull(
						query,
						"The query, for which a query part graph shall be calculated, must be not null!");
		Preconditions
				.checkNotNull(allocationMap,
						"The allocation map to create a query part graph, must be not null!");

		QueryPartGraph graph = new QueryPartGraph(allocationMap.keySet());
		this.mGraphToQueryMap.put(query, graph);

	}

	public Map<PeerID, Collection<IRecoveryBackupInformation>> calcBackupInformation(
			ID sharedQueryId, ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		Preconditions.checkNotNull(sharedQueryId,
				"The shared query ID must be not null!");
		Preconditions
				.checkNotNull(query,
						"The query, for which a query part graph shall be read, must be not null!");
		Preconditions.checkArgument(
				this.mGraphToQueryMap.containsKey(query),
				"There is no query part graph for the query "
						+ query.toString());
		Preconditions
				.checkNotNull(allocationMap,
						"The allocation map to calcuate subsequent query parts, must be not null!");

		Map<PeerID, Collection<IRecoveryBackupInformation>> infoMap = Maps
				.newHashMap();
		QueryPartGraph graph = this.mGraphToQueryMap.get(query);
		Collection<QueryPartGraphNode> nodes = graph.getGraphNodes();

		for (QueryPartGraphNode node : nodes) {

			IRecoveryBackupInformation info = new BackupInformation();
			info.setSharedQuery(sharedQueryId);

			ILogicalQueryPart part = node.getQueryPart();
			if (!allocationMap.containsKey(part)) {

				LOG.error("Missing query part in allocation map: {}", part);
				continue;

			}

			String pqlStatement = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(part);
			info.setPQL(pqlStatement);

			PeerID peerId = allocationMap.get(part);
			info.setPeer(peerId);

			Collection<ILogicalQueryPart> subsequentParts = calcSubsequentParts(node);
			for (ILogicalQueryPart subsequentPart : subsequentParts) {

				info.addSubsequentPartsInformation(new Pair<String, PeerID>(
						LogicalQueryHelper
								.generatePQLStatementFromQueryPart(subsequentPart),
						allocationMap.get(subsequentPart)));

			}

			if (infoMap.containsKey(peerId)) {

				infoMap.get(peerId).add(info);

			} else {

				Collection<IRecoveryBackupInformation> infos = Sets
						.newHashSet(info);
				infoMap.put(peerId, infos);

			}

		}

		this.mGraphToQueryMap.remove(query);
		return infoMap;

	}

	private static Collection<ILogicalQueryPart> calcSubsequentParts(
			QueryPartGraphNode node) {

		Preconditions
				.checkNotNull(
						node,
						"The node, for which subsequent query parts shall be calculated, must be not null!");

		Collection<ILogicalQueryPart> subsequentParts = Lists.newArrayList();

		calcSubsequentPartsRecursive(node, subsequentParts);

		return subsequentParts;

	}

	private static final void calcSubsequentPartsRecursive(
			QueryPartGraphNode node,
			Collection<ILogicalQueryPart> subsequentParts) {

		Preconditions
				.checkNotNull(
						node,
						"The node, for which subsequent query parts shall be calculated, must be not null!");
		Preconditions.checkNotNull(subsequentParts,
				"The collection of subsequent parts must be not null!");

		for (QueryPartGraphConnection outConnection : node
				.getConnectionsAsStart()) {

			QueryPartGraphNode endNode = outConnection.getEndNode();
			ILogicalQueryPart endPart = endNode.getQueryPart();
			if (!subsequentParts.contains(endPart)) {

				subsequentParts.add(endPart);
				calcSubsequentPartsRecursive(outConnection.getEndNode(),
						subsequentParts);

			}

		}

	}

}