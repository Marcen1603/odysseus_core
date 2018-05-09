package de.uniol.inf.is.odysseus.temporaltypes.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

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

	/*
	 * TODO Allow expressions to manipulate the timestamps to have a little more
	 * flexibility. Maybe similar to the TimestampAO.
	 */

	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private boolean alignAtEnd = false;
	private boolean copyTimeInterval;

	public ChangeValidTimeAO() {

	}

	public ChangeValidTimeAO(ChangeValidTimeAO other) {
		super(other);
		this.valueToAddStart = other.getValueToAddStart();
		this.valueToAddEnd = other.getValueToAddEnd();
		this.copyTimeInterval = other.isCopyTimeInterval();
		this.alignAtEnd = other.isAlignAtEnd();
	}

	@Parameter(name = "copyTimeInterval", doc = "Set to true if the ValidTimes shall equal the TimeInterval.", type = BooleanParameter.class, optional = true)
	public void setCopyTimeInterval(boolean copyTimeInterval) {
		this.copyTimeInterval = copyTimeInterval;
	}

	public boolean isCopyTimeInterval() {
		return this.copyTimeInterval;
	}

	@Parameter(name = "addToStartValue", doc = "The value that is added to the start timestamp of the stream time to create the start timestamp of the ValidTime.", type = TimeParameter.class, optional = true)
	public void setAddStartValue(TimeValueItem valueToAddStart) {
		this.valueToAddStart = valueToAddStart;
	}

	@Parameter(name = "addToEndValue", doc = "The value that is addd to the start timestamp of the stream time to create end end timestamp of the ValidTime.", type = TimeParameter.class, optional = true)
	public void setAddEndValue(TimeValueItem valueToAddEnd) {
		this.valueToAddEnd = valueToAddEnd;
	}

	/**
	 * 
	 * @return The value that is added to the start timestamp of the stream time to
	 *         create the start timestamp of the ValidTime.
	 */
	public TimeValueItem getValueToAddStart() {
		return this.valueToAddStart;
	}

	/**
	 * 
	 * @return The value that is addd to the start timestamp of the stream time to
	 *         create end end timestamp of the ValidTime.
	 */
	public TimeValueItem getValueToAddEnd() {
		return this.valueToAddEnd;
	}

	public boolean isAlignAtEnd() {
		return alignAtEnd;
	}

	@Parameter(name = "alignAtEnd", doc = "The values set as the ValidTime are aligned at the end timestamp of the streamtime. Default: false..", type = BooleanParameter.class, optional = true)
	public void setAlignAtEnd(boolean alignAtEnd) {
		this.alignAtEnd = alignAtEnd;
	}
	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeValidTimeAO(this);
	}


}
