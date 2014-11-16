package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

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
				info.setLocalPQL(LogicalQueryHelper.generatePQLStatementFromQueryPart(node.getQueryPart()));
				info.setLocationPeer(peerId);

				ILogicalQueryPart part = outConnection.getEndNode()
						.getQueryPart();
				if (!allocationMap.containsKey(part)) {

					LOG.error("Missing query part in allocation map: {}", part);
					continue;

				}
				info.setAboutPeer(allocationMap.get(part));

				String pqlStatement = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(part);
				info.setPQL(pqlStatement);

				Collection<IRecoveryBackupInformation> subsequentParts = calcSubsequentParts(
						outConnection.getEndNode(), sharedQueryId,
						allocationMap);
				for (IRecoveryBackupInformation subsequentPart : subsequentParts) {

					info.addSubsequentPartsInformation(subsequentPart);

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
	 * Calculates all backup information about subsequent query parts of a given
	 * query part.
	 * 
	 * @param node
	 *            The node representing the given query part. <br />
	 *            Must be not null.
	 * @param sharedQueryID
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 * @param allocationMap
	 *            The allocation map defined by the distribution process. <br />
	 *            Must be not null.
	 * @return All backup information about subsequent query parts of the given
	 *         query part.
	 */
	private static Set<IRecoveryBackupInformation> calcSubsequentParts(
			QueryPartGraphNode node, ID sharedQueryID,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		Preconditions.checkNotNull(node);
		Preconditions.checkNotNull(sharedQueryID);
		Preconditions.checkNotNull(allocationMap);

		Set<IRecoveryBackupInformation> subsequentParts = Sets.newHashSet();
		Set<QueryPartGraphConnection> visitedConnections = Sets.newHashSet();

		calcSubsequentPartsRecursive(node, sharedQueryID, allocationMap,
				subsequentParts, visitedConnections);

		return subsequentParts;

	}

	/**
	 * Calculates recursively all backup information about subsequent query
	 * parts of a given query part.
	 * 
	 * @param node
	 *            The node representing the given query part. <br />
	 *            Must be not null.
	 * @param sharedQueryID
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 * @param allocationMap
	 *            The allocation map defined by the distribution process. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            All already collected backup information about subsequent
	 *            query parts. <br />
	 *            Must be not null and will be muted.
	 * @param visitedConnections
	 *            All already visited connections between graph nodes. Needed to
	 *            avoid endless loops, if the graph contains cycles.
	 */
	private static final void calcSubsequentPartsRecursive(
			QueryPartGraphNode node, ID sharedQueryID,
			Map<ILogicalQueryPart, PeerID> allocationMap,
			Collection<IRecoveryBackupInformation> subsequentParts,
			Collection<QueryPartGraphConnection> visitedConnections) {

		Preconditions.checkNotNull(node);
		Preconditions.checkNotNull(sharedQueryID);
		Preconditions.checkNotNull(allocationMap);
		Preconditions.checkNotNull(subsequentParts);
		Preconditions.checkNotNull(visitedConnections);

		for (QueryPartGraphConnection outConnection : node
				.getConnectionsAsStart()) {

			if (visitedConnections.contains(outConnection)) {

				// avoid endless loops
				continue;

			} else {

				visitedConnections.add(outConnection);

			}

			QueryPartGraphNode endNode = outConnection.getEndNode();
			ILogicalQueryPart endPart = endNode.getQueryPart();
			String endPartPQL = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(endPart);
			PeerID endPartPeer = allocationMap.get(endPart);

			IRecoveryBackupInformation endPartInfo = new BackupInformation();
			endPartInfo.setSharedQuery(sharedQueryID);
			endPartInfo.setAboutPeer(endPartPeer);
			endPartInfo.setPQL(endPartPQL);
			endPartInfo.setLocalPQL(LogicalQueryHelper.generatePQLStatementFromQueryPart(node.getQueryPart()));

			for (IRecoveryBackupInformation info : subsequentParts) {

				if (info.getPQL().equals(
						LogicalQueryHelper
								.generatePQLStatementFromQueryPart(node
										.getQueryPart()))
						&& !info.getSubsequentPartsInformation().contains(
								endPartInfo)) {

					info.addSubsequentPartsInformation(endPartInfo);

				}

			}

			if (!subsequentParts.contains(endPartInfo)) {

				subsequentParts.add(endPartInfo);
				calcSubsequentPartsRecursive(outConnection.getEndNode(),
						sharedQueryID, allocationMap, subsequentParts,
						visitedConnections);

			}

		}

	}

}