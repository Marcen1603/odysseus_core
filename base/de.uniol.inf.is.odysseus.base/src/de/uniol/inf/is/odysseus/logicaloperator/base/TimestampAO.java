package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TimestampAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -467482177921504749L;

	private SDFAttribute startTimestamp;
	private SDFAttribute endTimestamp;
	private boolean isUsingSystemTime;

	public TimestampAO(TimestampAO ao) {
		super(ao);
		this.startTimestamp = ao.startTimestamp;
		this.endTimestamp = ao.endTimestamp;
		this.isUsingSystemTime = ao.isUsingSystemTime;
	}

	public TimestampAO() {
		startTimestamp = null;
		endTimestamp = null;
		isUsingSystemTime = true;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	public SDFAttribute getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(SDFAttribute startTimestamp) {
		this.startTimestamp = startTimestamp;
		if (this.startTimestamp == null) {
			this.isUsingSystemTime = true;
		} else {
			this.isUsingSystemTime = false;
		}
	}

	public SDFAttribute getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(SDFAttribute endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	public boolean hasStartTimestamp() {
		return this.startTimestamp != null;
	}
	
	public boolean hasEndTimestamp() {
		return this.endTimestamp != null;
	}
	
	public boolean isUsingSystemTime() {
		return this.isUsingSystemTime;
	}
	
	public void setIsUsingSystemTime(boolean value) {
		this.isUsingSystemTime = value;
	}

}
