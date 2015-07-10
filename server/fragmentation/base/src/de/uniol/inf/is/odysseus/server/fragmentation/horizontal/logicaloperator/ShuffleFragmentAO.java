package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "SHUFFLEFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc="Can be used to fragment incoming streams",category={LogicalOperatorCategory.PROCESSING})
public class ShuffleFragmentAO extends AbstractStaticFragmentAO {

	private static final long serialVersionUID = 6061095387594392868L;

	
	public ShuffleFragmentAO() {
		super();
	}
	
	public ShuffleFragmentAO(ShuffleFragmentAO shuffleFragmentAO){
		super(shuffleFragmentAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ShuffleFragmentAO(this);
	}

}
