package de.uniol.inf.is.odysseus.net.querydistribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.querydistribute.DistributionCheckException;
import de.uniol.inf.is.odysseus.net.querydistribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPostProcessorException;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryDistributionPreProcessorException;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.net.querydistribute.impl.QueryDistributionComponent;
import de.uniol.inf.is.odysseus.net.querydistribute.parameter.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.net.querydistribute.service.DistributionCheckerRegistry;

public final class QueryDistributorHelper {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributorHelper.class);

	private static IOdysseusNodeCommunicator communicator;

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		communicator = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (communicator == serv) {
			communicator = null;
		}
	}

	public static void preProcess(IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors, ILogicalQuery query) throws QueryDistributionPreProcessorException {
		for (InterfaceParametersPair<IQueryDistributionPreProcessor> preProcessor : preProcessors) {
			preProcessor.getInterface().preProcess(serverExecutor, caller, query, config);
		}
	}

	public static Collection<ILogicalOperator> prepareLogicalQuery(ILogicalQuery query) {
		Collection<ILogicalOperator> operators = query.getLogicalPlan().getOperators();
		LogicalQueryHelper.removeTopAOs(operators);
		return operators;
	}

	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for (ILogicalQueryPart part : queryParts) {
			for (ILogicalOperator operator : part.getOperators()) {
				map.put(operator, part);
			}
		}
		return map;
	}

	public static void postProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors, ILogicalQuery query) throws QueryDistributionPostProcessorException {
		for (InterfaceParametersPair<IQueryDistributionPostProcessor> postProcessor : postProcessors) {
			postProcessor.getInterface().postProcess(serverExecutor, caller, allocationMap, query, config, postProcessor.getParameters());
		}
	}

	public static void checkDistribution(QueryBuildConfiguration config, ILogicalQuery query, Collection<ILogicalOperator> operators) throws DistributionCheckException {
		checkDistribution(operators, query, config);
	}

	private static void checkDistribution(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config) throws DistributionCheckException {
		ImmutableCollection<String> names = DistributionCheckerRegistry.getInstance().getNames();
		if (names.isEmpty()) {
			LOG.debug("No distribution checkers registered. No checks taken.");
			return;
		}

		LOG.debug("Begin distribution checks with {} checkers.", names.size());
		for (String name : names) {
			IDistributionChecker checker = DistributionCheckerRegistry.getInstance().get(name);
			LOG.debug("Checking with {}", checker.getName());

			checker.check(operators, query, config);
		}
		LOG.debug("All checks passed");
	}

	public static Collection<ILogicalQueryPart> partitionQuery(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartitioner>> partitioners, Collection<ILogicalOperator> operators, ILogicalQuery query) throws QueryPartitionException {
		LOG.debug("Begin partitioning");

		InterfaceParametersPair<IQueryPartitioner> firstPartitioner = partitioners.get(0);
		Collection<ILogicalQueryPart> partitions = firstPartitioner.getInterface().partition(operators, query, config, firstPartitioner.getParameters());

		if (partitions == null || partitions.isEmpty()) {
			throw new QueryPartitionException("Query partitioner '" + firstPartitioner.getInterface().getName() + "' retured null or empty query part list!");
		}
		checkPartitions(partitions, operators);
		if (LOG.isDebugEnabled()) {
			LoggingHelper.printQueryParts(partitions);
		}

		return partitions;
	}

	private static void checkPartitions(Collection<ILogicalQueryPart> queryParts, Collection<ILogicalOperator> operators) throws QueryPartitionException {
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(queryParts);

		for (ILogicalOperator operator : operators) {
			if (!queryPartAssignment.containsKey(operator)) {
				throw new QueryPartitionException("Operator " + operator + " was not partitioned to a logical query part");
			}
		}
	}

	public static Collection<ILogicalQueryPart> modifyQueryParts(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartModificator>> modificators, Collection<ILogicalQueryPart> queryParts, ILogicalQuery query) throws QueryPartModificationException {
		if (modificators.isEmpty()) {
			LOG.debug("No modificator set");
			return queryParts;
		}

		LOG.debug("Begin modifying query parts");

		Collection<ILogicalQueryPart> currentPartitionState = queryParts;
		for (InterfaceParametersPair<IQueryPartModificator> modificator : modificators) {
			currentPartitionState = modificator.getInterface().modify(currentPartitionState, query, config, modificator.getParameters());

			if (currentPartitionState == null || currentPartitionState.isEmpty()) {
				throw new QueryPartModificationException("Query part modificator '" + modificator.getInterface().getName() + "' retured null or empty query part list!");
			}
		}

		if (LOG.isDebugEnabled()) {
			LoggingHelper.printQueryParts(currentPartitionState);
		}

		return currentPartitionState;
	}

	public static Map<ILogicalQueryPart, IOdysseusNode> allocateQueryParts(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Collection<ILogicalQueryPart> modifiedQueryParts, ILogicalQuery query) throws QueryPartAllocationException {
		LOG.debug("Begin allocation of query parts");

		InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
		IOdysseusNode localNode = determineLocalNode();
		Map<ILogicalQueryPart, IOdysseusNode> allocationMap = firstAllocator.getInterface().allocate(modifiedQueryParts, query, communicator.getDestinationNodes(), localNode, config, firstAllocator.getParameters());

		if (allocationMap == null || allocationMap.isEmpty()) {
			throw new QueryPartAllocationException("Query part allocation map from allocator '" + firstAllocator.getInterface().getName() + "' is null or empty!");
		}

		if (LOG.isDebugEnabled()) {
			LoggingHelper.printAllocationMap(allocationMap);
		}

		return allocationMap;
	}

	private static IOdysseusNode determineLocalNode() throws QueryPartAllocationException {
		try {
			return QueryDistributionComponent.getLocalNode();
		} catch (OdysseusNetException e) {
			throw new QueryPartAllocationException("Could not allocate query parts", e);
		}
	}

	public static Map<ILogicalQueryPart, IOdysseusNode> tryReallocate(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultyNodes) throws QueryPartAllocationException {
		LOG.debug("Begin reallocation of query parts");

		IOdysseusNode localNode = determineLocalNode();
		InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
		Map<ILogicalQueryPart, IOdysseusNode> allocationMap = firstAllocator.getInterface().reallocate(previousAllocationMap, faultyNodes, communicator.getDestinationNodes(), localNode, config, firstAllocator.getParameters());

		if (allocationMap == null || allocationMap.isEmpty()) {
			throw new QueryPartAllocationException("Query part allocation map from allocator '" + firstAllocator.getInterface().getName() + "' is null or empty!");
		}

		if (LOG.isDebugEnabled()) {
			LoggingHelper.printAllocationMap(allocationMap);
		}

		return allocationMap;
	}

	public static String toNodeNames(List<IOdysseusNode> nodes) {
		StringBuilder sb = new StringBuilder();
		for (IOdysseusNode node : nodes) {
			sb.append(node.getName()).append(" ");
		}
		return sb.toString();
	}
}
