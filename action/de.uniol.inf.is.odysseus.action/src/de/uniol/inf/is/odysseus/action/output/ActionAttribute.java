package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Class representing an attribute of a datastream element
 * @author Simon
 *
 */
public class ActionAttribute {
	private Class<?> datatype;
	private Object identifier;
	
	public ActionAttribute(Class<?> datatype, Object identifier) {
		this.datatype = datatype;
		this.identifier = identifier;
	}
	
	public ActionAttribute(SDFDatatype datatype, Object identifier){
		String datatypeName = datatype.getQualName();
		//TODO datentyp entsprechend definieren
		this.identifier = identifier;
	}
	
	public Class<?> getDatatype() {
		return datatype;
	}
	
	/**
	 * Returns the identifier which allows retrieving the value of
	 * this attribute; e.g. an index or a unique name
	 * @return
	 */
	public Object getIdentifier() {
		return identifier;
	}
}
