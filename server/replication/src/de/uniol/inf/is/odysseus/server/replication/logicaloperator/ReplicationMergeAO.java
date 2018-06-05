package de.uniol.inf.is.odysseus.server.replication.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * A {@link ReplicationMergeAO} can be used to merge results from semantically equivalent 
 * {@link ILogicalQuery}s. <br />
 * The {@link ReplicationMergeAO} does not have any parameters but it must have at least two inputs. 
 * It can be used in PQL: <br />
 * <code>output = REPLICATIONMERGE(input1,input2,...inputn)</code> 
 * @author Michael Brand
 */
@LogicalOperator(name="REPLICATIONMERGE", minInputPorts=2, maxInputPorts=Integer.MAX_VALUE, doc="Merge input from semantically equal queries.", category = {LogicalOperatorCategory.PROCESSING})
public class ReplicationMergeAO extends BinaryLogicalOp {
	
	private static final long serialVersionUID = -4050688632559595499L;

	/**
	 * Constructs a new {@link ReplicationMergeAO}.
	 * @see BinaryLogicalOp#BinaryLogicalOp()
	 */
	public ReplicationMergeAO() {
		
		super();
		
	}
	
	/**
	 * Constructs a new {@link ReplicationMergeAO} as a copy of an existing one.
	 * @param mergeAO The {@link ReplicationMergeAO} to be copied.
	 * @see BinaryLogicalOp#BinaryLogicalOp(AbstractLogicalOperator)
	 */
	public ReplicationMergeAO(ReplicationMergeAO mergeAO) {
		
		super(mergeAO);
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new ReplicationMergeAO(this);
		
	}

}