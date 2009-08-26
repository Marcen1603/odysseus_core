package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFUnequalOperator implements SDFCompareOperator {
	public SDFUnequalOperator() {
	}

	public String toString() {
		return "!=";
	}
	
	public String getXMLRepresentation() {
		return toString();
	}
	
	public boolean evaluate(Object o1, Object o2) {
		return !o1.equals(o2);
	}
}