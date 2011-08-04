package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;

abstract class AbstractGeometryAggregation<R, W> extends AbstractListAggregation<R, W> {

	protected AbstractGeometryAggregation(String name) {
		super(name);
	}
		
	@Override
	public IPartialAggregate<R> init(R in) {
	    GeometryPartialAggregate<R> aggregate = new GeometryPartialAggregate<R>();
	    aggregate.addElem(in);
		return aggregate;
	}
	
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
		GeometryPartialAggregate<R> list = (GeometryPartialAggregate<R>) p;
		if (createNew){
			list = new GeometryPartialAggregate<R>((GeometryPartialAggregate<R>)p);
		}
		list.addElem(toMerge);
		return list;
	}


	
}
