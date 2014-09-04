package de.uniol.inf.is.odysseus.peer.ddc;

import com.google.common.base.Preconditions;

/**
 * A DDC key is a multidimensional String to act as a key for DDC entries.
 * 
 * @author Michael Brand
 *
 */
public class DDCKey implements Comparable<DDCKey> {

	/**
	 * The key as a String array.
	 */
	private final String[] mKey;

	/**
	 * Creates a new multidimensional DDC key.
	 * 
	 * @param key
	 *            The multidimensional key as a String array. <br />
	 *            Must be not null and must have at least one entry.
	 */
	public DDCKey(String[] key) {

		Preconditions.checkNotNull(key,
				"The key for a DDC entry must be not null!");
		Preconditions
				.checkArgument(key.length > 0,
						"The (multidimensional) key for a DDC entry must have at least one dimension");
		this.mKey = key;

	}

	/**
	 * Creates a new 1-dimensional DDC key.
	 * 
	 * @param key
	 *            The 1-dimensional key as a String. <br />
	 *            Must be not null.
	 */
	public DDCKey(String key) {

		this(new String[] { key });

	}

	/**
	 * Creates a new DDC key as a copy of an existing one.
	 * 
	 * @param key
	 *            The DDC key to be copied. <br />
	 *            Must be not null.
	 */
	public DDCKey(DDCKey key) {

		this(key.mKey);

	}

	@Override
	public DDCKey clone() {

		return new DDCKey(this);

	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof DDCKey)) {

			return false;

		}

		DDCKey key = (DDCKey) obj;

		if (this.mKey.length != key.mKey.length) {

			return false;

		}

		for (int keyIndex = 0; keyIndex < this.mKey.length; keyIndex++) {

			if (!this.mKey[keyIndex].equals(key.mKey[keyIndex])) {

				return false;

			}

		}

		return true;

	}

	@Override
	public int hashCode() {

		int hash = 11;
		for (String partialKey : this.mKey) {

			hash += 7 * partialKey.hashCode();

		}
		return hash;

	}

	@Override
	public String toString() {

		String keyString = "";

		for (String partialKey : this.mKey) {

			keyString += partialKey + ",";

		}

		return keyString.substring(0, keyString.length() - 1);

	}

	/**
	 * Gets the key.
	 * 
	 * @return The key as a String array.
	 */
	public String[] get() {

		return this.mKey;

	}

	@Override
	public int compareTo(DDCKey key) {
		
		Preconditions.checkNotNull(key, "The DDC key must be not null!");
		
		final int lengthThisKey = this.mKey.length;
		final int lengthOtherKey = key.mKey.length;
		
		for(int index = 0; index < lengthThisKey && index < lengthOtherKey; index++) {
			
			int partialComp = this.mKey[index].compareToIgnoreCase(key.mKey[index]);
			
			if(partialComp != 0) {
				
				return partialComp;
				
			}
			
		}
		
		return lengthThisKey - lengthOtherKey;
		
	}

}