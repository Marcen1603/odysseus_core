package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.Count;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.CountPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalCount extends Count<RelationalTuple<?>> {

	private static RelationalCount instance;

	private RelationalCount(){
		super();
	}
	
	public static RelationalCount getInstance(){
		if (instance == null){
			instance = new RelationalCount();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
		CountPartialAggregate<RelationalTuple<?>> pa = (CountPartialAggregate<RelationalTuple<?>>) p;
		RelationalTuple<?> r = new RelationalTuple(1);
		r.setAttribute(0, new Integer(pa.getCount()));
		return r;
	}


}
