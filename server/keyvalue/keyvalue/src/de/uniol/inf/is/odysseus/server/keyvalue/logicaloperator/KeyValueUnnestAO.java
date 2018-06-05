package de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="KVUnnest", doc="Creates from one key value object a set of key value objects", category={LogicalOperatorCategory.TRANSFORM})
public class KeyValueUnnestAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 5151104274079028249L;
	
	private String attribute;
	
	public KeyValueUnnestAO(){
	}
	
	public KeyValueUnnestAO(KeyValueUnnestAO keyValueUnnestAO) {
		super(keyValueUnnestAO);
		this.attribute = keyValueUnnestAO.attribute;
	}
	
	public String getAttribute() {
		return attribute;
	}

	@Parameter(type=StringParameter.class, optional = false, doc="The input attribute that should be unnested. Must be a multi value attribute!")
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new KeyValueUnnestAO(this);
	}

}
