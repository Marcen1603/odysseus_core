package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.Arrays;

public class ComplexParameterKey {
	
	private Object[] queryParameters;
	
	private int hashCode;

	public ComplexParameterKey(Object[] queryParameters) {
		this.queryParameters = queryParameters;
		setHashCodeIntern();
	}

	public Object[] getQueryParameters() {
		return queryParameters;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}
	
	private void setHashCodeIntern() {
		final int prime = 31;
		
		this.hashCode = prime + Arrays.hashCode(queryParameters);
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
