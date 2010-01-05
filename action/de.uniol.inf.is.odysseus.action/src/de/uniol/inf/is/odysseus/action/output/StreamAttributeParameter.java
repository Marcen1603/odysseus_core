package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Class representing an attribute of a datastream element.
 * Used as a parameter for
 * @author Simon Flandergan
 *
 */
public class StreamAttributeParameter implements IActionParameter{
	private Class<?> datatype;
	private Object identifier;
	
	public StreamAttributeParameter(Class<?> datatype, Object identifier) {
		this.datatype = datatype;
		this.identifier = identifier;
	}
	
	public StreamAttributeParameter(SDFDatatype datatype, Object identifier){
		String datatypeName = datatype.getQualName();
		//TODO datentyp entsprechend definieren
		this.identifier = identifier;
	}
	

	@Override
	public Class<?> getParamClass() {
		return datatype;
	}

	@Override
	public ParameterType getType() {
		return ParameterType.Attribute;
	}

	@Override
	public Object getValue() {
		return this.identifier;
	}
}
