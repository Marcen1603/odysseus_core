package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * A {@link RoundRobinFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link RoundRobinFragmentAO} has only one parameter for the number of fragments and it must have exact one input.
 * It can be used in PQL: <br />
 * <code>output = RRFRAGMENT([FRAGMENTS=n], input)</code>
 * @author Michael Brand
 */
@LogicalOperator(name = "RRFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc="Can be used to fragment incoming streams",category={LogicalOperatorCategory.PROCESSING})
public class RoundRobinFragmentAO extends AbstractStaticFragmentAO {
	
	private static final long serialVersionUID = -6789007084291408905L;
	
	/**
	 * Constructs a new {@link RoundRobinFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public RoundRobinFragmentAO() {
		
		super();
		
	}

	/**
	 * Constructs a new {@link RoundRobinFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link RoundRobinFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public RoundRobinFragmentAO(RoundRobinFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new RoundRobinFragmentAO(this);
		
	}

}