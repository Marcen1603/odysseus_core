package de.uniol.inf.is.odysseus.peer.distribute.partition.user;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class UserQueryPartitioner implements IQueryPartitioner {

	private static final Logger LOG = LoggerFactory.getLogger(UserQueryPartitioner.class);
	private static final String LOCAL_DESTINATION_NAME = "local";

	@Override
	public String getName() {
		return "user";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		return determineQueryParts(operators);
	}

	private static Collection<ILogicalQueryPart> determineQueryParts(Collection<ILogicalOperator> operators) throws QueryPartitionException {
		Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		Map<String, Collection<ILogicalOperator>> revertedDestinationMap = revertMap(destinations);

		return generateQueryParts(revertedDestinationMap);
	}

	private static <K, V> Map<K, Collection<V>> revertMap(Map<V, K> map) {
		Map<K, Collection<V>> revertedMap = Maps.newHashMap();
		for (V values : map.keySet()) {
			K key = map.get(values);

			Collection<V> coll = revertedMap.get(key);
			if (coll == null) {
				coll = Lists.<V> newArrayList();
				revertedMap.put(key, coll);
			}
			coll.add(values);
		}
		return revertedMap;
	}

	private static Collection<ILogicalQueryPart> generateQueryParts(Map<String, Collection<ILogicalOperator>> revertedDestinationMap) {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();
		for (String destinationName : revertedDestinationMap.keySet()) {
			LogicalQueryPart queryPart = new LogicalQueryPart(revertedDestinationMap.get(destinationName));
			parts.add(queryPart);
			LOG.debug("Created destination name {} to query part {}", destinationName, queryPart);
		}
		return parts;
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(Collection<ILogicalOperator> operators) throws QueryPartitionException {
		Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		Collection<ILogicalOperator> unsetOperators = Lists.newArrayList(operators);
		LOG.debug("Determining destionation names");
		boolean changed = true;
		while( changed ) {
			changed = false;
			
			Iterator<ILogicalOperator> it = unsetOperators.iterator();
			while( it.hasNext() ) {
				ILogicalOperator unsetOperator = it.next();
				Optional<String> optDestinationName = getDestinationNameDown(unsetOperator);
				if (!optDestinationName.isPresent()) {
					optDestinationName = getDestinationNameUp(unsetOperator);
				}
				
				if (optDestinationName.isPresent()) {
					destinationNames.put(unsetOperator, optDestinationName.get());
					LOG.debug("\t{} --> {}", unsetOperator.getName(), optDestinationName.get());
					changed = true;
					it.remove();
				} 
			}
		}
		
		if( !unsetOperators.isEmpty() ) {
			throw new QueryPartitionException("Could not determine destionation name for operators " + unsetOperators);
		}

		return destinationNames;
	}

	private static Optional<String> getDestinationNameDown(ILogicalOperator operator) {
		if (operator instanceof StreamAO) {
			return Optional.of(LOCAL_DESTINATION_NAME);
		}

		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return Optional.of(operator.getDestinationName());
		}
		if (operator.getSubscribedToSource().size() > 0) {
			for(LogicalSubscription sub :  operator.getSubscribedToSource() ) {
				Optional<String> optName = getDestinationNameDown(sub.getTarget());
				if( optName.isPresent() ) {
					return optName;
				}
			}
		}

		return Optional.absent();
	}

	private static Optional<String> getDestinationNameUp(ILogicalOperator operator) {
		if (operator instanceof StreamAO) {
			return Optional.of(LOCAL_DESTINATION_NAME);
		}

		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return Optional.of(operator.getDestinationName());
		}
		
		if (operator.getSubscriptions().size() > 0) {
			for(LogicalSubscription sub :  operator.getSubscriptions() ) {
				Optional<String> optName = getDestinationNameUp(sub.getTarget());
				if( optName.isPresent() ) {
					return optName;
				}
			}
		}

		return Optional.absent();
	}
}
