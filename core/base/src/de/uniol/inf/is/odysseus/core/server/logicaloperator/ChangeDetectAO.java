package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
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

	public ChangeDetectAO(AbstractLogicalOperator po) {
		super(po);
	}

	public ChangeDetectAO() {
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true)
	public void setAttributes(List<SDFAttribute> outputSchema) {
		this.attributes = new SDFSchema("", outputSchema);
	}

	public SDFSchema getAttributes() {
		return attributes;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema();
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
		if (attributes != null && attributes.size() > 0) {
			int[] comPos = getComparePositions();
			for (int c : comPos) {
				if (c == -1) {
					addError(new IllegalParameterException(
							"Not all attributes in input found!"));
				}
			}
		}
		return super.isValid();
	}
}
