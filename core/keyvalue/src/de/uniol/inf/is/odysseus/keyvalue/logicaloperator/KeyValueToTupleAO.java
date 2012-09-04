package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class KeyValueToTupleAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 4804826171047928513L;
	
	private boolean keepInputObject = false;

	public KeyValueToTupleAO() {
	}
	
	public KeyValueToTupleAO(KeyValueToTupleAO keyValueToTuple) {
		super(keyValueToTuple);
		this.keepInputObject = keyValueToTuple.keepInputObject;
	}

	public void setKeepInputObject(boolean keepInputObject) {
		this.keepInputObject = keepInputObject;
	}
	
	public boolean isKeepInputObject() {
		return keepInputObject;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new KeyValueToTupleAO(this);
	}
	
	

}
