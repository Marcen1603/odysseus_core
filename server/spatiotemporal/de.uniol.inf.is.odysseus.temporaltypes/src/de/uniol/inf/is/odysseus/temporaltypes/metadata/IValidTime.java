package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * This metadata type describes the valid time of a stream element. This time
 * interval is independent from the stream time (the "normal" time interval).
 * This time is used for the temporal types that are predicted to a certain time
 * interval: the valid time. You could also call this the "prediction time".
 * 
 * @author Tobias Brandt
 *
 */
public interface IValidTime extends IMetaAttribute {

	public PointInTime getValidStart();

	public PointInTime getValidEnd();

	public void setValidStart(PointInTime point);

	public void setValidEnd(PointInTime point);

	public void setValidStartAndEnd(PointInTime start, PointInTime end);
	
	public boolean includes(PointInTime p);

}
