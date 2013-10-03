package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IntersectionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.p2p_new.QueryPart;

/**
 * The <code>OperatorSetCloudLoadBalancer</code> splits an {@link ILogicalQuery} before each {@link IStatefulOperator} 
 * and parallelizes each {@link QueryPart} on different peers. <br />
 * The copies of an {@link QueryPart} are assigned to a peer via round robin without the local peer. 
 * But the local peer will get all results and returns them to the {@link IExecutor}. <br />
 * To use the <code>OperatorSetCloudLoadBalancer</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE operatorsetcloud <code>degree of parallelism</code> <br />
 * where <code>degree of parallelism</code> has to be an integer greater than zero, "min" for <code>1</code> or 
 * "max" for {@link Integer#MAX_VALUE}.
 * @author Michael Brand
 */
public class OperatorSetCloudLoadBalancer extends AbstractLoadBalancer {
	
	/**
	 * The list of all {@link ILogicalOperator} classes which are defined as "stateful" for the <code>OperatorSetCloudLoadBalancer</cloud>.
	 */
	@SuppressWarnings("rawtypes")
	public static final Class[] LOGICAL_OPERATORS_DEFINED_AS_STATEFUL = {
		
		JoinAO.class, AggregateAO.class, UnionAO.class, IntersectionAO.class
		
	};

	@Override
	public String getName() {
		
		return "operatorsetcloud";
		
	}
	
	/**
	 * Returns one {@link Querypart} for each {@link IStatefulOperator} within the {@link ILogicalQuery} plus one {@link QueryPart} with 
	 * all operators before the first {@link QueryPart}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, 
			Optional<QueryPart> dataReunionPart) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must be not empty!");
		
		List<QueryPart> parts = Lists.newArrayList();
		List<ILogicalOperator> opsForCurrentPart = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(this.isSink(operator, dataReunionPart) || this.isSourceOfStatefulOperator(operator)) {
				
				// operator marks the beginning of a new querypart
				if(!opsForCurrentPart.isEmpty()) {
					
					parts.add(new QueryPart(opsForCurrentPart));
					opsForCurrentPart.clear();
					
				}
				
				opsForCurrentPart.add(operator);
				
			} else if(Arrays.asList(OperatorSetCloudLoadBalancer.LOGICAL_OPERATORS_DEFINED_AS_STATEFUL).contains(operator.getClass())) {
				
				// operator marks the end of a new querypart
				opsForCurrentPart.add(operator);
				parts.add(new QueryPart(opsForCurrentPart));
				opsForCurrentPart.clear();
				
			} else opsForCurrentPart.add(operator);
			
		}
		
		// the end of the last querypart
		if(!opsForCurrentPart.isEmpty()) {
			
			parts.add(new QueryPart(opsForCurrentPart));
			opsForCurrentPart.clear();
			
		}
		
		return parts;
		
	}
	
	/**
	 * Determines if an {@link ILogicalOperator} is a sink of a distributed {@link ILogicalQuery}.
	 * @param operator The {@link ILogicalOperator}.
	 * @param localPart The local part of the distributed {@link ILogicalQuery}.
	 * @param dataReunionPart The query part of data reunion, if present.
	 * @return true, if <code>operator</code> has no subscriptions or if <code>operator</code> is only subscribed to <code>localPart</code>; <br />
	 * false, else
	 */
	private boolean isSink(ILogicalOperator operator, Optional<QueryPart> dataReunionPart) {
		
		Preconditions.checkNotNull(operator, "operator must be not null!");
		
		if(operator.getSubscriptions().size() == 0)
			return true;
		
		for(LogicalSubscription subToSink : operator.getSubscriptions()) {
			
			if(!dataReunionPart.isPresent() || 
					!dataReunionPart.get().getOperators().contains(subToSink.getTarget())) {
				
				// Operator is subscribed by an operator which is not part of the reunion part
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
	/**
	 * Determines if an {@link ILogicalOperator} is a source of a stateful {@link ILogicalOperator}.
	 * @param operator The {@link ILogicalOperator}.
	 * @return true, if any subscription of <code>operator</code> targets an {@link ILogicalOperator} within 
	 * {@value #LOGICAL_OPERATORS_DEFINED_AS_STATEFUL}.
	 */
	private boolean isSourceOfStatefulOperator(ILogicalOperator operator) {
		
		Preconditions.checkNotNull(operator, "operator must be not null!");
		
		for(LogicalSubscription subToSink : operator.getSubscriptions()) {
			
			if(Arrays.asList(OperatorSetCloudLoadBalancer.LOGICAL_OPERATORS_DEFINED_AS_STATEFUL).contains(subToSink.getTarget().getClass()))
					return true;
			
		}
		
		return false;
		
	}

}