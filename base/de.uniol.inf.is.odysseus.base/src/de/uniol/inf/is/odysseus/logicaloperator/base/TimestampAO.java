package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TimestampAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = -467482177921504749L;

	private Long startTimestamp;
	private Long endTimestamp;

	public TimestampAO(TimestampAO ao) {
		super(ao);
		this.startTimestamp = ao.startTimestamp;
		this.endTimestamp = ao.endTimestamp;
	}

	public TimestampAO() {
		startTimestamp = null;
		endTimestamp = null;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	public Long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	public boolean hasStartTimestamp() {
		return this.startTimestamp != null;
	}
	
	public boolean hasEndTimestamp() {
		return this.endTimestamp != null;
	}

}
