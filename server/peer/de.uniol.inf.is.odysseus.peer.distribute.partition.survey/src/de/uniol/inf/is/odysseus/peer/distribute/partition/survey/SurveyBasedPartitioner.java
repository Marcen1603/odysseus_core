package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PNetworkManagerService;

public class SurveyBasedPartitioner implements IQueryPartitioner {
	
	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedPartitioner.class);
	private static Partitioner partitioner;
	
	public void bindPartitioner( Partitioner part) {
		partitioner = part;
		
		LOG.debug("Bound partitioner {}", part);
	}
	
	public void unbindPartitioner( Partitioner part ) {
		if( partitioner == part ) {
			partitioner = null;
			
			LOG.debug("Unbound partitioner {}", part);
		}
	}
	
	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws QueryPartitionException {
		ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), true);
		
		// copy --> original
		Map<ILogicalOperator, ILogicalOperator> operatorCopyMap = createOperatorCopyMap(operators);
		try {
			List<SubPlan> subPlans = partitioner.partitionWithDummyOperators(operatorCopyMap.keySet().iterator().next(), sharedQueryID, config);
			List<ILogicalQueryPart> logicalQueries = transformToLogicalPlan(subPlans, operatorCopyMap );
			
			return logicalQueries;
		} catch (CouldNotPartitionException e) {
			throw new QueryPartitionException("Could not use partitioner", e);
		}
	}

	private static List<ILogicalQueryPart> transformToLogicalPlan(List<SubPlan> subPlans, Map<ILogicalOperator, ILogicalOperator> operatorCopyMap) {
		List<ILogicalQueryPart> logicalQueries = Lists.newArrayList();
		for( SubPlan subPlan : subPlans ) {
			List<ILogicalOperator> operatorsForLogicalQueryPart = Lists.newArrayList();
			
			for( ILogicalOperator operator : subPlan.getOperators() ) {
				operatorsForLogicalQueryPart.add(operatorCopyMap.get(operator));
			}
			
			logicalQueries.add(new LogicalQueryPart(operatorsForLogicalQueryPart));
		}
		return logicalQueries;
	}

	private static Map<ILogicalOperator, ILogicalOperator> createOperatorCopyMap(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, ILogicalOperator> copyMap = Maps.newHashMap();
		for( ILogicalOperator operator: operators ) {
			copyMap.put(operator.clone(), operator);
		}
		return copyMap;
	}
}
