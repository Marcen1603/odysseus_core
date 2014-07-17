package de.uniol.inf.is.odysseus.relational_interval.tupleaggregate;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface IRelationalTupleAggregateMethod{
	Tuple<? extends ITimeInterval> process(Iterator<Tuple<? extends ITimeInterval>> elems, int pos);
}
