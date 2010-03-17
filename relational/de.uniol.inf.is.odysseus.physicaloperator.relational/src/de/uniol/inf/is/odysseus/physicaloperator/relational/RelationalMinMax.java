package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.MinMax;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMinMax extends MinMax<RelationalTuple<?>> {

	int[] attrList = new int[1];

	public RelationalMinMax(int pos, boolean isMax) {
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
