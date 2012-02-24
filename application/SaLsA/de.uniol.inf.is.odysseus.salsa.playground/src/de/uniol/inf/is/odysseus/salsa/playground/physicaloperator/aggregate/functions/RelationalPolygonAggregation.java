package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalPolygonAggregation<T, R> extends AbstractAggregateFunction<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -9112610790613631712L;
	private int[] restrictList;
	
	public RelationalPolygonAggregation(int[] restrictList) {
		super("L1Merge");
		this.restrictList = restrictList;
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<? extends IMetaAttribute>> elems = ((GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		
		RelationalTuple<? extends IMetaAttribute> ret = new RelationalTuple<IMetaAttribute>(1);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge, boolean createNew) {
//		GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>> list = (GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>) p;
//		if (createNew){
//			list = new GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>((GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p);
//		}
//		list.addElem(toMerge.restrict(restrictList, true));
//		return list;
//		
		return ((GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).addElem(toMerge.restrict(restrictList, true));
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> init(RelationalTuple<? extends IMetaAttribute> in) {
		return new GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(in);
	}


}
