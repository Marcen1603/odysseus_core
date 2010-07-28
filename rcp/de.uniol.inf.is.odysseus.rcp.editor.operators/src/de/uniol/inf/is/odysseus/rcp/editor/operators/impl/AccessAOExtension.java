package de.uniol.inf.is.odysseus.rcp.editor.operators.impl;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.rcp.editor.operator.ILogicalOperatorExtension;

public class AccessAOExtension implements ILogicalOperatorExtension {

	public AccessAOExtension() {
	}

	@Override
	public ILogicalOperator create() {
		return new AccessAO();
	}

}
