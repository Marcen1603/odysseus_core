package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFLowerOrEqualThanOperator implements SDFCompareOperator {
	public SDFLowerOrEqualThanOperator() {
	}

	@Override
	public String toString() {
		return "<=";
	}
	
	@Override
	public String getXMLRepresentation() {
		return "&lt;=";
	}

	@Override
	public boolean evaluate(Object o1, Object o2) {
		if (o1 instanceof String) {
			return ((String)o1).compareTo((String) o2) <= 0;
		} else {
			return ((Number)o1).doubleValue() <= ((Number)o2).doubleValue();
		}
	}
}