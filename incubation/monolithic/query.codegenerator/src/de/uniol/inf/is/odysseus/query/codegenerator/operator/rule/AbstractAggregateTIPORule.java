package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;


import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractAggregateTIPORule<T extends AggregateAO> extends AbstractRule<T> {

	public AbstractAggregateTIPORule(String name) {
		super(name);
	}

	public boolean isExecutable(AggregateAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof AggregateAO) {

			return true;
		}

		return false;
	}


}
