package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AbstractListAggregation;

abstract class AbstractGeometryAggregation<R, W> extends AbstractListAggregation<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1818420032865729036L;

	protected AbstractGeometryAggregation(String name) {
		super(name);
	}
		
	@Override
	public IPartialAggregate<R> init(R in) {
		return new GeometryPartialAggregate<R>(in);
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
