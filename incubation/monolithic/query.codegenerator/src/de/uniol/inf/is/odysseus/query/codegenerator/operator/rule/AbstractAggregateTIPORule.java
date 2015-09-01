package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;

public abstract class AbstractAggregateTIPORule<T extends AggregateAO> extends AbstractCIntervalRule<T> {

	public AbstractAggregateTIPORule(String name) {
		super(name);
	}

}
