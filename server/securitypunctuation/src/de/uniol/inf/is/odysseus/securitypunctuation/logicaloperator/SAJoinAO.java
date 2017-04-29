package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "SAJOIN", doc = "Operator to combine two datastreams and their SPs based on the predicate", url = "-", category = { LogicalOperatorCategory.BASE })
public class SAJoinAO extends BinaryLogicalOp{

	/**
	 * 
	 */
	private static final long serialVersionUID = -703945943446340861L;

	public SAJoinAO(){
		super();
	}
	
	public SAJoinAO(SAJoinAO saJoin){
		super(saJoin);
	}
	
	
	
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
