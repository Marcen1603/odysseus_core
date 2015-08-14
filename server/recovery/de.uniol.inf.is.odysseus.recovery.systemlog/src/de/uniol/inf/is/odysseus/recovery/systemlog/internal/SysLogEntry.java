package de.uniol.inf.is.odysseus.recovery.systemlog.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;

/**
 * Entry within a system log contains a tag identifying the action, a time stamp
 * and an optional comment. Each entry will be formated in CSV.
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
	 * The optional comment.
	 */
	private final Optional<String> mComment;

	/**
	 * Creates a new entry with comment.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @param comment
	 *            An optional string or null, if there is no comment.
	 */
	public SysLogEntry(String tag, long timeStamp, String comment) {
		Preconditions.checkNotNull(tag);
		this.mTag = tag;
		this.mTimeStamp = timeStamp;
		this.mComment = Optional.fromNullable(comment);
	}

	/**
	 * Creates a new entry without comment.
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
		this.mComment = other.getComment();
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
				&& this.mComment.equals(other.getComment());
	}

	@Override
	public int hashCode() {
		return 7 + 11 * this.mTag.hashCode() + (int) (13 * this.mTimeStamp)
				+ 17 * this.mComment.hashCode();
	}

	/**
	 * CSV formated.
	 */
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(this.mTag + ", " + this.mTimeStamp);
		if (this.mComment.isPresent()) {
			out.append(", " + this.mComment.get());
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
	public Optional<String> getComment() {
		return this.mComment;
	}

}