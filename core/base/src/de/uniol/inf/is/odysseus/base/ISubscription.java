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
//	@Deprecated
//	public int getsinkInPort();
	public int getSinkInPort();
//	@Deprecated
//	public int getsourceOutPort();
	public int getSourceOutPort();
	public SDFAttributeList getSchema();
	
}
