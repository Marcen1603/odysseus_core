package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "SASelect", minInputPorts = 1, maxInputPorts = 1, doc = "Select Operator that processes Security Punctuations", url = "-", category = {
		LogicalOperatorCategory.BASE })
public class SASelectAO extends SelectAO {


	/**
	 * 
	 */
	private static final long serialVersionUID = 540726703125811883L;

	public SASelectAO() {
		super();
	}

	public SASelectAO(SelectAO po) {
		super(po);
		
	}
	public SASelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@Override
	public SASelectAO clone() {

		return new SASelectAO(this);
	}

}
