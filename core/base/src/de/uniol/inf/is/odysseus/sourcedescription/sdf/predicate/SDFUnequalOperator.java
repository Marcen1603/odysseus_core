package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

public class SDFUnequalOperator implements SDFCompareOperator {
	public SDFUnequalOperator() {
	}

	@Override
	public String toString() {
		return "!=";
	}
	
	@Override
	public String getXMLRepresentation() {
		return toString();
	}
	
	@Override
	public boolean evaluate(Object o1, Object o2) {
		return !o1.equals(o2);
	}
}