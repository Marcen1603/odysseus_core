package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "HMM", minInputPorts = 1, maxInputPorts = 1)
public class HmmAO extends UnaryLogicalOp implements ILogicalOperator{

	// The default constructor is required as instances of logical operators are
	// created by newInstance().
	public HmmAO() {
		super();
	}

	
	//Dieser Konstruktor wird aufgerufen, wenn clone() benutzt wird
	public HmmAO(HmmAO hmmAO) {
		super(hmmAO);
	}

	// Clone must call the copy constructor and the copy constructor must call
	// the super copy constructor!
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new HmmAO(this);
	}

	// Finally, the class needs setters and getters for the parameter it should
	// keep

}
