package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface IRelationalTupleAggregateMethod{
	void process(Iterator<Tuple<? extends ITimeInterval>> elems, int pos, List<Tuple<? extends ITimeInterval>> retList);
}
