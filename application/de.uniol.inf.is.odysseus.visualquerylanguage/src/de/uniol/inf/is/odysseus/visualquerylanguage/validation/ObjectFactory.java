package de.uniol.inf.is.odysseus.visualquerylanguage.validation;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;


public class ObjectFactory {
	
	private static final ObjectFactory objectFac = new ObjectFactory();
	
	public static ObjectFactory getInstance() {
		return objectFac;
	}
	
	public Object getParamType(String value, String paramType) {
		if(paramType.equals("java.lang.String")) {
			if(Validator.getInstance().validateString(value, "")) {
				return value;
			}
		}else if(paramType.equals("de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource")) {
			if(Validator.getInstance().validateString(value, "")) {
				return value;
			}
		}else if(paramType.equals("java.lang.Integer")) {
			if(Validator.getInstance().validateInteger(value)) {
				return Integer.parseInt(value);
			}
		}else if(paramType.equals("de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate")) {
			if(Validator.getInstance().validateSDFExpression(value)) {
				return new SDFExpression(new SDFAttribute(value));
			}
		}
		
		return null;
	}
	
}
