package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.MinMax;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMinMax extends MinMax<RelationalTuple> {

	int[] attrList = new int[1];

	public RelationalMinMax(int pos, boolean isMax) {
		super(isMax);
		attrList[0] = pos;
	}
	
	@Override
	public PartialAggregate<RelationalTuple> init(RelationalTuple in) {
		return super.init(in.restrict(attrList, null));
	}
	
	@Override
	public PartialAggregate<RelationalTuple> merge(
			PartialAggregate<RelationalTuple> p, RelationalTuple toMerge, boolean createNew) {
		return super.merge(p, toMerge.restrict(attrList,null), createNew);
	}
	
//	@Override
//	public RelationalTuple evaluate(PartialAggregate<RelationalTuple> p) {
//		return super.evaluate(p);
//	}


}
