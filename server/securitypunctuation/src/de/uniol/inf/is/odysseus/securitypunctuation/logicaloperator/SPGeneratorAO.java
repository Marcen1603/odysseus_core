package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;


@LogicalOperator(name = "generatesp", minInputPorts = 0, maxInputPorts = 1, doc = "Generates SPs and adds them to the Datastream randomly", url = "http://example.com/MyOperator.html", category = {
		LogicalOperatorCategory.BASE })
public class SPGeneratorAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 0L;

	public SPGeneratorAO() {
		super();
	}

	public SPGeneratorAO(SPGeneratorAO spGeneratorAO) {
		super(spGeneratorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {

		return new SPGeneratorAO(this);
	}

}
