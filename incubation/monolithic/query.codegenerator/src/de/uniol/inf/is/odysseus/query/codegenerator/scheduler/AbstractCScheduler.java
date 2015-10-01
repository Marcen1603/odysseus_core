package de.uniol.inf.is.odysseus.query.codegenerator.scheduler;


public abstract class AbstractCScheduler implements ICScheduler{
	
	private String name = "";
	private String targetPlatform = "";
	
	public AbstractCScheduler(String name, String targetPlatform) {
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
