package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphConnection;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraphNode;

/**
 * A helper class to determine all subsequent query parts for a given query
 * part. <br />
 * This class uses {@link QueryPartGraph} and subsequent query parts mean not
 * only direct subsequent ones but only indirect subsequent ones (having other
 * query parts in between).
 * 
 * @author Michael Brand
 *
 */
public class SubsequentQueryPartsIdentifier {

	/**
	 * Determination of all (not only directly) subsequent query parts.
	 * 
	 * @param @param graph A {@link QueryPartGraph} containing all relevant
	 *        query parts. <br />
	 *        Must be not null.
	 * @return A mapping of subsequent query parts to each part of
	 *         <code>parts</code>.
	 */
	public static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> determineSubsequentParts(
			QueryPartGraph graph) {

		Preconditions.checkNotNull(graph,
				"The graph of query parts must be not null!");

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> subsequentPartsMap = Maps
				.newHashMap();

		for (QueryPartGraphNode node : graph.getGraphNodes()) {

			subsequentPartsMap.put(node.getQueryPart(),
					determineSubsequentParts(graph, node));

		}

		return subsequentPartsMap;

	}

	/**
	 * Determination of all (not only directly) subsequent query parts.
	 * 
	 * @param graph
	 *            A {@link QueryPartGraph} containing all relevant query parts
	 *            including the part of <code>node</code>. <br />
	 *            Must be not null.
	 * @param node
	 *            The node containing the part, which subsequent parts shall be
	 *            determined. <br />
	 *            Must be not null.
	 */
	public static Collection<ILogicalQueryPart> determineSubsequentParts(
			QueryPartGraph graph, QueryPartGraphNode node) {

		Preconditions.checkNotNull(graph,
				"The graph of query parts must be not null!");
		Preconditions
				.checkNotNull(
						node,
						"The node of the query part to determine subsequent parts for must be not null!");

		Collection<ILogicalQueryPart> subsequentParts = Lists.newArrayList();

		determineSubsequentParts(node, graph, subsequentParts);
		return subsequentParts;

	}

	/**
	 * Recursive determination of all (not only directly) subsequent query
	 * parts.
	 * 
	 * @param node
	 *            The node containing the part, which subsequent parts shall be
	 *            determined. <br />
	 *            Must be not null.
	 * @param graph
	 *            A {@link QueryPartGraph} containing all relevant query parts
	 *            including the part of <code>node</code>. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            A collection of all already collected subsequent query parts.
	 */
	private static void determineSubsequentParts(QueryPartGraphNode node,
			QueryPartGraph graph, Collection<ILogicalQueryPart> subsequentParts) {

		Preconditions
				.checkNotNull(
						node,
						"The node of the query part to determine subsequent parts for must be not null!");
		Preconditions.checkNotNull(graph,
				"The graph of query parts must be not null!");
		Preconditions.checkArgument(graph.contains(node.getQueryPart()),
				"The graph of query parts must contain " + node.getQueryPart()
						+ "!");
		if (subsequentParts == null) {

			subsequentParts = Lists.newArrayList();

		}

		Collection<QueryPartGraphConnection> outgoingConnections = node
				.getConnectionsAsStart();

		for (QueryPartGraphConnection connection : outgoingConnections) {

			QueryPartGraphNode subsequentNode = connection.getEndNode();
			subsequentParts.add(subsequentNode.getQueryPart());
			determineSubsequentParts(subsequentNode, graph, subsequentParts);

		}

	}

}