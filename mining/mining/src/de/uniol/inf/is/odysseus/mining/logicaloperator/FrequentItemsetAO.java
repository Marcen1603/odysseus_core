package de.uniol.inf.is.odysseus.mining.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;


@LogicalOperator(name = "FREQUENTITEMSET", minInputPorts = 1, maxInputPorts = 1)
public class FrequentItemsetAO extends AbstractLogicalOperator{

	
	private static final long serialVersionUID = 7771591123865284928L;
	private int numberOftransactions = 100;
	

	public FrequentItemsetAO() {
		
	}
	
	public FrequentItemsetAO(FrequentItemsetAO frequentItemsetAO) {
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentItemsetAO(this);
	}
	
	
	@Parameter(name="transactions", optional=true, type=IntegerParameter.class)
	public void setNumberOfTransactions(int i){
		this.numberOftransactions = i;
	}
	
	public int getNumberOfTransactions(){
		return this.numberOftransactions;
	}

}
