package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name="TOPK", maxInputPorts=1,minInputPorts=1,category={ LogicalOperatorCategory.ADVANCED }, doc = "Calculate the top k elements of the input")
public class TopKAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 8852471127806000337L;

	private SDFAttribute attribute;
	private int k;
	private boolean descending;
	private boolean writeOnlyOnChange;

	TopKAO() {
	}

	TopKAO(TopKAO other) {
		super(other);
		this.attribute = other.attribute;
		this.k = other.k;
		this.descending = other.descending;
		this.writeOnlyOnChange = other.writeOnlyOnChange;
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	@Parameter(name="Attribute", optional = false, type = ResolvedSDFAttributeParameter.class, doc ="The attribute for ordering")
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}

	public int getK() {
		return k;
	}

	@Parameter(name="k", optional = false, type = IntegerParameter.class, doc ="The number of elements to sort")
	public void setK(int k) {
		this.k = k;
	}

	public boolean isDescending() {
		return descending;
	}

	@Parameter(name="descending", optional = true, type = BooleanParameter.class, doc ="Sort descending (default is true)")
	public void setDescending(boolean ascending) {
		this.descending = ascending;
	}

	public boolean isWriteOnlyOnChange() {
		return writeOnlyOnChange;
	}

	@Parameter(name="WriteOnlyOnChange", optional = true, type = BooleanParameter.class, doc ="Output only if the top k elements change, else output for every new input")
	public void setWriteOnlyOnChange(boolean writeOnlyOnChange) {
		this.writeOnlyOnChange = writeOnlyOnChange;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TopKAO(this);
	}

}
