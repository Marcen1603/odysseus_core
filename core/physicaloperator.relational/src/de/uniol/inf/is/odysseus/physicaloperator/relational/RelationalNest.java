package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Nest;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalNest extends Nest<RelationalTuple<? extends IMetaAttribute>> {

	private int[] restrictList;

	public RelationalNest(int[] restrictList){
		this.restrictList = restrictList;
	}
	
	@Override
	public RelationalTuple evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<?>> elems = ((ListPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		RelationalTuple ret = new RelationalTuple<IMetaAttribute>(0);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		return ((ListPartialAggregate)p).addElem(toMerge.restrict(restrictList, true));
	}
	
}
