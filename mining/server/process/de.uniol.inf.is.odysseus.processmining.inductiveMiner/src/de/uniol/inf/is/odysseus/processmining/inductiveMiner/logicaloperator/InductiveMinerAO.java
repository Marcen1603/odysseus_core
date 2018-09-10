package de.uniol.inf.is.odysseus.processmining.inductiveMiner.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.GeneratingStrategy;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.models.Invarianttype;

@LogicalOperator( name = "InductiveMiner", maxInputPorts = 1, minInputPorts = 0,
doc="Inductive Miner", category={LogicalOperatorCategory.MINING})
public class InductiveMinerAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5812923839972330744L;

	Invarianttype invariantType = Invarianttype.NONE;
	GeneratingStrategy generatingStrategy;
	public InductiveMinerAO(){
		super();
	}
	
	public InductiveMinerAO(InductiveMinerAO inductiveMinerAO){
		super(inductiveMinerAO);
		this.invariantType = inductiveMinerAO.getInvariantType();
		this.generatingStrategy = inductiveMinerAO.getGeneratingStrategy();
	}
	
	
	@Parameter(type = EnumParameter.class, name = "generatingtype", optional = false)
	public void setGeneratingStrategy(GeneratingStrategy generatingStrategy) {
		this.generatingStrategy = generatingStrategy;
	}
	
	public GeneratingStrategy getGeneratingStrategy() {
		return generatingStrategy;
	}

	@Parameter(type = EnumParameter.class, name = "invarianttype", optional = true)
	public void setInvariantType(Invarianttype invariantType) {
		this.invariantType = invariantType;
	}

	public Invarianttype getInvariantType() {
		return invariantType;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new InductiveMinerAO(this);
	}

}
