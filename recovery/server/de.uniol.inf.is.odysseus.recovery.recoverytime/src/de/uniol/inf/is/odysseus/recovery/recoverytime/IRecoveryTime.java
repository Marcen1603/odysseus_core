package de.uniol.inf.is.odysseus.recovery.recoverytime;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Meta attribute for recovery time information. <br />
 * <br />
 * Recovery time is defined as the time between the first element after a
 * restart and the first trustworthy element. Recovery time is measured in
 * system time as well as in application time.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryTime extends IMetaAttribute {

	/**
	 * Gets the system time at which the first element after a restart has been
	 * seen; -1 if it has not been seen yet.
	 */
	public long getSystemTimeStart();

	/**
	 * Sets the system time at which the first element after a restart has been
	 * seen.
	 */
	public void setSystemTimeStart(long time);

	/**
	 * Gets the system time at which the first trustworthy element after a
	 * restart has been seen; -1 if it has not been seen yet.
	 */
	public long getSystemTimeEnd();

	/**
	 * Sets the system time at which the first trustworthy element after a
	 * restart has been seen.
	 */
	public void setSystemTimeEnd(long time);

	/**
	 * Gets the system time between the first element after a restart and the
	 * first trustworthy element.
	 */
	public long getRecoverySystemTime();

	/**
	 * Gets the time stamp of the first element after a restart; -1 if it has
	 * not been seen yet.
	 */
	public long getApplicationTimeStart();

	/**
	 * Sets the time stamp of the first element after a restart.
	 */
	public void setApplicationTimeStart(long time);

	/**
	 * Gets the time stamp of the first trustworthy element after a restart; -1
	 * if it has not been seen yet.
	 */
	public long getApplicationTimeEnd();

	/**
	 * Sets the time stamp of the first trustworthy element after a restart.
	 */
	public void setApplicationTimeEnd(long time);

	/**
	 * Gets the application time between the first element after a restart and the
	 * first trustworthy element.
	 */
	public long getRecoveryApplicationTime();

}