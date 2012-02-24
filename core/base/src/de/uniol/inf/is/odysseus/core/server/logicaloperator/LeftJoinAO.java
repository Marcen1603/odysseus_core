package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "LEFTJOIN")
public class LeftJoinAO extends JoinAO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8336532431695971478L;

	public LeftJoinAO(){
		super();
	}
	
	public LeftJoinAO(IPredicate<?> joinPredicate) {
		super(joinPredicate);
	}

	public LeftJoinAO(LeftJoinAO joinPO) {
		super(joinPO);
	}
	
	public LeftJoinAO clone(){
		return new LeftJoinAO(this);
	}

}
