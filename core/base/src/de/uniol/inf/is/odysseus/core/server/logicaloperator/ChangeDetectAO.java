package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event
 * 
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(name = "CHANGEDETECT", minInputPorts = 1, maxInputPorts = 1)
public class ChangeDetectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9042464546094886480L;
	private SDFSchema attributes;
	private int rate;
	private boolean deliverFirstElement = false;

	public ChangeDetectAO(ChangeDetectAO po) {
		super(po);
		attributes = po.attributes;
		this.rate = po.rate;
		this.deliverFirstElement = po.deliverFirstElement;
	}

	public ChangeDetectAO() {
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTR", isList = true)
	public void setAttr(List<SDFAttribute> outputSchema) {
		this.attributes = new SDFSchema("", outputSchema);
	}

	public SDFSchema getAttributes() {
		return attributes;
	}
	
	
	@Parameter(type = IntegerParameter.class, name = "heartbeatrate")
	public void setHeartbeatRate(int rate){
		this.rate = rate;
	}

	public int getHeartbeatRate() {
		return rate;
	}

	public boolean isDeliverFirstElement() {
		return deliverFirstElement;
	}

	@Parameter(type = BooleanParameter.class, name = "deliverFirstElement", optional = true)
	public void setDeliverFirstElement(boolean deliverFirstElement) {
		this.deliverFirstElement = deliverFirstElement;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeDetectAO(this);
	}

	public int[] getComparePositions() {
		SDFSchema inputSchema = getInputSchema();
		if (attributes.size() > 0) {
			int[] ret = new int[attributes.size()];
			int i = 0;
			for (SDFAttribute a : attributes) {
				ret[i++] = inputSchema.indexOf(a);
			}
			return ret;
		} else {
			return null;
		}
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (hasAttributes()) {
			int[] comPos = getComparePositions();
			for (int c : comPos) {
				if (c == -1) {
					addError(new IllegalParameterException(
							"Not all attributes in input found!"));
					isValid = false;
				}
			}
		}
		return isValid && super.isValid();
	}

	public boolean hasAttributes() {
		return attributes != null && attributes.size() > 0;
	}

}
