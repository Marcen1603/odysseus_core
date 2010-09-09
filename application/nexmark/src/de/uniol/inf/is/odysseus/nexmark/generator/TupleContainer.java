package de.uniol.inf.is.odysseus.nexmark.generator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Wrappt ein Tupel damit die Information um was fuer ein Tupel sich handelt mit
 * gesendet werden kann.
 * 
 * @author Bernd Hochschulz
 * 
 */
public class TupleContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	public RelationalTuple<ITimeInterval> tuple;
	public NEXMarkStreamType type;

	public TupleContainer(RelationalTuple<ITimeInterval> tuple, NEXMarkStreamType type) {
		this.tuple = tuple;
		this.type = type;
	}
}
