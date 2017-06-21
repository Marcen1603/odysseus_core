package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "SAAGGREGATE", doc = "Operator to combine two datastreams and their SPs based on the predicate", url = "-", category = {
		LogicalOperatorCategory.BASE })
public class SAAggregationAO extends AggregationAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1088689431181099569L;

	public SAAggregationAO() {
		super();
	}
	public SAAggregationAO(SAAggregationAO saAggregationAO){
		super(saAggregationAO);
	}
	
	public SAAggregationAO(AggregationAO aggregationAO){
		super(aggregationAO);
	}
	
	public SAAggregationAO clone(){
		return new SAAggregationAO(this);
	}
}
