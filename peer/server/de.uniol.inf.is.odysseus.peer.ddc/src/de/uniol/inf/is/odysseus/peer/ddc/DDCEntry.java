package de.uniol.inf.is.odysseus.peer.ddc;

import com.google.common.base.Preconditions;

/**
 * A DDC entry is an ADT for a key value pair of Strings with an additional
 * timestamp indicating the point in time of the generation or change of the key
 * value pair. <br />
 * A key for a DDC entry can be multidimensional String to represent table
 * entries.
 * 
 * @author Michael Brand
 *
 */
public class DDCEntry {

	/**
	 * The multidimensional key.
	 */
	private final DDCKey mKey;

	/**
	 * The multidimensional key.
	 * 
	 * @return A multidimensional String key with at least one dimension.
	 */
	public DDCKey getKey() {

		return this.mKey;

	}

	/**
	 * The value.
	 */
	private final String mValue;

	/**
	 * The value.
	 * 
	 * @return A String representing the value.
	 */
	public String getValue() {

		return this.mValue;

	}

	/**
	 * The timestamp indicating the point in time of creation or last change.
	 */
	private final long mTS;

	/**
	 * The timestamp indicating the point in time of creation or last change.
	 * 
	 * @return A long value greater zero.
	 */
	public long getTimeStamp() {

		return this.mTS;

	}

	/**
	 * Creates a new DDC entry.
	 * 
	 * @param key
	 *            The multidimensional key. <br />
	 *            Must be not null.
	 * @param value
	 *            The value. <br />
	 *            The value must be not null.
	 * @param ts
	 *            The timestamp indicating the point in time of creation or last
	 *            change. <br />
	 *            The timestamp must be greater zero.
	 */
	public DDCEntry(DDCKey key, String value, long ts) {

		Preconditions.checkNotNull(key,
				"The key for a DDC entry must be not null!");
		this.mKey = key;

		Preconditions.checkNotNull(value,
				"The value for a DDC entry must be not null!");
		this.mValue = value;

		Preconditions.checkArgument(ts > 0,
				"The timestamp for a DDC entry must be greater zero!");
		this.mTS = ts;

	}

	/**
	 * Creates a new DDC entry as a copy of an existing one.
	 * 
	 * @param entry
	 *            The DDC entry to be copied. <br />
	 *            Must be not null.
	 */
	public DDCEntry(DDCEntry entry) {

		this(entry.mKey, entry.mValue, entry.mTS);

	}

	@Override
	public DDCEntry clone() {

		return new DDCEntry(this);

	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof DDCEntry)) {

			return false;

		}

		DDCEntry entry = (DDCEntry) obj;
		return this.mKey.equals(entry.mKey) && this.mValue.equals(entry.mValue)
				&& this.mTS == entry.mTS;

	}

	/**
	 * Compares the timestamp of the DDC entry with the timestamp of another DDC
	 * entry.
	 * 
	 * @param entry
	 *            The other DDC entry. <br />
	 *            Must be not null and the key must be same.
	 * @return A value lower zero, if <code>entry</code> has a younger
	 *         timestamp, a value greater zero, if <code>entry</code> has an
	 *         older timestamp or zero, if bothe timestamps are the same.
	 */
	public long compareTimeStamps(DDCEntry entry) {

		Preconditions.checkNotNull(entry, "DDCEntry must be not null!");
		Preconditions
				.checkArgument(this.mKey.equals(entry.mKey),
						"To compare timestamps of DDC entries, both entries must have the same key!");

		return this.mTS - entry.mTS;

	}

	@Override
	public int hashCode() {

		int hash = 7;
		hash += 11 * mKey.hashCode();
		hash += 11 * mValue.hashCode();
		hash += 11 * mTS;
		return hash;

	}

	@Override
	public String toString() {

		return this.mKey.toString() + " = " + this.mValue + " [ts: "
				+ String.valueOf(this.mTS) + "]";

	}

}