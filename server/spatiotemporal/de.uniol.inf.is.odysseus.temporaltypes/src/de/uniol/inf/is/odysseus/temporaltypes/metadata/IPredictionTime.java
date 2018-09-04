package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * This metadata type describes the prediction time of a stream element. This
 * time interval is independent from the stream time (the "normal" time
 * interval). This time is used for the temporal types that are predicted to a
 * certain time interval: the prediction time.
 * 
 * @author Tobias Brandt
 *
 */
public interface IPredictionTime extends IMetaAttribute {

	public PointInTime getPredictionStart();

	public PointInTime getPredictionEnd();

	public void setPredictionStart(PointInTime point);

	public void setPredictionEnd(PointInTime point);

	public void setPredictionStartAndEnd(PointInTime start, PointInTime end);

	public boolean includes(PointInTime p);

}
