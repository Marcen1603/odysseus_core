package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalPolygonAggregation extends AbstractGeometryAggregation<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -9112610790613631712L;
	private int[] restrictList;

	public RelationalPolygonAggregation(int[] restrictList){
		super("PMERGE");
		this.restrictList = restrictList;
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<? extends IMetaAttribute>> elems = ((GeometryPartialAggregate)p).getElems();
		
		RelationalTuple<? extends IMetaAttribute> ret = new RelationalTuple<IMetaAttribute>(1);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		return ((GeometryPartialAggregate)p).addElem(toMerge.restrict(restrictList, true));
	}
}
