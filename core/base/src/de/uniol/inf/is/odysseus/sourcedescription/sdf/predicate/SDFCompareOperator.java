package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.io.Serializable;


/**
 * @author  Marco Grawunder
 */
public interface SDFCompareOperator extends Serializable{

	/**
	 * @uml.property  name="xMLRepresentation"
	 */
	String getXMLRepresentation();

	boolean evaluate(Object o1, Object o2);
}