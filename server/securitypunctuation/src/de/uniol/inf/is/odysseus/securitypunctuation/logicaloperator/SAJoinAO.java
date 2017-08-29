package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "SAJOIN", doc = "Operator to combine two datastreams and their SPs based on the predicate", url = "-", category = { LogicalOperatorCategory.BASE })
public class SAJoinAO extends JoinAO{
	String tupleRangeAttribute;
	/**
	 * 
	 */
	private static final long serialVersionUID = -703945943446340861L;

	public SAJoinAO(){
		super();
	}
	
	public SAJoinAO(String tupleRangeAttribute){
		super();
		this.tupleRangeAttribute=tupleRangeAttribute;
	}
	
	public SAJoinAO(SAJoinAO saJoinAO){
		super(saJoinAO);
		this.tupleRangeAttribute=saJoinAO.getTupleRangeAttribute();
	}
	
	@Override
	public JoinAO clone() {
		return new SAJoinAO(this);
	}
	
	public String getTupleRangeAttribute(){
		return this.tupleRangeAttribute;
	}

}
