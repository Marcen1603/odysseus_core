package de.uniol.inf.is.odysseus.priority;

import java.util.Map;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PriorityAO<T> extends UnaryLogicalOp {

	private static final long serialVersionUID = -4615895793429076670L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + defaultPriority;
		result = prime * result
				+ ((priorities == null) ? 0 : priorities.hashCode());
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
		PriorityAO<?> other = (PriorityAO<?>) obj;
		if (defaultPriority != other.defaultPriority)
			return false;
		if (priorities == null) {
			if (other.priorities != null)
				return false;
		} else if (!priorities.equals(other.priorities))
			return false;
		return true;
	}

	private Map<Byte, IPredicate<? super T>> priorities;
	private byte defaultPriority;

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(byte defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public PriorityAO() {
		this.priorities = new TreeMap<Byte, IPredicate<? super T>>();
		this.defaultPriority = 0;
	}

	public PriorityAO(PriorityAO<T> priorityAO) {
		this.priorities = priorityAO.priorities;
	}

	public Map<Byte, IPredicate<? super T>> getPriorities() {
		return priorities;
	}

	public void setPriorities(Map<Byte, IPredicate<? super T>> priorities) {
		this.priorities = priorities;
	}

	public void setPriority(byte priority, IPredicate<? super T> predicate) {
		if (this.priorities.containsKey(priority)) {
			throw new IllegalArgumentException("rdefinition of priority: "
					+ priority);
		}
		this.priorities.put(priority, predicate);
	}

	@Override
	public PriorityAO<T> clone() {
		return new PriorityAO<T>(this);
	}

	private boolean isPunctuationActive = false;

	public void setPunctuationActive(boolean isPunctuationActive) {
		this.isPunctuationActive = isPunctuationActive;
	}

	public boolean isPunctuationActive() {
		return isPunctuationActive;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

}
