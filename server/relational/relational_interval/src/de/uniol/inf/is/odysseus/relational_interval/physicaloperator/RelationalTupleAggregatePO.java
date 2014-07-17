package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.IRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class RelationalTupleAggregatePO extends AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> sa = new DefaultTISweepArea<>();
	
	final IRelationalTupleAggregateMethod method;
	final int pos;
	final List<Tuple<? extends ITimeInterval>> retList = new LinkedList<>();
	
	public RelationalTupleAggregatePO(IRelationalTupleAggregateMethod method, int pos){
		this.method = method;
		this.pos = pos;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		sa.clear();
	}
	
	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		// find in sweep area all elements before object.start
		Iterator<Tuple<? extends ITimeInterval>> elems = sa.extractElementsBefore(object.getMetadata().getStart());
		retList.clear();
		method.process(elems, pos, retList);
		transfer(retList);
		sa.insert(object);
	}
	
	@Override
	protected void process_close() {
		// TODO: what to do in this case?
	}



}
