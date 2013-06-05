package de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

// TODO javaDoc M.B.
// TODO setPredicates needed? M.B.
@LogicalOperator(name="FANIN", minInputPorts=2, maxInputPorts=Integer.MAX_VALUE)
public class DistributionMergeAO extends BinaryLogicalOp {
	
	public DistributionMergeAO() {
		
		super();
		
	}
	
	public DistributionMergeAO(DistributionMergeAO mergeAO) {
		
		super(mergeAO);
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new DistributionMergeAO(this);
		
	}

}