package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.AvgSum;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings("unchecked")
public class RelationalAvgSum extends AvgSum<RelationalTuple<?>>{

	private int pos;

	static Map<Boolean, Map<Integer, RelationalAvgSum>> instances = new HashMap<Boolean, Map<Integer,RelationalAvgSum>>();

	static public RelationalAvgSum getInstance(int pos, boolean isAvg){
		Map<Integer, RelationalAvgSum> in = instances.get(isAvg);
		RelationalAvgSum ret;
		if (in==null){
			in = new HashMap<Integer, RelationalAvgSum>();
			instances.put(isAvg, in);
			ret = new RelationalAvgSum(pos, isAvg);
			in.put(pos, ret);
		}else{
			ret = in.get(pos);
			if (ret == null){
				ret = new RelationalAvgSum(pos, isAvg);
				in.put(pos,ret);
			}
		}
		return ret;
	}
	
	private RelationalAvgSum(int pos, boolean isAvg){
		super(isAvg);
		this.pos = pos;
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
		if (isAvg()){
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()/pa.getCount()));
		}else{
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
		}
		return r;
	}
	
}
