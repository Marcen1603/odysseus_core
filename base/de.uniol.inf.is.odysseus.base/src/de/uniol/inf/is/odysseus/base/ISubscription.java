package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	public SDFAttributeList getSchema();
}
