package de.uniol.inf.is.odysseus.broker.movingobjects.generator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class TupleContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	public RelationalTuple<ITimeInterval> tuple;
	public MovingObjectsStreamType type;

	public TupleContainer(RelationalTuple<ITimeInterval> tuple, MovingObjectsStreamType type) {
		this.tuple = tuple;
		this.type = type;
	}
}
