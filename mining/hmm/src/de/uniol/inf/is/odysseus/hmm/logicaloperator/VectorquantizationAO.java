package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "VECTORQUANTIZATION", minInputPorts = 1, maxInputPorts = 1)
public class VectorquantizationAO extends UnaryLogicalOp implements ILogicalOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1666674260694172414L;

	// The default constructor is required as instances of logical operators are
	// created by newInstance().
	public VectorquantizationAO() {
		super();
	}

	
	//Dieser Konstruktor wird aufgerufen, wenn clone() benutzt wird
	public VectorquantizationAO(VectorquantizationAO vqAO) {
		super(vqAO);
	}

	// Clone must call the copy constructor and the copy constructor must call
	// the super copy constructor!
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new VectorquantizationAO(this);
	}

	// Finally, the class needs setters and getters for the parameter it should
	// keep

}
