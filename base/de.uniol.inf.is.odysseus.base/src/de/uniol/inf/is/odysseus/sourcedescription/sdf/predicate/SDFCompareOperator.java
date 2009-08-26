package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;


/**
 * @author  Marco Grawunder
 */
public interface SDFCompareOperator {

	/**
	 * @uml.property  name="xMLRepresentation"
	 */
	String getXMLRepresentation();

	boolean evaluate(Object o1, Object o2);
}