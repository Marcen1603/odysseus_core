package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

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

}
