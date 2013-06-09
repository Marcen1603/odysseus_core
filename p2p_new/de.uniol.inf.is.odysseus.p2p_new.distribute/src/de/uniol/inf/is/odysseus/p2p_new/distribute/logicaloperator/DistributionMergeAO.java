package de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * A {@link DistributionMergeAO} can be used to merge results from semantically equivalent 
 * {@link ILogicalQuery}s. <br />
 * The {@link DistributionMergeAO} does not have any parameters but it must have at least two inputs. 
 * It can be used in PQL: <br />
 * <code>output = DISTRIBUTION_MERGE(input1,input2,...inputn)</code> 
 * @author Michael Brand
 */
@LogicalOperator(name="DISTRIBUTION_MERGE", minInputPorts=2, maxInputPorts=Integer.MAX_VALUE)
public class DistributionMergeAO extends BinaryLogicalOp {
	
	/**
	 * Constructs a new {@link DistributionMergeAO}.
	 * @see BinaryLogicalOp#BinaryLogicalOp()
	 */
	public DistributionMergeAO() {
		
		super();
		
	}
	
	/**
	 * Constructs a new {@link DistributionMergeAO} as a copy of an existing one.
	 * @param mergeAO Die {@link DistributionMergeAO} to be copied.
	 * @see BinaryLogicalOp#BinaryLogicalOp(AbstractLogicalOperator)
	 */
	public DistributionMergeAO(DistributionMergeAO mergeAO) {
		
		super(mergeAO);
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new DistributionMergeAO(this);
		
	}

}