package de.uniol.inf.is.odysseus.temporaltypes.metadata.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * This operator is used to manipulate the ValidTime metadata field.
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(name = "ChangeValidTime", doc = "Change the metadata ValidTime", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.ADVANCED }, hidden = false)
public class ChangeValidTimeAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6126935973966254984L;

	private int valueToAddStart;
	private int valueToAddEnd;

	public ChangeValidTimeAO() {

	}

	public ChangeValidTimeAO(ChangeValidTimeAO other) {
		super(other);
		this.valueToAddStart = other.getValueToAddStart();
		this.valueToAddEnd = other.getValueToAddEnd();
	}

	@Parameter(name = "addToStartValue", doc = "The value that is added to the start timestamp of the stream time to create the start timestamp of the ValidTime.", type = IntegerParameter.class, optional = false)
	public void setAddStartValue(int valueToAddStart) {
		this.valueToAddStart = valueToAddStart;
	}

	@Parameter(name = "addToEndValue", doc = "The value that is addd to the start timestamp of the stream time to create end end timestamp of the ValidTime.", type = IntegerParameter.class, optional = false)
	public void setAddEndValue(int valueToAddEnd) {
		this.valueToAddEnd = valueToAddEnd;
	}

	/**
	 * 
	 * @return The value that is added to the start timestamp of the stream time to
	 *         create the start timestamp of the ValidTime.
	 */
	public int getValueToAddStart() {
		return this.valueToAddStart;
	}

	/**
	 * 
	 * @return The value that is addd to the start timestamp of the stream time to
	 *         create end end timestamp of the ValidTime.
	 */
	public int getValueToAddEnd() {
		return this.valueToAddEnd;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeValidTimeAO(this);
	}

}
