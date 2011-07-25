package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalPolygonAggregation
		extends
		AbstractGeometryAggregation<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	private int[] restrictList;

	public RelationalPolygonAggregation(int[] restrictList){
		super("PMERGE");
		this.restrictList = restrictList;
	}

	@Override
	public RelationalTuple evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<?>> elems = ((GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		RelationalTuple ret = new RelationalTuple<IMetaAttribute>(0);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		return ((GeometryPartialAggregate)p).addElem(toMerge.restrict(restrictList, true));
	}
}
