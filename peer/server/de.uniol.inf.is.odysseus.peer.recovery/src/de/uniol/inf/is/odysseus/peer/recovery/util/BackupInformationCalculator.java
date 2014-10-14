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

/**
 * A helper class to calculate backup information about a distributed query.
 * 
 * @author Michael Brand
 *
 */
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

	/**
	 * Calculates and stores the query part graph of a given query.
	 * 
	 * @param query
	 *            The given query. <br />
	 *            Must be not null.
	 * @param parts
	 *            The query parts of <code>query</code>. Must be not null.
	 */
	public void calcQueryPartGraph(ILogicalQuery query,
			Collection<ILogicalQueryPart> parts) {

		Preconditions.checkNotNull(query);
		Preconditions.checkNotNull(parts);

		QueryPartGraph graph = new QueryPartGraph(parts);
		this.mGraphToQueryMap.put(query, graph);

	}

	/**
	 * Calculates the backup information about a given query. <br />
	 * Note that {@link #calcQueryPartGraph(ILogicalQuery, Collection)} must be
	 * called before with <code>query</code>.
	 * 
	 * @param sharedQueryId
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 * @param query
	 *            The given query. <br />
	 *            Must be not null.
	 * @param allocationMap
	 *            The allocation map defined by the distribution process. <br />
	 *            Must be not null.
	 * @return A mapping of backup information to those peers, where they should
	 *         be stored.
	 */
	public Map<PeerID, Collection<IRecoveryBackupInformation>> calcBackupInformation(
			ID sharedQueryId, ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(query);
		Preconditions.checkArgument(this.mGraphToQueryMap.containsKey(query));
		Preconditions.checkNotNull(allocationMap);

		Map<PeerID, Collection<IRecoveryBackupInformation>> infoMap = Maps
				.newHashMap();
		QueryPartGraph graph = this.mGraphToQueryMap.get(query);
		Collection<QueryPartGraphNode> nodes = graph.getGraphNodes();

		for (QueryPartGraphNode node : nodes) {

			PeerID peerId = allocationMap.get(node.getQueryPart());

			for (QueryPartGraphConnection outConnection : node
					.getConnectionsAsStart()) {

				IRecoveryBackupInformation info = new BackupInformation();
				info.setSharedQuery(sharedQueryId);

				ILogicalQueryPart part = outConnection.getEndNode()
						.getQueryPart();
				if (!allocationMap.containsKey(part)) {

					LOG.error("Missing query part in allocation map: {}", part);
					continue;

				}
				info.setPeer(allocationMap.get(part));

				String pqlStatement = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(part);
				info.setPQL(pqlStatement);

				Collection<ILogicalQueryPart> subsequentParts = calcSubsequentParts(outConnection
						.getEndNode());
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

		}

		this.mGraphToQueryMap.remove(query);
		return infoMap;

	}

	/**
	 * Calculates all subsequent parts for a given query part node.
	 * 
	 * @param node
	 *            The given node. <br />
	 *            Must be not null.
	 * @return A collection of all nodes, which can be accessed by outgoing
	 *         query part connections direct or via other subsequent parts.
	 */
	private static Collection<ILogicalQueryPart> calcSubsequentParts(
			QueryPartGraphNode node) {

		Preconditions.checkNotNull(node);

		Collection<ILogicalQueryPart> subsequentParts = Lists.newArrayList();

		calcSubsequentPartsRecursive(node, subsequentParts);

		return subsequentParts;

	}

	/**
	 * Calculates recursively all subsequent parts for a given query part node.
	 * 
	 * @param node
	 *            The given node. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            All already collected nodes, which can be accessed by outgoing
	 *            query part connections direct or via other subsequent parts. <br />
	 *            Will be enhanced with every recursion step.
	 */
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