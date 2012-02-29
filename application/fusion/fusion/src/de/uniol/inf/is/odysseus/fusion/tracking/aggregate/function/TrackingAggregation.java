package de.uniol.inf.is.odysseus.fusion.tracking.aggregate.function;

import java.util.ArrayList;
import java.util.Map;


import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class TrackingAggregation<T, R> extends AbstractAggregateFunction<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = 1168495530599195228L;
	private int[] restrictList;
	
	public TrackingAggregation(int[] restrictList) {
		super("Tracker");
		this.restrictList = restrictList;
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		Map<Integer, RelationalTuple<? extends IMetaAttribute>> elems = ((TrackingPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		
		ArrayList<RelationalTuple<? extends IMetaAttribute>> elems_copy = new ArrayList<RelationalTuple<? extends IMetaAttribute>>();
		
		for(RelationalTuple<? extends IMetaAttribute> tuple: elems.values()){
			elems_copy.add(tuple);
		}
		
		
		RelationalTuple<? extends IMetaAttribute> ret = new RelationalTuple<IMetaAttribute>(1);
		ret.setAttribute(0, elems_copy);
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
		return ((TrackingPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).addElem(toMerge.restrict(restrictList, true));
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> init(RelationalTuple<? extends IMetaAttribute> in) {
		return new TrackingPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(in);
	}


}
