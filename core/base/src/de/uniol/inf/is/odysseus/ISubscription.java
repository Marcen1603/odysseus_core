package de.uniol.inf.is.odysseus;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Marco Grawunder
 *
 * @param <K>
 */

public interface ISubscription<K> {	
	public K getTarget();
	public int getSinkInPort();
	public int getSourceOutPort();
	public SDFAttributeList getSchema();
	
}
