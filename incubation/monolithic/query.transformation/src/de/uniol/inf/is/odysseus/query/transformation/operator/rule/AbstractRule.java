package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation;

public abstract class AbstractRule implements IOperatorRule {
	
	private String name = "";
	private String targetPlatform = "";
	
	public AbstractRule(){
	}
	
	public AbstractRule(String name, String targetPlatform){
		this.name = name;
		this.targetPlatform = targetPlatform;
	}

	public int getPriority() {
		return 0;
	}
	
	public String getName() {
		return this.name;
	}

	public String getTargetPlatform(){
		return this.targetPlatform;
	}
	
	public void addDataHandlerFromSDFSchema(ILogicalOperator logicalOperator, TransformationInformation transformationInformation){
		
		SDFSchema sdfSchema = logicalOperator.getOutputSchema();
		
		for(SDFAttribute attribute : sdfSchema.getAttributes()){
			transformationInformation.addDataHandler(attribute.getDatatype().toString());
		}
		
	}

	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			TransformationInformation transformationInformation) {
		
	}
	
}
