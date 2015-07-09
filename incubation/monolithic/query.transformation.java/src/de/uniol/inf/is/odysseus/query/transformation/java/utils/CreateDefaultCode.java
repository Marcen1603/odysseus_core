package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;

public class CreateDefaultCode {
	
	
	public static String initOperator(ILogicalOperator operator){
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		StringBuilder code = new StringBuilder();
		//generate code for SDFSchema
		code.append(TransformSDFSchema.getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
				
	
		return code.toString();
	}

}
