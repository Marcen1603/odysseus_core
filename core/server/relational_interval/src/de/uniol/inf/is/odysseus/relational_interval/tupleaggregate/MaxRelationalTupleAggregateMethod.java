package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class MaxRelationalTupleAggregateMethod implements
		IRelationalTupleAggregateMethod {

	@Override
	public void process(Iterator<Tuple<? extends ITimeInterval>> elems,
			int pos, List<Tuple<? extends ITimeInterval>> ret) {
		Number max = null;
		while (elems.hasNext()) {
			Tuple<? extends ITimeInterval> nextElem = elems.next();
			Number toTest = nextElem.getAttribute(pos);
			if (max == null || toTest.doubleValue() > max.doubleValue()) {
				max = toTest;
				ret.clear();
				ret.add(nextElem);
			} else if (toTest.doubleValue() == max.doubleValue()) {
				ret.add(nextElem);
			}
		}
	}
}
