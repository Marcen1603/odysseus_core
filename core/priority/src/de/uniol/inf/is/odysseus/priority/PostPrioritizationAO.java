package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PostPrioritizationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4238316182706318260L;

	private boolean isActive = false;

	public PostPrioritizationAO() {
	}

	public PostPrioritizationAO(PostPrioritizationAO postPriorisationAO) {
		this.isActive = postPriorisationAO.isActive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostPrioritizationAO other = (PostPrioritizationAO) obj;
		if (isActive != other.isActive) {
			return false;
		}
		return true;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	private byte defaultPriority;

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(byte defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public PostPrioritizationAO clone() {
		return new PostPrioritizationAO(this);
	}

}
