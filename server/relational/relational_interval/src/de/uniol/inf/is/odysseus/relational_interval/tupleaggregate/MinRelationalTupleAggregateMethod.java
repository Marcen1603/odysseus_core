package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class MinRelationalTupleAggregateMethod implements
		IRelationalTupleAggregateMethod {

	@Override
	public void process(Iterator<Tuple<? extends ITimeInterval>> elems,
			int pos, List<Tuple<? extends ITimeInterval>> ret) {
		Number min = null;
		while (elems.hasNext()) {
			Tuple<? extends ITimeInterval> nextElem = elems.next();
			Number toTest = nextElem.getAttribute(pos);
			if (min == null || toTest.doubleValue() < min.doubleValue()) {
				min = toTest;
				ret.clear();
				ret.add(nextElem);
			} else if (toTest.doubleValue() == min.doubleValue()) {
				ret.add(nextElem);
			}
		}
	}
}
