package de.uniol.inf.is.odysseus.peer.ddc.impl;

import com.google.common.base.Preconditions;

/**
 * A DDC entry is an ADT for a key value pair of Strings with an additional timestamp
 * indicating the point in time of the generation or change of the key value
 * pair. <br />
 * A key for a DDC entry can be multidimensional String to represent table
 * entries.
 * 
 * @author Michael Brand
 *
 */
public class DDCEntry {

	/**
	 * The multidimensional key. <br />
	 * The key must be not null and there must be at least one dimension of a
	 * key.
	 */
	private final String[] mKey;

	/**
	 * The multidimensional key.
	 * 
	 * @return A multidimensional String key with at least one dimension.
	 */
	public String[] getKey() {

		return this.mKey;

	}

	/**
	 * The value. <br />
	 * The value must be not null.
	 */
	private final String mValue;

	/**
	 * The value.
	 * 
	 * @return A not null String.
	 */
	public String getValue() {

		return this.mValue;

	}

	/**
	 * The timestamp indicating the point in time of creation or last change. <br />
	 * The timestamp must be greater zero.
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
	 *            The key must be not null and there must be at least one
	 *            dimension of a key.
	 * @param value
	 *            The value. <br />
	 *            The value must be not null.
	 * @param ts
	 *            The timestamp indicating the point in time of creation or last
	 *            change. <br />
	 *            The timestamp must be greater zero.
	 */
	public DDCEntry(String[] key, String value, long ts) {

		Preconditions.checkNotNull(key,
				"The key for a DDC entry must be not null!");
		Preconditions
				.checkArgument(key.length > 0,
						"The (multidimensional) key for a DDC entry must have at least one dimension");
		this.mKey = key;

		Preconditions.checkNotNull(value,
				"The value for a DDC entry must be not null!");
		this.mValue = value;

		Preconditions.checkArgument(ts > 0,
				"The timestamp for a DDC entry must be greater zero!");
		this.mTS = ts;

	}
	
	/**
	 * Creates a new DDC entry.
	 * 
	 * @param key
	 *            The 1-dimensional key. <br />
	 *            The key must be not null.
	 * @param value
	 *            The value. <br />
	 *            The value must be not null.
	 * @param ts
	 *            The timestamp indicating the point in time of creation or last
	 *            change. <br />
	 *            The timestamp must be greater zero.
	 */
	public DDCEntry(String key, String value, long ts) {

		this(new String[] {key}, value, ts);

	}

	/**
	 * Creates a new DDC entry as a copy of an existing one.
	 * 
	 * @param entry
	 *            The DDC entry to be copied. <br />
	 *            The DDC entry must be not null.
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
		return DDCEntry.equalsWithoutTS(this, entry) && this.mTS == entry.mTS;

	}

	/**
	 * Checks, if two different DDC entries are equal except their timestamps.
	 * 
	 * @param entry1
	 *            A DDC entry to be compared with <code>entry2</code>. <br />
	 *            The DDC entry must be not null.
	 * @param entry2
	 *            A DDC entry to be compared with <code>entry1</code>. <br />
	 *            The DDC entry must be not null.
	 * @return True, if the keys and the values are the same.
	 */
	private static boolean equalsWithoutTS(DDCEntry entry1, DDCEntry entry2) {

		return DDCEntry.compareKeys(entry1.mKey, entry2.mKey)
				&& entry1.mValue.equals(entry2.mValue);

	}

	/**
	 * Checks, if two different keys for DDC entries are equal.
	 * 
	 * @param key1
	 *            A key for a DDC entry to be compared with <code>key2</code>. <br />
	 *            The key must be not null and it must have at least one
	 *            dimension.
	 * @param key2
	 *            A key for aDDC entry to be compared with <code>key1</code>. <br />
	 *            The key must be not null and it must have at least one
	 *            dimension.
	 * @return True, if the keys are the same.
	 */
	private static boolean compareKeys(String[] key1, String[] key2) {

		if (key1.length != key2.length) {

			return false;

		}

		for (int index = 0; index < key1.length; index++) {

			if (!key1[index].equals(key2[index])) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Compares the timestamp of the DDC entry with the timestamp of another DDC
	 * entry.
	 * 
	 * @param entry
	 *            The other DDC entry. <br />
	 *            The DDC entry must be not null.
	 * @return A value lower zero, if <code>entry</code> has a younger
	 *         timestamp, a value greater zero, if <code>entry</code> has an
	 *         older timestamp or zero, if bothe timestamps are the same.
	 */
	public long compareTimeStamps(DDCEntry entry) {

		Preconditions.checkNotNull(entry, "DDCEntry must be not null!");
		Preconditions
				.checkArgument(
						DDCEntry.equalsWithoutTS(this, entry),
						"To compare timestamps of DDC entries, both entries must be the same (except the timestamps)!");

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

		String ddcString = "";

		for (String partialKey : this.mKey) {

			ddcString += partialKey + ", ";

		}
		ddcString = ddcString.substring(0, ddcString.length() - 2);
		ddcString += " = " + this.mValue + " [" + String.valueOf(this.mTS)
				+ "]";
		return ddcString;

	}

}