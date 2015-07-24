package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;


public class IQLExpressionParserHelper {
	
	
	@Inject
	private IIQLTypeFactory typeFactory;


	public JvmTypeReference determineType(JvmTypeReference left, JvmTypeReference right) {
		if (typeFactory.isDouble(left) || typeFactory.isDouble(right)) {
			return typeFactory.getTypeRef("double");
		} else if (typeFactory.isFloat(left) || typeFactory.isFloat(right)) {
			return typeFactory.getTypeRef("float");
		} else if (typeFactory.isLong(left) || typeFactory.isLong(right)) {
			return typeFactory.getTypeRef("long");
		} else {
			return typeFactory.getTypeRef("int");		
		}
	}	

}
