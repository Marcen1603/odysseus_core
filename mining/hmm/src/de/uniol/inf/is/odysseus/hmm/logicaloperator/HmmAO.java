package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "HMM", minInputPorts = 1, maxInputPorts = 1)
public class HmmAO extends UnaryLogicalOp implements ILogicalOperator{

	private String mode;
	
	
	// The default constructor is required as instances of logical operators are
	// created by newInstance().
	public HmmAO() {
		super();
	}

	
	//Dieser Konstruktor wird aufgerufen, wenn clone() benutzt wird
	public HmmAO(HmmAO hmmAO) {
		super(hmmAO);
		this.mode = hmmAO.mode;
	}

	// Clone must call the copy constructor and the copy constructor must call
	// the super copy constructor!
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new HmmAO(this);
	}

	@Parameter(name = "mode", type = StringParameter.class)
	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getMode() {
		return mode;
	}
	
	// Finally, the class needs setters and getters for the parameter it should
	// keep

}
