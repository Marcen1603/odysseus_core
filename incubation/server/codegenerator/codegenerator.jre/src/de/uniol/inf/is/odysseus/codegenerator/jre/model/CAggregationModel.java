package de.uniol.inf.is.odysseus.codegenerator.jre.model;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * helping modell for CAggregationModel codegeneration. Used by the 
 * AggreationTIPO rule
 * 
 * @author MarcPreuschaft
 *
 */
public class CAggregationModel {
	
	SDFAttribute outAttribute;
	String functionName = "";
	SDFAttribute attribute;
	
	public CAggregationModel(SDFAttribute outAttribute, String functionName,
			SDFAttribute attribute) {
		this.outAttribute = outAttribute;
		this.functionName = functionName;
		this.attribute = attribute;
	}

	public SDFAttribute getOutAttribute() {
		return outAttribute;
	}

	public void setOutAttribute(SDFAttribute outAttribute) {
		this.outAttribute = outAttribute;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}
	
	

	
	

}
