/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.base;

public class Subscription<K> {
	final public K target;
	final public int port;
	public boolean done = false;

	public Subscription(K target, int port) {
		this.target = target;
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + port;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription<?> other = (Subscription<?>) obj;
		if (port != other.port)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return target+" "+port;
	}
}