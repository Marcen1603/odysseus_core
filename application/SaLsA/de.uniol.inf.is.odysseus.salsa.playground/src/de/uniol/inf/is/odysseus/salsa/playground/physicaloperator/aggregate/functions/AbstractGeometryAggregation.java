package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

abstract class AbstractGeometryAggregation<R, W> extends AbstractListAggregation<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1818420032865729036L;

	protected AbstractGeometryAggregation(String name) {
		super(name);
	}
		
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> init(RelationalTuple<? extends IMetaAttribute> in) {
		return new GeometryPartialAggregate((RelationalTuple<? extends IMetaAttribute>) in);
	}
	
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge, boolean createNew) {
		GeometryPartialAggregate list = (GeometryPartialAggregate) p;
		if (createNew){
			list = new GeometryPartialAggregate((GeometryPartialAggregate)p);
		}
		list.addElem(toMerge);
		return list;
	}


	
}
