package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * The metadata type for a list of prediction times.
 * 
 * @author Tobias Brandt
 *
 */
public interface IPredictionTimes extends IMetaAttribute {

	public List<IPredictionTime> getPredictionTimes();

	/**
	 * Adds a prediction time to the list without changing the existing values.
	 * 
	 * @param validTime The valid to add
	 */
	public void addPredictionTime(IPredictionTime validTime);

	/**
	 * @return True, if the given PointInTime is in the PredictionTimes.
	 */
	public boolean includes(PointInTime time);

	/**
	 * Removes all prediction times from the list.
	 */
	public void clear();

	/**
	 * The TimeUnit of the prediction times, which can differ from the TimeUnit of
	 * the stream.
	 */
	public TimeUnit getPredictionTimeUnit();

	/**
	 * Sets the TimeUnit for the PredictionTime, which can differ from the stream
	 * basetime.
	 * 
	 * @param timeUnit The new timeunit for the ValidTime
	 */
	public void setTimeUnit(TimeUnit timeUnit);

}
