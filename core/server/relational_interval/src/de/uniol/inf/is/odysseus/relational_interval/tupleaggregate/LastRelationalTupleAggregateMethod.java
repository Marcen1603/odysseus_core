package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class LastRelationalTupleAggregateMethod implements
		IRelationalTupleAggregateMethod {

	@Override
	public void process(Iterator<Tuple<? extends ITimeInterval>> elems,
			int pos, List<Tuple<? extends ITimeInterval>> ret) {
		Tuple<? extends ITimeInterval> el = null;
		while (elems.hasNext()) {
			el = elems.next();
		}
		if (el != null) {
			ret.add(el);
		}
	}

}
