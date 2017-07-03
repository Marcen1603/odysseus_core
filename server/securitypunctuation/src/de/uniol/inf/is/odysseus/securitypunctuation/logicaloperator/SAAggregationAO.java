package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "SAAGGREGATION", doc = "Operator to combine two datastreams and their SPs based on the predicate", url = "-", category = {
		LogicalOperatorCategory.BASE })

public class SAAggregationAO extends AggregationAO {
	String tupleRangeAttribute;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1088689431181099569L;
	public SAAggregationAO() {
		super();
	}
	public SAAggregationAO(String tupleRangeAttribute) {
		super();
		this.tupleRangeAttribute=tupleRangeAttribute;
	}
	public SAAggregationAO(SAAggregationAO saAggregationAO){
		super(saAggregationAO);
		this.tupleRangeAttribute=saAggregationAO.getTupleRangeAttribute();
	}
	
	public SAAggregationAO(AggregationAO aggregationAO,String tupleRangeAttribute){
		super(aggregationAO);
		this.tupleRangeAttribute=tupleRangeAttribute;
	}
	
	public SAAggregationAO clone(){
		return new SAAggregationAO(this);
	}
	
	public String getTupleRangeAttribute(){
		return this.tupleRangeAttribute;
	}
}
