package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;

public abstract class AbstractRule implements IOperatorRule {
	
	private ComponentContext context;
	
	private String name = "";
	private String targetPlatform = "";
	
	public AbstractRule(){
	}
	
	public AbstractRule(String name){
		this.name = name;
	}

	public int getPriority() {
		return 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setTragetPlattform(String targetPlatform){
		this.targetPlatform = targetPlatform;
	}

	public String getTargetPlatform(){
		return this.targetPlatform;
	}
	
	public void addDataHandlerFromSDFSchema(ILogicalOperator logicalOperator, QueryAnalyseInformation transformationInformation){
		
		SDFSchema sdfSchema = logicalOperator.getOutputSchema();
		
		for(SDFAttribute attribute : sdfSchema.getAttributes()){
			transformationInformation.addDataHandler(attribute.getDatatype().toString());
		}
		
	}

	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
	}
	
	
    protected void activate(ComponentContext context) {
        this.context = context;
        initRule();
    }

    private void initRule() {
    	String targetPlattform = (String)this.context.getProperties().get("targetPlattform");
    	setTragetPlattform(targetPlattform);
    }
	
}
