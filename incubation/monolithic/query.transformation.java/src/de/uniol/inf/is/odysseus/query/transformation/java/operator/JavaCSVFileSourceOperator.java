package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformCSVParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class JavaCSVFileSourceOperator extends AbstractTransformationOperator {
	
	private final String name =  "CSVFileSource";
	private final String targetPlatform = "Java";
	  
	  
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}
	@Override
	public String getCode(ILogicalOperator operator) {
		
		StringBuilder code = new StringBuilder();
		
		//generate code for SDFSchema
		code.append(TransformSDFSchema.getCodeForSDFSchema(operator.getOutputSchema()));
		
		//generate code for options
		code.append(TransformCSVParameter.getCodeForParameterInfo(operator.getParameterInfos()));
		

		
		return code.toString();
	}

}
