package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.MinMax;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMinMax extends MinMax<RelationalTuple<?>> {

	int[] attrList = new int[1];

	static Map<Boolean, Map<Integer, RelationalMinMax>> instances = new HashMap<Boolean, Map<Integer,RelationalMinMax>>();

	static public RelationalMinMax getInstance(int pos, boolean isMax){
		Map<Integer, RelationalMinMax> in = instances.get(isMax);
		RelationalMinMax ret;
		if (in==null){
			in = new HashMap<Integer, RelationalMinMax>();
			instances.put(isMax, in);
			ret = new RelationalMinMax(pos, isMax);
			in.put(pos, ret);
		}else{
			ret = in.get(pos);
			if (ret == null){
				ret = new RelationalMinMax(pos, isMax);
				in.put(pos,ret);
			}
		}
		return ret;
	}


	private RelationalMinMax(int pos, boolean isMax) {
		super(isMax);
		attrList[0] = pos;
	}
	
	@Override
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		return super.init(in.restrict(attrList, true));
	}
	
	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(
			IPartialAggregate<RelationalTuple<?>> p, RelationalTuple<?> toMerge,
			boolean createNew) {
		return super.merge(p, toMerge.restrict(attrList, true), createNew);
	}
	
}
