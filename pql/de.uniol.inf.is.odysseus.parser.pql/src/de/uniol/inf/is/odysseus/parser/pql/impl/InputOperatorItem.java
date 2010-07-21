package de.uniol.inf.is.odysseus.parser.pql.impl;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public class InputOperatorItem {
	public InputOperatorItem(ILogicalOperator operator, int outputPort) {
		this.operator = operator;
		this.outputPort = outputPort;
	}

	public final ILogicalOperator operator;
	public final int outputPort;
}
