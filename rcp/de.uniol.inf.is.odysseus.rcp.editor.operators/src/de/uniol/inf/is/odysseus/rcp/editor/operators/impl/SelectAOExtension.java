package de.uniol.inf.is.odysseus.rcp.editor.operators.impl;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.rcp.editor.operator.ILogicalOperatorExtension;

public class SelectAOExtension implements ILogicalOperatorExtension {

	public SelectAOExtension() {
	}

	@Override
	public ILogicalOperator create() {
		return new SelectAO();
	}

}
