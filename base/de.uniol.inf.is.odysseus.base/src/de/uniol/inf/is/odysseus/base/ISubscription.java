package de.uniol.inf.is.odysseus.base;

/**
 * 
 * @author Marco Grawunder
 *
 * @param <K>
 */

public interface ISubscription<K> {	
	public K getTarget();
	public int getSinkPort();
	public int getSourcePort();
}
