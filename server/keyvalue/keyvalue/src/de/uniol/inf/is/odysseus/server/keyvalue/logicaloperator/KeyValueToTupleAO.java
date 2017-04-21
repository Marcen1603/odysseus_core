package de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "KeyValueToTuple", doc = "Deprecated! Use ToTuple instead", category = {
		LogicalOperatorCategory.TRANSFORM }, deprecation = true, hidden = true)
@Deprecated
public class KeyValueToTupleAO extends ToTupleAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4649605546588871576L;

	public KeyValueToTupleAO() {
	}

	public KeyValueToTupleAO(KeyValueToTupleAO keyValueToTuple) {
		super(keyValueToTuple);
	}


}
