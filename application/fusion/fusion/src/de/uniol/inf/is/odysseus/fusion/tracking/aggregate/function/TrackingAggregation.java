package de.uniol.inf.is.odysseus.fusion.tracking.aggregate.function;

import java.util.ArrayList;
import java.util.Map;


import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class TrackingAggregation<T, R> extends AbstractAggregateFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = 1168495530599195228L;
	private int[] restrictList;
	
	public TrackingAggregation(int[] restrictList) {
		super("Tracker");
		this.restrictList = restrictList;
	}

	@Override
	public Tuple<? extends IMetaAttribute> evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		Map<Integer, Tuple<? extends IMetaAttribute>> elems = ((TrackingPartialAggregate<Tuple<? extends IMetaAttribute>>)p).getElems();
		
		ArrayList<Tuple<? extends IMetaAttribute>> elems_copy = new ArrayList<Tuple<? extends IMetaAttribute>>();
		
		for(Tuple<? extends IMetaAttribute> tuple: elems.values()){
			elems_copy.add(tuple);
		}
		
		
		Tuple<? extends IMetaAttribute> ret = new Tuple<IMetaAttribute>(1);
		ret.setAttribute(0, elems_copy);
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
		return ((TrackingPartialAggregate<Tuple<? extends IMetaAttribute>>)p).addElem(toMerge.restrict(restrictList, true));
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> init(Tuple<? extends IMetaAttribute> in) {
		return new TrackingPartialAggregate<Tuple<? extends IMetaAttribute>>(in);
	}


}
