package de.uniol.inf.is.odysseus.core.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a special class to represent String values with a numeric value
 * Allows faster compare operations, e.g. in Joins
 * 
 * The type uses a dictionary
 * 
 * TODO: - Optimization: If value can be coded in 64 Bits, do not use the
 * dictionary - How to clear dictionary?
 * 
 * 
 * @author Marco Grawunder
 *
 */
public class DString {

	static Long lastValue = 0l;

	static final Map<Long, java.lang.String> dictionary = new HashMap<>();
	static final Map<java.lang.String, Long> inverseDictionary = new HashMap<>();
	public static final DString EMPTY = new DString("");

	long value;

	public DString(java.lang.String input) {
		encode(input);
	}

	public java.lang.String getValue() {
		return decode(value);
	}

	public long getLongValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// Compare this String with java strings
		if (obj.getClass() == java.lang.String.class) {
			if (decode(value).equals((String)obj)) {
				return true;
			}
		}
		if (getClass() != obj.getClass())
			return false;
		DString other = (DString) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public java.lang.String toString() {
		return decode(value);
	}
	
	public Boolean startsWith(DString right) {
		return decode(value).startsWith(right.toString());
	}

	public Boolean startsWith(String right) {
		return decode(value).startsWith(right);
	}

	public Boolean contains(String string) {
		return decode(value).contains(string);
	}

	public Integer indexOf(String string) {
		return decode(value).indexOf(string);
	}

	public String replace(String b, String string) {
		return decode(value).replace(b, "");
	}

	public DString substring(int beginIndex, int endIndex) {
		return new DString(decode(value).substring(beginIndex, endIndex));
	}
	
	public DString substring(int beginIndex) {
		return new DString(decode(value).substring(beginIndex));
	}

	private java.lang.String decode(long value) {
		return dictionary.get(value);
	}

	private long encode(java.lang.String input) {
		// Must be sure, that two string have not different values!
		synchronized (dictionary) {
			Long k = inverseDictionary.get(input);

			if (k == null) {
				k = lastValue++;
				dictionary.put(k, input);
				inverseDictionary.put(input, k);
			}
			this.value = k;
			return k;
		}
	}
	
	public static String valueOf(Object inputValue) {
		if (inputValue != null){
			return inputValue.toString();
		}
		return "null";
	}










}
