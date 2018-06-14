package de.uniol.inf.is.odysseus.systemlog;

import com.google.common.base.Optional;

/**
 * Entry within a system log contains a tag identifying the action, a time stamp
 * and (optional) additional information. Each entry will be formated in CSV.
 * 
 * @author Michael Brand
 *
 */
public interface ISystemLogEntry {

	/**
	 * Gets the tag.
	 * 
	 * @return A string identifying the action.
	 */
	public String getTag();

	/**
	 * Gets the time stamp.
	 * 
	 * @return The time stamp of the action in milliseconds.
	 */
	public long getTimeStamp();

	/**
	 * Gets the (optional) additional information.
	 * 
	 * @return An optional string or {@link Optional#absent()}, if there are no
	 *         additional information.
	 */
	public Optional<String> getInformation();

}