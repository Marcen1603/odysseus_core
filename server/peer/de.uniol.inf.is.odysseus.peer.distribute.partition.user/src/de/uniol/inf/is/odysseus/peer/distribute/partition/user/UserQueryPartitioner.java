package de.uniol.inf.is.odysseus.peer.distribute.partition.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class UserQueryPartitioner implements IQueryPartitioner {

	private static final String LOCAL_DESTINATION_NAME = "local";

	@Override
	public String getName() {
		return "user";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		return determineQueryParts(operators);
	}

	private static Collection<ILogicalQueryPart> determineQueryParts(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		Map<String, Collection<ILogicalOperator>> revertedDestinationMap = revertMap(destinations);
		
		return generateQueryParts(revertedDestinationMap);
	}

	private static <K, V> Map<K, Collection<V>> revertMap(Map<V, K> map) {
		Map<K, Collection<V>> revertedMap = Maps.newHashMap();
		for( V values : map.keySet() ) {
			K key = map.get(values);
			
			Collection<V> coll = revertedMap.get(key);
			if( coll == null ) {
				coll = Lists.<V>newArrayList();
				revertedMap.put(key, coll);
			}
			coll.add(values);
		}
		return revertedMap;
	}

	private static Collection<ILogicalQueryPart> generateQueryParts(Map<String, Collection<ILogicalOperator>> revertedDestinationMap) {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();
		for( String destinationName : revertedDestinationMap.keySet() ) {
			parts.add( new LogicalQueryPart(revertedDestinationMap.get(destinationName)));
		}
		return parts;
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(Collection<ILogicalOperator> operators) {
		final Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (final ILogicalOperator operator : operators) {
			String destinationName = getDestinationName(operator);
			if( Strings.isNullOrEmpty(destinationName)) {
				throw new RuntimeException("Could not determine destination name of operator " + operator);
			}
			destinationNames.put(operator, destinationName);
		}

		return destinationNames;
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if (operator instanceof StreamAO) {
			return LOCAL_DESTINATION_NAME;
		}

		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		}
		if (operator.getSubscribedToSource().size() > 0) {
			return getDestinationName(getOnePreviousOperator(operator));
		}

		return LOCAL_DESTINATION_NAME;
	}

	private static ILogicalOperator getOnePreviousOperator(ILogicalOperator operator) {
		return operator.getSubscribedToSource().iterator().next().getTarget();
	}
}
