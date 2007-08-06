package mg.dynaquest.sourcedescription.sdf.predicate;


/**
 * @author  Marco Grawunder
 */
public interface SDFCompareOperator {

	/**
	 * @uml.property  name="xMLRepresentation"
	 */
	String getXMLRepresentation();

	boolean evaluate(double value1, double value2);
	boolean evaluate(String value1, String value2);
}