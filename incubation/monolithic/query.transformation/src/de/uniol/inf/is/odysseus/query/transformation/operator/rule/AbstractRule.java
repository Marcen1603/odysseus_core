package de.uniol.inf.is.odysseus.query.transformation.operator.rule;



public abstract class AbstractRule implements IOperatorRule {
	
	private String name = "";
	private String targetPlatform = "";
	
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

}
