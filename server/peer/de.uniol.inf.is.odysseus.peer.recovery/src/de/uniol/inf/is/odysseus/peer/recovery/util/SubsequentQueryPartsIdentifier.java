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
	 * @param parts
	 *            A collection of all relevant query parts. <br />
	 *            Must be not null.
	 * @return A mapping of subsequent query parts to each part of
	 *         <code>parts</code>.
	 */
	public static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> determineSubsequentParts(
			Collection<ILogicalQueryPart> parts) {

		Preconditions.checkNotNull(parts,
				"The collection of query parts must be not null!");

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> subsequentPartsMap = Maps
				.newHashMap();

		for (ILogicalQueryPart part : parts) {

			subsequentPartsMap.put(part, determineSubsequentParts(part, parts));

		}

		return subsequentPartsMap;

	}

	/**
	 * Determination of all (not only directly) subsequent query parts.
	 * 
	 * @param part
	 *            The part, which subsequent parts shall be determined. <br />
	 *            Must be not null.
	 * @param allParts
	 *            A collection of all relevant query parts including
	 *            <code>part</code>. <br />
	 *            Must be not null.
	 */
	public static Collection<ILogicalQueryPart> determineSubsequentParts(
			ILogicalQueryPart part, Collection<ILogicalQueryPart> allParts) {

		Preconditions
				.checkNotNull(part,
						"The query part to determine subsequent parts for must be not null!");
		Preconditions.checkNotNull(allParts,
				"The collection of all query parts must be not null!");

		QueryPartGraph graph = new QueryPartGraph(allParts);
		Collection<ILogicalQueryPart> subsequentParts = Lists.newArrayList();

		determineSubsequentParts(part, graph, subsequentParts);
		return subsequentParts;

	}

	/**
	 * Recursive determination of all (not only directly) subsequent query
	 * parts.
	 * 
	 * @param part
	 *            The part, which subsequent parts shall be determined. <br />
	 *            Must be not null.
	 * @param graph
	 *            A {@link QueryPartGraph} containing all relevant query parts
	 *            including <code>part</code>. <br />
	 *            Must be not null.
	 * @param subsequentParts
	 *            A collection of all already collected subsequent query parts.
	 */
	private static void determineSubsequentParts(ILogicalQueryPart part,
			QueryPartGraph graph, Collection<ILogicalQueryPart> subsequentParts) {

		Preconditions
				.checkNotNull(part,
						"The query part to determine subsequent parts for must be not null!");
		Preconditions.checkNotNull(graph,
				"The graph of query parts must be not null!");
		Preconditions.checkArgument(graph.contains(part),
				"The graph of query parts must contain " + part + "!");
		if (subsequentParts == null) {

			subsequentParts = Lists.newArrayList();

		}

		QueryPartGraphNode node = graph.getGraphNode(part);
		Collection<QueryPartGraphConnection> outgoingConnections = node
				.getConnectionsAsStart();

		for (QueryPartGraphConnection connection : outgoingConnections) {

			ILogicalQueryPart subsequentPart = connection.getEndNode()
					.getQueryPart();
			subsequentParts.add(subsequentPart);
			determineSubsequentParts(subsequentPart, graph, subsequentParts);

		}

	}

}