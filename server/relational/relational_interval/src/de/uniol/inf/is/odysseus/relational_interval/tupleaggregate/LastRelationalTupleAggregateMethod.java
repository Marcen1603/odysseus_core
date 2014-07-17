package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class LastRelationalTupleAggregateMethod implements
		IRelationalTupleAggregateMethod {

	@Override
	public Tuple<? extends ITimeInterval> process(
			Iterator<Tuple<? extends ITimeInterval>> elems, int pos) {
		Tuple<? extends ITimeInterval> ret = null;
		while (elems.hasNext()) {
			ret = elems.next();
		}
		return ret;
	}

}
