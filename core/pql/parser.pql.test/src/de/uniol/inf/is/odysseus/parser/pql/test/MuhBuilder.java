package de.uniol.inf.is.odysseus.parser.pql.test;

import java.util.Collections;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;

public class MuhBuilder extends AbstractOperatorBuilder {

	public MuhBuilder() {
		super(0, 5);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		MuhOperator op = new MuhOperator();
		return op;
	}

	@Override
	public Set<IParameter<?>> getParameters() {
		return Collections.emptySet();
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
