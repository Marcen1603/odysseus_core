package de.uniol.inf.is.odysseus.net.querydistribute.allocate.user;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;

public class UserAllocator implements IQueryPartAllocator {

	private static final long serialVersionUID = 3337074226754855028L;

	private static final Logger LOG = LoggerFactory.getLogger(UserAllocator.class);
	
	private static final Random RAND = new Random(0);
	
	@Override
	public String getName() {
		return "User";
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		List<IOdysseusNode> nodes = Lists.newArrayList(knownRemoteNodes);
		nodes.add(localNode);
		return userAllocationImpl(queryParts, localNode, nodes);
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> reallocate(Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultyNodes, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		List<IOdysseusNode> nodes = Lists.newArrayList(knownRemoteNodes);
		nodes.add(localNode);

		for (IOdysseusNode node: nodes.toArray(new IOdysseusNode[0])) {
			if (faultyNodes.contains(node)) {
				nodes.remove(node);
			}
		}

		return userAllocationImpl(previousAllocationMap.keySet(), localNode, nodes);
	}

	private static Map<ILogicalQueryPart, IOdysseusNode> userAllocationImpl(Collection<ILogicalQueryPart> queryParts, IOdysseusNode localNode, List<IOdysseusNode> nodes) {
		LOG.debug("Beginning user-based allocation with {} remote nodes", nodes.size());
		
		Map<ILogicalQueryPart, IOdysseusNode> allocationMap = Maps.newHashMap();
		Map<String, IOdysseusNode> nodeNameMap = createNodeNameMap(nodes);
		nodeNameMap.put(localNode.getName(), localNode);
		nodeNameMap.put("local", localNode);
		
		for (ILogicalQueryPart part : queryParts) {
			String destinationName = determineMostMentionedDestinationName(part);
			LOG.debug("QueryPart {} has most mentioned destination {}", part, destinationName);

			if (nodeNameMap.containsKey(destinationName)) {
				IOdysseusNode node = nodeNameMap.get(destinationName);
				allocationMap.put(part, node);
				LOG.debug("Assigning query part to node {}", node);
				
			} else {
				IOdysseusNode node = chooseRandomNode(nodeNameMap.values(), localNode);
				LOG.debug("Assigning query part randomly to node {}", node);
				nodeNameMap.put(destinationName, node);
				LOG.debug("Destination {} is assigned to node {}", destinationName, node);

				allocationMap.put(part, node);
			}
		}

		LOG.debug("User-based allocation finished");
		return allocationMap;
	}

	private static Map<String, IOdysseusNode> createNodeNameMap(List<IOdysseusNode> nodes) {
		Map<String, IOdysseusNode> result = Maps.newHashMap();
		
		for( IOdysseusNode node : nodes ) {
			result.put(node.getName(), node);
		}
		
		return result;
	}

	private static String determineMostMentionedDestinationName(ILogicalQueryPart part) {
		Collection<ILogicalOperator> operators = part.getOperators();

		Map<String, Integer> countMap = Maps.newHashMap();
		for (ILogicalOperator operator : operators) {
			String destinationOfOperator = operator.getDestinationName();

			if (!Strings.isNullOrEmpty(destinationOfOperator)) {
				if (countMap.containsKey(destinationOfOperator)) {
					countMap.put(destinationOfOperator, countMap.get(destinationOfOperator) + 1);
				} else {
					countMap.put(destinationOfOperator, 1);
				}
			}
		}

		if (countMap.isEmpty()) {
			return "local";
		}

		String mostName = "local";
		int mostCount = Integer.MIN_VALUE;

		for (String destinationName : countMap.keySet()) {
			int count = countMap.get(destinationName);
			if (count > mostCount) {
				mostName = destinationName;
				mostCount = count;
			}
		}

		return mostName;
	}

	private static IOdysseusNode chooseRandomNode(Collection<IOdysseusNode> values, IOdysseusNode localNode) {
		IOdysseusNode current = null;

		do {
			int choice = RAND.nextInt(values.size());
			Iterator<IOdysseusNode> nodeIterator = values.iterator();
			for (int i = 0; i < choice + 1; i++) {
				current = nodeIterator.next();
			}
		} while (current == null || current.equals(localNode));

		return current;
	}
}
