package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;


/**
 * 
 * @author Marco Grawunder, Jonas Jacobi
 *
 * @param <K>
 */

public class Subscription<K> implements ISubscription<K>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5744808958349736195L;
	private K target;
	private int sinkPort;
	private int sourcePort;

	public Subscription(K target, int sinkPort, int sourcePort) {
		this.target = target;
		this.sinkPort = sinkPort;
		this.sourcePort = sourcePort;
	}

	@Override
	public K getTarget() {
		return target;
	}
	@Override
	public int getSinkPort() {
		return sinkPort;
	}
	@Override
	public int getSourcePort() {
		return sourcePort;
	}

	@Override
	public String toString() {
		return target+" "+sinkPort+" "+sourcePort;
	}

	// ACHTUNG: BEI DER GENERIERUNG VON HASHCODE UND EQUALS
	// DARF NIEMALS EIN EQUALS AUF DEN TARGETS ERFOLGEN, DA DIE WIEDER IHRE 
	// SUBSCRIPTIONS TESTEN WÜRDEN --> REKURSION --> STACK OVERFLOW!!
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sinkPort;
		result = prime * result + sourcePort;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (sinkPort != other.sinkPort)
			return false;
		if (sourcePort != other.sourcePort)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!(target == other.target)) // ACHTUNG. KEIN EQUALS AUF DER TARGET!!
			return false;
		return true;
	}
	
	
	
	
}
