package de.uniol.inf.is.odysseus.energy.ase.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * 
 * @author Jan Sören Schwarz
 *
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="ASE-Operator", doc="Adaptive state estimation", category={LogicalOperatorCategory.TRANSFORM})
public class ASEAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2331471600303961620L;

	public ASEAO() {		
	}
	
	public ASEAO(ASEAO aseAO) {
		super(aseAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ASEAO(this);
	}

}
