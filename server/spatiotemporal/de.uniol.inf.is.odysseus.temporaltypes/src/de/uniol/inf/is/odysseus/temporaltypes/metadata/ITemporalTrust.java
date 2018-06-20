package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trust.ITrust;

public interface ITemporalTrust extends IMetaAttribute {

	public void setTrust(PointInTime time, ITrust trust);

	public ITrust getTrust(PointInTime time);
	
	public Set<PointInTime> getTemporalPoints();
	
}
