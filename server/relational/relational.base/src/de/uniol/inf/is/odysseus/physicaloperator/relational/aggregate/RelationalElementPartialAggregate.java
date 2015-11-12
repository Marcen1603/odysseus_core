package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

public class RelationalElementPartialAggregate extends
		ElementPartialAggregate<Tuple<?>> {

	private static final long serialVersionUID = -1220783987930401544L;

	public RelationalElementPartialAggregate(Tuple<?> elem, String datatype) {
		super(elem, datatype);
	}
	
	@SuppressWarnings("rawtypes")
	public RelationalElementPartialAggregate(Object elem, String datatype) {
		super(new Tuple(elem, false), datatype);
	}

	public RelationalElementPartialAggregate(IPartialAggregate<Tuple<?>> p) {
		super(p);
	}
	
	public <K> K getValue(){
		return getElem().getAttribute(0);
	}
}
