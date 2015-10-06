package de.uniol.inf.is.odysseus.query.codegenerator.scheduler;

import org.osgi.service.component.ComponentContext;


public abstract class AbstractCScheduler implements ICScheduler{
	
	private ComponentContext context;
	
	private String name = "";
	private String targetPlatform = "";
	
	
	public AbstractCScheduler(String name) {
		this.name = name;
	}
	
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
	
	public void setTragetPlattform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}


	/*
	 * call by osgi
	 */
	protected void activate(ComponentContext context) {
		this.context = context;
		initRule();
	}

	private void initRule() {
		String targetPlattform = (String) this.context.getProperties().get(
				"targetPlatform");
		setTragetPlattform(targetPlattform);
	}
	

}
