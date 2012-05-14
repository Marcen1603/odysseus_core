package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class RelationalPolygonAggregation<T, R> extends AbstractAggregateFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -9112610790613631712L;
	private int[] restrictList;
	
	public RelationalPolygonAggregation(int[] restrictList) {
		super("L1Merge");
		this.restrictList = restrictList;
	}

	@Override
	public Tuple<? extends IMetaAttribute> evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		List<Tuple<? extends IMetaAttribute>> elems = ((GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>)p).getElems();
		
		Tuple<? extends IMetaAttribute> ret = new Tuple<IMetaAttribute>(1);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(IPartialAggregate<Tuple<? extends IMetaAttribute>> p, Tuple<? extends IMetaAttribute> toMerge, boolean createNew) {
//		GeometryPartialAggregate<Tuple<? extends IMetaAttribute>> list = (GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>) p;
//		if (createNew){
//			list = new GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>((GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>)p);
//		}
//		list.addElem(toMerge.restrict(restrictList, true));
//		return list;
//		
		return ((GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>)p).addElem(toMerge.restrict(restrictList, true));
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> init(Tuple<? extends IMetaAttribute> in) {
		return new GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>(in);
	}


}
