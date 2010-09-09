package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFEqualOperator implements SDFCompareOperator {
	public SDFEqualOperator() {
	}

	@Override
	public String toString() {
		return "==";
	}

	public String getXMLRepresentation() {
		return toString();
	}
	
	public boolean evaluate(Object o1, Object o2) {
		return o1.equals(o2);
	}
//
//	public boolean evaluate(double value1, double value2) {
//		return value1==value2;
//	}
//
//	public boolean evaluate(String value1, String value2) {
//		return value1.equals(value2);
//	}
}