package de.uniol.inf.is.odysseus.core.server.incubation.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

/**
 * Logical combine operator implementation
 * @author Dennis Nowak
 *
 */
@LogicalOperator(name = "COMBINE", minInputPorts = 2, doc = "Takes values of attributes from the input operators and combines them in one tuple", category = { LogicalOperatorCategory.PROCESSING }, maxInputPorts = Integer.MAX_VALUE)
public class CombineAO extends AbstractLogicalOperator {

	private boolean waitForAllChanged;
	private boolean bufferNewInputElements;

	private static final long serialVersionUID = 4265752462231737808L;

	/**
	 * Creates new instance of CombineAO
	 */
	public CombineAO() {
		super();
	}

	/**
	 * Copy constructor for CombineAO
	 * @param combineAO
	 */
	public CombineAO(CombineAO combineAO) {
		super(combineAO);
		this.waitForAllChanged = combineAO.getWaitForAllChanged();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CombineAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos >= getInputSchema(0).size()) {
			return null;
		}
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		int length = this.getNumberOfInputs();
		for (int i = 0; i < length; i++) {
			SDFAttribute attribute = new SDFAttribute(getInputSchema(i)
					.getAttribute(pos).getSourceName(), "Port" + i + "_"
					+ getInputSchema(i).getAttribute(pos).getAttributeName(),
					getInputSchema(i).getAttribute(pos));
			attributes.add(attribute);
		}
		SDFSchema out = new SDFSchema("", Tuple.class, attributes);
		return out;
	}

	@Parameter(name = "WaitForAllChanged", optional = true, type = BooleanParameter.class, doc = "If true, there is only output when there has been input on all ports")
	public void setWaitForAllChanged(boolean wait) {
		this.waitForAllChanged = wait;
	}

	public boolean getWaitForAllChanged() {
		return this.waitForAllChanged;
	}

	@Parameter(name = "BufferNewInputElements", optional = true, type = BooleanParameter.class, doc = "If WaitForAllChanged is set, specifies, if new Input should be buffered or overrides older Input that hast not been transfered yet")
	public void setBufferNewInputElements(boolean buffer) {
		this.bufferNewInputElements = buffer;
	}

	public boolean getBufferNewInputElements() {
		return this.bufferNewInputElements;
	}

	@Override
	public boolean isValid() {
		for (int i = 0; i < this.getNumberOfInputs(); i++) {
			if (this.getInputSchema(i).size() != this.getOutputPorts()) {
				return false;
			}
		}
		return super.isValid();

	}

	public int getOutputPorts() {
		return this.getInputSchema(0).size();
	}

	public int getOutputTupleSize() {
		return this.getNumberOfInputs();
	}

}
