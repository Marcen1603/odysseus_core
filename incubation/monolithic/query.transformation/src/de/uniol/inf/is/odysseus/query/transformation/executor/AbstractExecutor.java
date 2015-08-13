package de.uniol.inf.is.odysseus.query.transformation.executor;


public abstract class AbstractExecutor implements IExecutor{
	
	public String name = "";
	public String targetPlatform = "";
	
	
	public AbstractExecutor(String name, String targetPlatform) {
		this.name = name;
		this.targetPlatform = targetPlatform;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getTargetPlatform() {
		return targetPlatform;
	}


	

}
