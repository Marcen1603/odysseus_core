package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * A {@link HashFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link HashFragmentAO} has only one parameter for the number of fragments and it must have exact one input.
 * It can be used in PQL: <br />
 * <code>output = HASHFRAGMENT([FRAGMENTS=n], input)</code>
 * @author Michael Brand
 */
@LogicalOperator(name = "HASHFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc="Can be used to fragment incoming streams",category={LogicalOperatorCategory.PROCESSING})
public class HashFragmentAO extends AbstractFragmentAO {
	
	private static final long serialVersionUID = -6789007084291408905L;
	
	/**
	 * Constructs a new {@link HashFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public HashFragmentAO() {
		
		super();
		
	}

	/**
	 * Constructs a new {@link HashFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link HashFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public HashFragmentAO(HashFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new HashFragmentAO(this);
		
	}

}