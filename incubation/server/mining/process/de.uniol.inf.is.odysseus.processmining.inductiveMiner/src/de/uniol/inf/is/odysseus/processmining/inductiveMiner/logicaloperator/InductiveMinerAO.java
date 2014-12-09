package de.uniol.inf.is.odysseus.processmining.inductiveMiner.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator( name = "IM", maxInputPorts = 1, minInputPorts = 0,
doc="Inductive Miner", category={LogicalOperatorCategory.MINING})
public class InductiveMinerAO extends UnaryLogicalOp {

	public InductiveMinerAO(){
		super();
	}
	
	public InductiveMinerAO(InductiveMinerAO inductiveMinerAO){
		super(inductiveMinerAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new InductiveMinerAO(this);
	}

}
