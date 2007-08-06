package mg.dynaquest.sourcedescription.sdf.predicate;

public class SDFEqualOperator implements SDFStringCompareOperator,
		SDFNumberCompareOperator {
	public SDFEqualOperator() {
	}

	public String toString() {
		return "=";
	}

	public String getXMLRepresentation() {
		return toString();
	}

	public boolean evaluate(double value1, double value2) {
		return value1==value2;
	}

	public boolean evaluate(String value1, String value2) {
		return value1.equals(value2);
	}
}