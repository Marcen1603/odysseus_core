package de.uniol.inf.is.odysseus.systemlog.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.systemlog.ISystemLogEntry;

/**
 * Entry within a system log contains a tag identifying the action, a time stamp
 * and (optional) additional information. Each entry will be formated in CSV.
 * 
 * @author Michael Brand
 *
 */
public class SystemLogEntry implements ISystemLogEntry {

	/**
	 * The tag.
	 */
	private final String tag;

	/**
	 * The time stamp in milliseconds.
	 */
	private final long ts;

	/**
	 * The (optional) additional information.
	 */
	private final Optional<String> info;

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
	public SystemLogEntry(String tag, long timeStamp, String information) {
		Preconditions.checkNotNull(tag);
		this.tag = tag;
		this.ts = timeStamp;
		this.info = Optional.fromNullable(information);
	}

	/**
	 * Creates a new entry without additional information.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 */
	public SystemLogEntry(String tag, long timeStamp) {
		this(tag, timeStamp, null);
	}

	/**
	 * Creates a new entry as a copy of an existing one.
	 * 
	 * @param other
	 *            The entry to copy.
	 */
	public SystemLogEntry(ISystemLogEntry other) {
		Preconditions.checkNotNull(other);
		this.tag = other.getTag();
		this.ts = other.getTimeStamp();
		this.info = other.getInformation();
	}

	@Override
	public ISystemLogEntry clone() {
		return new SystemLogEntry(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof ISystemLogEntry)) {
			return false;
		}

		ISystemLogEntry other = (ISystemLogEntry) obj;
		return this.tag.equals(other.getTag())
				&& this.ts == other.getTimeStamp()
				&& this.info.equals(other.getInformation());
	}

	@Override
	public int hashCode() {
		return 7 + 11 * this.tag.hashCode() + (int) (13 * this.ts)
				+ 17 * this.info.hashCode();
	}

	/**
	 * CSV formated.
	 */
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(this.tag + ", " + this.ts);
		if (this.info.isPresent()) {
			out.append(", " + this.info.get());
		}
		return out.toString();
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	@Override
	public long getTimeStamp() {
		return this.ts;
	}

	@Override
	public Optional<String> getInformation() {
		return this.info;
	}

}