package de.uniol.inf.is.odysseus.aggregation.functions.impl;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class Min<M extends ITimeInterval, T extends Tuple<M>> extends MinMax<M, T> {

	private static final long serialVersionUID = 4149646620943209670L;

	public Min() {
		super(true);
	}

}
