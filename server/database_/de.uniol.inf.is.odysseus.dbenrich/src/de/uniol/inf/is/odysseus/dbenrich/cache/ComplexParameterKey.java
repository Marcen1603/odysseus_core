package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.Arrays;

/**
 * This class is used to encapsulate query parameters, so that they can be 
 * used as a key in a cache. The array queryParameters itself would otherwise 
 * be compared by reference.
 */
public class ComplexParameterKey {

	private final Object[] queryParameters;

	private final int hashCode;

	public ComplexParameterKey(Object[] queryParameters) {
		this.queryParameters = queryParameters;
		hashCode = genrateHashCode();
	}

	public Object[] getQueryParameters() {
		return queryParameters;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	private int genrateHashCode() {
		final int prime = 31;

		return prime + Arrays.hashCode(queryParameters);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexParameterKey other = (ComplexParameterKey) obj;
		if (!Arrays.equals(queryParameters, other.queryParameters))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComplexParameterKey " + super.toString() + " [parameters=" + Arrays.toString(queryParameters) + "]";
	}
}
