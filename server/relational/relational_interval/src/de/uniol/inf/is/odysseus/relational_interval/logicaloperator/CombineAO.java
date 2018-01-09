package de.uniol.inf.is.odysseus.relational_interval.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

/**
 * Logical combine operator implementation
 * 
 * The combine operator combines attributes of incoming tuples. Tuples on output
 * port n will contain the nth attributes of incoming tuples.
 * 
 * @author Dennis Nowak
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(name = "COMBINE", minInputPorts = 2, doc = "Takes values of attributes from the input operators and combines them in one tuple", category = { LogicalOperatorCategory.PROCESSING }, maxInputPorts = Integer.MAX_VALUE)
public class CombineAO extends AbstractLogicalOperator {

	private boolean waitForAllChanged;
	private boolean bufferNewInputElements;
	private boolean outputOnHeartsbeat;

	private static final long serialVersionUID = 4265752462231737808L;

	/**
	 * Creates new instance of CombineAO
	 */
	public CombineAO() {
		super();
	}

	/**
	 * Copy constructor for CombineAO
	 * 
	 * @param combineAO
	 */
	public CombineAO(CombineAO combineAO) {
		super(combineAO);
		this.waitForAllChanged = combineAO.getWaitForAllChanged();
		this.bufferNewInputElements = combineAO.bufferNewInputElements;
		this.outputOnHeartsbeat = combineAO.outputOnHeartsbeat;
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
		SDFSchema out = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
		return out;
	}

	/**
	 * Setter for parameter WaitForAllChanged
	 * @param wait if true, the operator will wait for input on all ports before transfering the output tuples
	 */
	@Parameter(name = "WaitForAllChanged", optional = true, type = BooleanParameter.class, doc = "If true, there is only output when there has been input on all ports")
	public void setWaitForAllChanged(boolean wait) {
		this.waitForAllChanged = wait;
	}

	/**
	 * Getter for parameter WaitForAllChanged
	 * @return parameter WaitForAllChanged
	 */
	public boolean getWaitForAllChanged() {
		return this.waitForAllChanged;
	}
	
	@Parameter(optional = true, type = BooleanParameter.class, doc = "If true, output will also be triggered by an heartbeat")
	public void setOutputOnHeartsbeat(boolean outputOnHeartsbeat) {
		this.outputOnHeartsbeat = outputOnHeartsbeat;
	}
	
	public boolean isOutputOnHeartsbeat() {
		return outputOnHeartsbeat;
	}

	/**
	 * Setter for parameter BufferNewInputElements
	 * @param buffer if true, new input is buffered if there has been input before that has not been transfered to output.
	 */
	@Parameter(name = "BufferNewInputElements", optional = true, type = BooleanParameter.class, doc = "If WaitForAllChanged is set, specifies, if new Input should be buffered or overrides older Input that hast not been transfered yet")
	public void setBufferNewInputElements(boolean buffer) {
		this.bufferNewInputElements = buffer;
	}

	/**
	 * Getter for parameter BufferNewInputElements
	 * @return parameter BufferNewInputElements
	 */
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

	/**
	 * Returns number of ports for output needed
	 * @return umber if ports for output needed
	 */
	public int getOutputPorts() {
		return this.getInputSchema(0).size();
	}

	/**
	 * Returns number of attributes in output tuples
	 * @return number of attributes in output tuples
	 */
	public int getOutputTupleSize() {
		return this.getNumberOfInputs();
	}

}
