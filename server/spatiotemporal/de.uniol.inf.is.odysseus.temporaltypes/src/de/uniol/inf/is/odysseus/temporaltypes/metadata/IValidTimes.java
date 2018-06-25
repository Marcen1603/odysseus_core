package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * The metadata type for a list of valid times.
 * 
 * @author Tobias Brandt
 *
 */
public interface IValidTimes extends IMetaAttribute {

	public List<IValidTime> getValidTimes();

	/**
	 * Adds a valid time to the list without changing the existing values.
	 * 
	 * @param validTime
	 *            The valid to add
	 */
	public void addValidTime(IValidTime validTime);

	/**
	 * @return True, if the given PointInTime is in the ValidTimes.
	 */
	public boolean includes(PointInTime time);

	/**
	 * Removes all valid times from the list.
	 */
	public void clear();

	/**
	 * The TimeUnit of the valid times, which can differ from the TimeUnit of the
	 * stream.
	 */
	public TimeUnit getPredictionTimeUnit();

	/**
	 * Sets the TimeUnit for the ValidTime, which can differ from the stream
	 * basetime.
	 * 
	 * @param timeUnit
	 *            The new timeunit for the ValidTime
	 */
	public void setTimeUnit(TimeUnit timeUnit);

}
