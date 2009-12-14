package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Class representing an attribute of a tuple
 * @author Simon
 *
 */
public class ActionAttribute {
	private Class<?> datatype;
	private int index;
	
	public ActionAttribute(Class<?> datatype, int index) {
		this.datatype = datatype;
		this.index = index;
	}
	
	public ActionAttribute(SDFDatatype datatype, int index){
		String datatypeName = datatype.getQualName();
		//TODO datentyp entsprechend definieren
		this.index = index;
	}
	
	public Class<?> getDatatype() {
		return datatype;
	}
	
	public int getIndex() {
		return index;
	}
}
