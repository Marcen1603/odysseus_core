package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class MaxRelationalTupleAggregateMethod implements
		IRelationalTupleAggregateMethod {

	@Override
	public Tuple<? extends ITimeInterval> process(
			Iterator<Tuple<? extends ITimeInterval>> elems, int pos) {
		Number max = null;
		Tuple<? extends ITimeInterval> ret = null;
		while(elems.hasNext()){
			Tuple<? extends ITimeInterval> nextElem = elems.next();
			Number toTest = nextElem.getAttribute(pos);
			if (max == null ||  toTest.doubleValue() > max.doubleValue()){
				max = toTest;
				ret = nextElem;
			}
		}
		return ret;
	}

}
