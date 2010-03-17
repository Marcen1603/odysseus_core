package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings("unchecked")
public class RelationalAvgSum implements IAggregateFunction<RelationalTuple<?>>{

	private int pos;
	boolean isAvg;
	
	public RelationalAvgSum(int pos, boolean isAvg){
		this.pos = pos;
		this.isAvg = isAvg;
	}
	
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple in) {
		AvgSumPartialAggregate<RelationalTuple<?>> pa = 
			new AvgSumPartialAggregate<RelationalTuple<?>>(((Number)in.getAttribute(pos)).doubleValue(),1);
		return pa;
	}

	public IPartialAggregate<RelationalTuple<?>> merge(IPartialAggregate p, RelationalTuple toMerge, boolean createNew) {
		AvgSumPartialAggregate<RelationalTuple> pa = null;
		if (createNew){
			AvgSumPartialAggregate<RelationalTuple> h = (AvgSumPartialAggregate<RelationalTuple>) p;			
			pa = new AvgSumPartialAggregate<RelationalTuple>(h.getAggValue(), h.getCount());
			
		}else{
			pa = (AvgSumPartialAggregate<RelationalTuple>) p;
		}
		return merge(pa, toMerge);
	}
	
	public IPartialAggregate<RelationalTuple<?>> merge(IPartialAggregate p, RelationalTuple toMerge) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		Double newAggValue = pa.getAggValue().doubleValue() + ((Number)toMerge.getAttribute(pos)).doubleValue(); 
		pa.setAggValue(newAggValue, pa.getCount()+1);
		return pa;
	}
	
	public RelationalTuple evaluate(IPartialAggregate p) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		RelationalTuple r = new RelationalTuple(1);
		if (isAvg){
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()/pa.getCount()));
		}else{
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
		}
		return r;
	}
}
