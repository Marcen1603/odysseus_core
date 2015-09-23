package de.uniol.inf.is.odysseus.query.codegenerator.executor;


public abstract class AbstractCExecutor implements ICExecutor{
	
	private String name = "";
	private String targetPlatform = "";
	
	public AbstractCExecutor(String name, String targetPlatform) {
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
