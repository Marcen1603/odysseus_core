package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

/**
 * An standard ADT-class, which provides the result of a fragmentation.
 * @see IDataFragmentation
 * @author Michael Brand
 */
public class StandardFragmentPlan implements IFragmentPlan {
	
	/**
	 * A mapping of all origin operators to their query. <br />
	 * This is the status before fragmentation.
	 */
	private final Map<ILogicalQuery, List<ILogicalOperator>> operatorsPerLogicalPlanBeforeFragmentation;
	
	/**
	 * A mapping of all operators to their query. <br />
	 * This is the status after fragmentation.
	 */
	private Map<ILogicalQuery, List<ILogicalOperator>> operatorsPerLogicalPlanAfterFragmentation;
	
	/**
	 * A list of operators which are allocated to a part called fragmentation part. <br />
	 * This part and therefore the return value contains the {@link StreamAO}s for the sources fragmented 
	 * and the inserted operators for fragmentation. It may also contain {@link WindowAO}s, if they 
	 * were related to that source.
	 */
	private List<ILogicalOperator> operatorsOfFragmentationPart;
	
	/**
	 * A list of operators which are allocated to a part called data reunion part. <br />
	 * This part and therefore the return value contains the inserted operators for data reunion. 
	 * It may also contain other operators, if they were moved or inserted after that operators.
	 */
	private List<ILogicalOperator> operatorsOfReunionPart;
	
	/**
	 * Constructs a new {@link StandardFragmentPlan}.
	 * @param operatorsPerLogicalPlanBeforeFragmentation A mapping of all origin operators to their query. 
	 */
	public StandardFragmentPlan(
			Map<ILogicalQuery, List<ILogicalOperator>> operatorsPerLogicalPlanBeforeFragmentation) {
		
		Preconditions.checkNotNull(operatorsPerLogicalPlanBeforeFragmentation);
		
		this.operatorsPerLogicalPlanBeforeFragmentation = operatorsPerLogicalPlanBeforeFragmentation;
		this.operatorsPerLogicalPlanAfterFragmentation = Maps.newHashMap(operatorsPerLogicalPlanBeforeFragmentation);
		this.operatorsOfFragmentationPart = Lists.newArrayList();
		this.operatorsOfReunionPart = Lists.newArrayList();
		
	}
	
	/**
	 * Constructs a new {@link StandardFragmentPlan} as a copy of an existing one.
	 * @param fragmentPlan The {@link StandardFragmentPlan} to be copied.
	 */
	public StandardFragmentPlan(StandardFragmentPlan fragmentPlan) {
		
		this(fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation());
		this.operatorsPerLogicalPlanAfterFragmentation = 
				fragmentPlan.operatorsPerLogicalPlanAfterFragmentation;
		this.operatorsOfFragmentationPart = fragmentPlan.operatorsOfFragmentationPart;
		this.operatorsOfReunionPart = fragmentPlan.operatorsOfReunionPart;
		
	}

	@Override
	public Map<ILogicalQuery, List<ILogicalOperator>> getOperatorsPerLogicalPlanBeforeFragmentation() {
		
		return this.operatorsPerLogicalPlanBeforeFragmentation;
		
	}

	@Override
	public Map<ILogicalQuery, List<ILogicalOperator>> getOperatorsPerLogicalPlanAfterFragmentation() {
		
		return this.operatorsPerLogicalPlanAfterFragmentation;
		
	}

	@Override
	public ILogicalOperator getCompleteLogicalPlan() {
		
		for(ILogicalOperator operator : this.operatorsOfReunionPart) {
			
			if(operator.getSubscriptions().isEmpty())
				return operator;
			
		}
		
		return null;
		
	}

	@Override
	public List<ILogicalOperator> getOperatorsOfFragmentationPart() {
		
		return this.operatorsOfFragmentationPart;
		
	}

	@Override
	public List<ILogicalOperator> getOperatorsOfReunionPart() {
		
		return this.operatorsOfReunionPart;
		
	}
	
	@Override
	public IFragmentPlan clone() {
		
		return new StandardFragmentPlan(this);
		
	}

}