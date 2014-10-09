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

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphConnection;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphNode;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformation;

/**
 * A helper class to determine all subsequent query parts for a given query
 * part. <br />
 * This class uses {@link QueryPartGraph} and subsequent query parts mean not
 * only direct subsequent ones but only indirect subsequent ones (having other
 * query parts in between). Furthermore, a subsequent query part will only be
 * considered, if it's executed on a different peer.
 * 
 * @author Michael Brand
 *
 */
public class SubsequentQueryPartsCalculator {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SubsequentQueryPartsCalculator.class);

	/**
	 * The calculated query part graphs mapped to the original queries.
	 */
	private Map<ILogicalQuery, QueryPartGraph> mGraphToQueryMap = Maps
			.newHashMap();

	/**
	 * Calculates the {@link QueryPartGraph} for a given query (to be
	 * distributed).
	 * 
	 * @param query
	 *            The original query. <br />
	 *            Must be not null.
	 * @param allocationMap
	 *            The allocated peers (their ids) to the parts of
	 *            <code>query</code>. <br />
	 *            Must be not null.
	 */
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

	/**
	 * Determines the backup information for a given query.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query.
	 * @param query
	 *            The query to distribute.
	 * @param allocationMap
	 *            The allocation map.
	 */
	public Collection<BackupInformation> calcBackupInformation(
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

		Collection<BackupInformation> backupInformation = Lists.newArrayList();
		QueryPartGraph graph = this.mGraphToQueryMap.get(query);
		Collection<QueryPartGraphNode> nodes = graph.getGraphNodes();

		for (QueryPartGraphNode node : nodes) {

			ILogicalQueryPart part = node.getQueryPart();
			if (!allocationMap.containsKey(part)) {

				LOG.error("Missing query part in allocation map: {}", part);
				continue;

			}
			String pqlStatement = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(part);
			BackupInformation info = new BackupInformation(sharedQueryId,
					pqlStatement);

			PeerID peerId = allocationMap.get(part);
			info.setPeer(peerId);

			Collection<ILogicalQueryPart> subsequentParts = calcSubsequentParts(node);
			for (ILogicalQueryPart subsequentPart : subsequentParts) {

				info.addSubsequentPart(LogicalQueryHelper
						.generatePQLStatementFromQueryPart(subsequentPart),
						allocationMap.get(subsequentPart));

			}

			backupInformation.add(info);

		}

		this.mGraphToQueryMap.remove(query);
		return backupInformation;

	}

	/**
	 * Calculates the subsequent query parts for a given query part (node).
	 * 
	 * @param node
	 *            The given query part node. <br />
	 *            Must be not null.
	 * @return A collection of all subsequent query parts for that part (node).
	 */
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

	/**
	 * Calculates recursively the subsequent query parts for a given query part
	 * (node).
	 * 
	 * @param node
	 *            The given query part node. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            A collection of all already calculated subsequent parts for
	 *            <code>node</code>. <br />
	 *            Must be not null.
	 * @return A collection of all subsequent query parts for that part (node).
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