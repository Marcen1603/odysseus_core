package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "EM")
public class EMAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4183569304131228484L;

	public EMAO() {
		super();
	}

	public EMAO(EMAO emAO) {
		super(emAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new EMAO(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
