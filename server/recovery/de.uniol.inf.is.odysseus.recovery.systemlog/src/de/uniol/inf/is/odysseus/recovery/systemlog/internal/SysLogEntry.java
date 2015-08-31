package de.uniol.inf.is.odysseus.recovery.systemlog.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

/**
 * Entry within a system log contains a tag identifying the action, a time stamp
 * and (optional) additional information. Each entry will be formated in CSV.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class SysLogEntry implements ISysLogEntry {

	/**
	 * The tag.
	 */
	private final String mTag;

	/**
	 * The time stamp in milliseconds.
	 */
	private final long mTimeStamp;

	/**
	 * The (optional) additional information.
	 */
	private final Optional<String> mInformation;

	/**
	 * Creates a new entry with additional information.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @param information
	 *            An optional string or null, if there are no additional information.
	 */
	public SysLogEntry(String tag, long timeStamp, String information) {
		Preconditions.checkNotNull(tag);
		this.mTag = tag;
		this.mTimeStamp = timeStamp;
		this.mInformation = Optional.fromNullable(information);
	}

	/**
	 * Creates a new entry without additional information.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 */
	public SysLogEntry(String tag, long timeStamp) {
		this(tag, timeStamp, null);
	}

	/**
	 * Creates a new entry as a copy of an existing one.
	 * 
	 * @param other
	 *            The entry to copy.
	 */
	public SysLogEntry(ISysLogEntry other) {
		Preconditions.checkNotNull(other);
		this.mTag = other.getTag();
		this.mTimeStamp = other.getTimeStamp();
		this.mInformation = other.getInformation();
	}

	@Override
	public ISysLogEntry clone() {
		return new SysLogEntry(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !ISysLogEntry.class.isInstance(obj)) {
			return false;
		}

		ISysLogEntry other = (ISysLogEntry) obj;
		return this.mTag.equals(other.getTag())
				&& this.mTimeStamp == other.getTimeStamp()
				&& this.mInformation.equals(other.getInformation());
	}

	@Override
	public int hashCode() {
		return 7 + 11 * this.mTag.hashCode() + (int) (13 * this.mTimeStamp)
				+ 17 * this.mInformation.hashCode();
	}

	/**
	 * CSV formated.
	 */
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(this.mTag + ", " + this.mTimeStamp);
		if (this.mInformation.isPresent()) {
			out.append(", " + this.mInformation.get());
		}
		return out.toString();
	}

	@Override
	public String getTag() {
		return this.mTag;
	}

	@Override
	public long getTimeStamp() {
		return this.mTimeStamp;
	}

	@Override
	public Optional<String> getInformation() {
		return this.mInformation;
	}

}