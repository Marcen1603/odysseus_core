package mg.dynaquest.sourcedescription.sdf.predicate;

public class SDFGreaterOrEqualThanOperator implements SDFNumberCompareOperator {
	public SDFGreaterOrEqualThanOperator() {
	}

	public String toString() {
		return ">=";
	}
	
	public String getXMLRepresentation() {
		return "&gt;=";
	}

	public boolean evaluate(double value1, double value2) {
		return value1>=value2;
	}

	public boolean evaluate(String value1, String value2) {
		return value1.compareTo(value2)>=0;
	}
}