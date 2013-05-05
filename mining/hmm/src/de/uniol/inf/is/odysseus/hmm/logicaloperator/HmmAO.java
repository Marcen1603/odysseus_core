package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "HMM", minInputPorts = 1, maxInputPorts = 1)
public class HmmAO extends UnaryLogicalOp implements ILogicalOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6352258419499082879L;
	private String mode;
	private String gesture;
	
	// The default constructor is required as instances of logical operators are
	// created by newInstance().
	public HmmAO() {
		super();
	}

	
	//Dieser Konstruktor wird aufgerufen, wenn clone() benutzt wird
	public HmmAO(HmmAO hmmAO) {
		super(hmmAO);
		this.mode = hmmAO.mode;
		this.gesture = hmmAO.gesture;
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

	@Parameter(name = "gesture", type = StringParameter.class, optional = true)
	public void setAlgorithmus(String gesture) {
		this.gesture = gesture;
	}
	
	public String getMode() {
		return mode;
	}
	
	public String getGesture() {
		return gesture;
	}
	
	// Finally, the class needs setters and getters for the parameter it should
	// keep

}
